package com.genpause.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.genpause.app.data.preferences.PreferencesManager
import com.genpause.app.domain.*
import com.genpause.app.overlay.OverlayController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

/**
 * AccessibilityService that detects foreground app changes.
 * Only reads package name — never reads screen content, text, or passwords.
 *
 * Grace window: if user leaves a protected app and returns within N seconds,
 *               the overlay is skipped (no nag on quick task-switch).
 *
 * Re-intervention: after M minutes inside a protected app, the overlay
 *                  re-appears to give the user a mindful check-in.
 */
class ZenPauseAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "GenPauseA11y"
        private const val SURGICAL_SCAN_THROTTLE_MS = 500L
    }

    private var overlayController: OverlayController? = null
    private var rulesEngine: RulesEngine? = null
    private var preferencesManager: PreferencesManager? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Debounce: track last intercepted package + timestamp
    private var lastPackage: String? = null
    private var lastTriggerTime = 0L
    private val debounceMillis = 800L
    private var isInitialized = false

    // Grace window: per-package last-exit timestamp
    private val lastExitTimes = mutableMapOf<String, Long>()

    // Re-intervention: per-package active coroutine job
    private val reInterventionJobs = mutableMapOf<String, Job>()

    // Currently foregrounded protected package (null if none)
    private var currentForegroundProtected: String? = null

    // Surgical blocking: last scan timestamp to throttle
    private var lastSurgicalScanTime = 0L

    // Media event filter: suppress interventions briefly during screen capture
    private var mediaEventSuppressUntil = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected called")

        try {
            val info = AccessibilityServiceInfo().apply {
                eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                            AccessibilityEvent.TYPE_WINDOWS_CHANGED or
                            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC or
                              AccessibilityServiceInfo.FEEDBACK_HAPTIC
                flags = AccessibilityServiceInfo.DEFAULT or
                       AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                notificationTimeout = 300
            }
            serviceInfo = info
            Log.d(TAG, "ServiceInfo configured successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to configure serviceInfo: ${e.message}")
        }

        initDependencies()
    }

    private fun initDependencies() {
        try {
            val app = application as? com.genpause.app.ZenPauseApp
            if (app == null) {
                Log.e(TAG, "Application is not ZenPauseApp!")
                return
            }
            rulesEngine = app.rulesEngine
            preferencesManager = app.preferencesManager
            overlayController = OverlayController(this, app.rulesEngine, app.preferencesManager)
            isInitialized = true
            Log.d(TAG, "Dependencies initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init dependencies: ${e.message}", e)
            isInitialized = false
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        val eventType = event.eventType

        if (eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            eventType != AccessibilityEvent.TYPE_WINDOWS_CHANGED &&
            eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            return
        }

        // Media event filter: suppress during screen capture
        val now = System.currentTimeMillis()
        if (now < mediaEventSuppressUntil) return
        if (packageName == "com.android.systemui" &&
            (event.className?.toString()?.contains("Screenshot", ignoreCase = true) == true ||
             event.className?.toString()?.contains("MediaProjection", ignoreCase = true) == true)) {
            mediaEventSuppressUntil = now + 2000L
            return
        }

        // Handle surgical blocking via content changes (throttled)
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            handleContentChange(packageName, now)
            return
        }

        Log.d(TAG, "Event: type=${eventType}, package=$packageName")

        if (!isInitialized) {
            initDependencies()
            if (!isInitialized) return
        }

        // Own package / system UI → ignore
        if (packageName == applicationContext.packageName) return
        if (isIgnoredPackage(packageName)) return

        // Debounce same package
        if (packageName == lastPackage && (now - lastTriggerTime) < debounceMillis) return
        lastPackage = packageName
        lastTriggerTime = now

        // Skip if overlay already showing
        if (overlayController?.isShowing == true) {
            Log.d(TAG, "Overlay already showing, skipping")
            return
        }

        serviceScope.launch {
            try {
                val rules = rulesEngine ?: return@launch
                val prefs = preferencesManager ?: return@launch
                val overlay = overlayController ?: return@launch

                val targetApp = rules.shouldIntercept(packageName, applicationContext.packageName)

                if (targetApp == null) {
                    // Not a protected app. If we were tracking one, record its exit.
                    val prevPkg = currentForegroundProtected
                    if (prevPkg != null && prevPkg != packageName) {
                        Log.d(TAG, "Left protected app: $prevPkg")
                        lastExitTimes[prevPkg] = now
                        cancelReIntervention(prevPkg)
                        currentForegroundProtected = null
                    }
                    return@launch
                }

                Log.d(TAG, "TARGET APP DETECTED: $packageName")

                // ── Grace window check ──
                val graceWindowSec = prefs.graceWindowSeconds.first()
                val lastExit = lastExitTimes[packageName] ?: 0L
                val timeSinceLeavingMs = now - lastExit
                val inGrace = lastExit > 0L && timeSinceLeavingMs < (graceWindowSec * 1000L)

                currentForegroundProtected = packageName

                if (inGrace) {
                    Log.d(TAG, "Within grace window (${timeSinceLeavingMs}ms < ${graceWindowSec * 1000}ms), skipping overlay")
                    // Resume re-intervention timer if not already running
                    if (targetApp.reInterventionEnabled && !reInterventionJobs.containsKey(packageName)) {
                        startReInterventionTimer(packageName, targetApp.reInterventionIntervalMin)
                    }
                    return@launch
                }

                // ── Show overlay ──
                val baseMode = try {
                    AppMode.valueOf(prefs.currentMode.first())
                } catch (_: Exception) { AppMode.CHILL }
                val effectiveMode = rules.getEffectiveMode(baseMode)
                val delay = rules.getDelay(targetApp)
                val requiresReason = rules.requiresReason(targetApp, effectiveMode)

                val emojiEnabled = prefs.emojiEnabled.first()
                val activePacks = prefs.activeTonePacks.first()
                val (promptId, promptText) = PromptPack.getRandomPromptText(activePacks, emojiEnabled)
                val todayAttempts = rules.getTodayAttemptCount(packageName)

                Log.d(TAG, "Showing overlay: delay=${delay}s, mode=$effectiveMode")

                withContext(Dispatchers.Main) {
                    overlay.showOverlay(
                        packageName = packageName,
                        delaySeconds = delay,
                        requiresReason = requiresReason,
                        mode = effectiveMode,
                        promptText = promptText,
                        promptId = promptId,
                        todayAttempts = todayAttempts + 1
                    )
                }

                // ── Start re-intervention timer ──
                if (targetApp.reInterventionEnabled) {
                    startReInterventionTimer(packageName, targetApp.reInterventionIntervalMin)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error processing event: ${e.message}", e)
            }
        }
    }

    // ── Re-Intervention ──

    private fun startReInterventionTimer(packageName: String, intervalMin: Int) {
        cancelReIntervention(packageName)

        val job = serviceScope.launch {
            delay(intervalMin * 60 * 1000L)

            // Only fire if user is still in this app and no overlay is up
            if (currentForegroundProtected != packageName) return@launch
            if (overlayController?.isShowing == true) return@launch

            Log.d(TAG, "Re-intervention triggered for $packageName after ${intervalMin}m")

            val rules = rulesEngine ?: return@launch
            val prefs = preferencesManager ?: return@launch
            val overlay = overlayController ?: return@launch

            val app = rules.shouldIntercept(packageName, applicationContext.packageName) ?: return@launch
            val baseMode = try { AppMode.valueOf(prefs.currentMode.first()) } catch (_: Exception) { AppMode.CHILL }
            val effectiveMode = rules.getEffectiveMode(baseMode)
            val delay = rules.getDelay(app)
            val requiresReason = rules.requiresReason(app, effectiveMode)
            val emojiEnabled = prefs.emojiEnabled.first()
            val activePacks = prefs.activeTonePacks.first()
            val (promptId, promptText) = PromptPack.getRandomPromptText(activePacks, emojiEnabled)
            val todayAttempts = rules.getTodayAttemptCount(packageName)

            withContext(Dispatchers.Main) {
                overlay.showOverlay(packageName, delay, requiresReason, effectiveMode, promptText, promptId, todayAttempts + 1)
            }

            // Schedule next re-intervention after this one fires
            reInterventionJobs.remove(packageName)
            startReInterventionTimer(packageName, intervalMin)
        }

        reInterventionJobs[packageName] = job
    }

    private fun cancelReIntervention(packageName: String) {
        reInterventionJobs.remove(packageName)?.cancel()
    }

    private fun cancelAllReInterventions() {
        reInterventionJobs.values.forEach { it.cancel() }
        reInterventionJobs.clear()
    }
    // ── Surgical Blocking ──

    private fun handleContentChange(packageName: String, now: Long) {
        // Throttle scanning to avoid performance issues
        if (now - lastSurgicalScanTime < SURGICAL_SCAN_THROTTLE_MS) return
        lastSurgicalScanTime = now

        // Only scan if the package has surgical targets defined
        if (packageName !in UiTreeScanner.getSupportedPackages()) return
        if (overlayController?.isShowing == true) return

        serviceScope.launch {
            try {
                val rootNode = rootInActiveWindow ?: return@launch
                val scanResult = UiTreeScanner.scanForAddictiveElements(rootNode, packageName)

                if (scanResult.found) {
                    Log.d(TAG, "Surgical block triggered: ${scanResult.matchedTarget?.description}")

                    val rules = rulesEngine ?: return@launch
                    val prefs = preferencesManager ?: return@launch
                    val overlay = overlayController ?: return@launch

                    val emojiEnabled = prefs.emojiEnabled.first()
                    val activePacks = prefs.activeTonePacks.first()
                    val (promptId, promptText) = PromptPack.getRandomPromptText(activePacks, emojiEnabled)
                    val todayAttempts = rules.getTodayAttemptCount(packageName)

                    val baseMode = try {
                        AppMode.valueOf(prefs.currentMode.first())
                    } catch (_: Exception) { AppMode.CHILL }
                    val effectiveMode = rules.getEffectiveMode(baseMode)

                    withContext(Dispatchers.Main) {
                        overlay.showOverlay(
                            packageName = packageName,
                            delaySeconds = 5,
                            requiresReason = false,
                            mode = effectiveMode,
                            promptText = "Caught you heading to ${scanResult.matchedTarget?.description} 👀\n$promptText",
                            promptId = promptId,
                            todayAttempts = todayAttempts + 1
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Surgical scan error: ${e.message}")
            }
        }
    }

    // ── Helpers ──

    private fun isIgnoredPackage(packageName: String): Boolean {
        val exact = setOf(
            "android", "com.android.systemui", "com.android.launcher",
            "com.android.launcher3", "com.google.android.apps.nexuslauncher",
            "com.google.android.inputmethod.latin", "com.google.android.gms",
            "com.google.android.gsf", "com.android.settings", "com.android.phone",
            "com.android.dialer", "com.android.contacts", "com.android.mms"
        )
        if (packageName in exact) return true
        val prefixes = listOf(
            "com.android.systemui", "com.sec.android.",
            "com.samsung.android.launcher", "com.miui.home", "com.huawei.android.launcher"
        )
        return prefixes.any { packageName.startsWith(it) }
    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt called")
        overlayController?.dismissOverlay()
        cancelAllReInterventions()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
        overlayController?.destroy()
        cancelAllReInterventions()
        serviceScope.cancel()
    }
}

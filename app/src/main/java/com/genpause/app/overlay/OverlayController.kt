package com.genpause.app.overlay

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.genpause.app.R
import com.genpause.app.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

/**
 * Controls the full-screen system overlay using WindowManager.
 * Handles the COUNTDOWN -> DECISION -> EMOTION -> DISMISSED state machine.
 *
 * Supports multiple intervention types:
 * - BREATH: Default breathing exercise countdown
 * - REASON: Must type a reason before continuing (hard mode)
 * - REFLECTION: "Why are you opening this app?" + selectable responses
 * - MATH_PUZZLE: Solve a random arithmetic problem
 * - FOLLOW_DOT: Visual tracking exercise (delegates to animation)
 * - MINI_TASK: Type specific characters
 */
class OverlayController(
    private val context: Context,
    private val rulesEngine: RulesEngine,
    private val preferencesManager: com.genpause.app.data.preferences.PreferencesManager
) {
    companion object {
        private const val TAG = "GenPauseOverlay"
    }

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var overlayView: View? = null
    private var countdownTimer: CountDownTimer? = null
    private var currentState = OverlayState.DISMISSED
    private var currentPackage: String? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Emotion/intention tracking state
    private var selectedEmotion: String? = null
    private var selectedIntention: Int? = null
    private var currentMathPuzzle: InterventionModules.MathPuzzle? = null
    private var currentInterventionType: InterventionType = InterventionType.BREATH

    @Volatile
    var isShowing = false
        private set

    /**
     * Show the overlay for a target app.
     */
    fun showOverlay(
        packageName: String,
        delaySeconds: Int,
        requiresReason: Boolean,
        mode: AppMode,
        promptText: String,
        promptId: String,
        todayAttempts: Int
    ) {
        if (isShowing) return

        currentPackage = packageName
        isShowing = true
        currentState = OverlayState.COUNTDOWN
        selectedEmotion = null
        selectedIntention = null

        // Determine intervention type using rotation
        val configuredType = if (requiresReason) InterventionType.REASON else InterventionType.BREATH
        currentInterventionType = InterventionModules.getNextInterventionType(configuredType)
        Log.d(TAG, "Intervention type: $currentInterventionType")

        val params = createLayoutParams(focusable = false)

        val inflater = LayoutInflater.from(context)
        overlayView = inflater.inflate(R.layout.overlay_intervention, null)

        setupOverlayViews(
            delaySeconds = delaySeconds,
            requiresReason = requiresReason,
            mode = mode,
            promptText = promptText,
            promptId = promptId,
            todayAttempts = todayAttempts,
            packageName = packageName,
            params = params
        )

        try {
            windowManager.addView(overlayView, params)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add overlay: ${e.message}")
            isShowing = false
            currentState = OverlayState.DISMISSED
            return
        }

        scope.launch {
            rulesEngine.logAttempt(packageName, delaySeconds, mode, promptId)
        }

        startCountdown(delaySeconds, requiresReason, params)
    }

    private fun setupOverlayViews(
        delaySeconds: Int,
        requiresReason: Boolean,
        mode: AppMode,
        promptText: String,
        promptId: String,
        todayAttempts: Int,
        packageName: String,
        params: WindowManager.LayoutParams
    ) {
        val view = overlayView ?: return

        // Basic views
        view.findViewById<TextView>(R.id.tvPrompt).text = promptText
        view.findViewById<TextView>(R.id.tvStat).text =
            context.getString(R.string.overlay_stat_format, todayAttempts)
        view.findViewById<TextView>(R.id.tvCountdown).text = delaySeconds.toString()
        view.findViewById<BreathingRingView>(R.id.breathingRing).startBreathingAnimation()

        // Setup intervention-type-specific UI
        setupInterventionType(view, params)

        // Setup decision buttons
        setupDecisionButtons(view, requiresReason, mode, packageName, params)

        // Setup emotion tracking chips
        setupEmotionChips(view)

        // Setup intention duration buttons
        setupIntentionButtons(view)
    }

    /**
     * Configure UI elements specific to the current intervention type.
     */
    private fun setupInterventionType(view: View, params: WindowManager.LayoutParams) {
        when (currentInterventionType) {
            InterventionType.REFLECTION -> {
                val container = view.findViewById<LinearLayout>(R.id.reflectionContainer)
                container.visibility = View.VISIBLE
                val question = view.findViewById<TextView>(R.id.tvReflectionQuestion)
                question.text = InterventionModules.getRandomPrompt()

                // Hide breathing ring for reflection-based intervention
                view.findViewById<BreathingRingView>(R.id.breathingRing).visibility = View.GONE
                view.findViewById<TextView>(R.id.tvCountdown).visibility = View.GONE

                // Add response options dynamically
                InterventionModules.reflectionResponses.forEach { option ->
                    val optionView = TextView(context).apply {
                        text = option.text
                        textSize = 15f
                        setTextColor(context.getColor(R.color.zen_text_primary))
                        setBackgroundColor(context.getColor(R.color.zen_elevated_surface))
                        setPadding(32, 24, 32, 24)
                        val optionParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply { bottomMargin = 12 }
                        layoutParams = optionParams
                        setOnClickListener { v ->
                            vibrateLight()
                            applyMicroBounce(v)
                            // Highlight selected, show buttons
                            v.setBackgroundColor(context.getColor(R.color.zen_primary_alpha))
                        }
                    }
                    container.addView(optionView)
                }
            }

            InterventionType.MATH_PUZZLE -> {
                val container = view.findViewById<LinearLayout>(R.id.mathPuzzleContainer)
                container.visibility = View.VISIBLE
                currentMathPuzzle = InterventionModules.generateMathPuzzle()
                view.findViewById<TextView>(R.id.tvMathPuzzle).text = currentMathPuzzle?.display ?: ""

                // Hide breathing ring for math puzzle
                view.findViewById<BreathingRingView>(R.id.breathingRing).visibility = View.GONE
                view.findViewById<TextView>(R.id.tvCountdown).visibility = View.GONE

                // Make focusable for keyboard
                try {
                    params.flags = params.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
                    windowManager.updateViewLayout(overlayView, params)
                } catch (_: Exception) {}
            }

            InterventionType.REASON -> {
                view.findViewById<EditText>(R.id.etReason).visibility = View.VISIBLE
            }

            // BREATH, MINI_TASK, FOLLOW_DOT use default breathing ring UI
            else -> { /* default setup is fine */ }
        }
    }

    /**
     * Setup the "Open Anyway", "Go Back", and "Snooze" buttons.
     */
    private fun setupDecisionButtons(
        view: View,
        requiresReason: Boolean,
        mode: AppMode,
        packageName: String,
        params: WindowManager.LayoutParams
    ) {
        val btnOpen = view.findViewById<TextView>(R.id.btnOpenAnyway)
        val btnBack = view.findViewById<TextView>(R.id.btnGoBack)
        val btnSnooze = view.findViewById<TextView>(R.id.btnSnooze)
        val etReason = view.findViewById<EditText>(R.id.etReason)

        // Hide "Continue" button in STRICT mode (Boss mode with strict schedule)
        if (mode == AppMode.BOSS) {
            // Check if a strict schedule is active
            scope.launch {
                // For now, Boss mode just makes the button harder to press
                // Future: fully hide the continue button during strict blocks
            }
        }

        // Open Anyway
        btnOpen.setOnClickListener {
            vibrateLight()
            applyMicroBounce(it)

            // Validate math puzzle answer if active
            if (currentInterventionType == InterventionType.MATH_PUZZLE) {
                val answer = view.findViewById<EditText>(R.id.etMathAnswer)
                    .text.toString().trim().toIntOrNull()
                if (answer != currentMathPuzzle?.answer) {
                    view.findViewById<EditText>(R.id.etMathAnswer).error = "Wrong answer! Try again 🤔"
                    return@setOnClickListener
                }
            }

            // Validate reason if required
            if (requiresReason || currentInterventionType == InterventionType.REASON) {
                val reason = etReason.text.toString().trim()
                if (reason.isEmpty()) {
                    etReason.error = "Please enter a reason 😤"
                    return@setOnClickListener
                }
                scope.launch { rulesEngine.logOpen(packageName, mode, reason) }
            } else {
                scope.launch { rulesEngine.logOpen(packageName, mode) }
            }

            // Show emotion tracking before dismissing
            showEmotionTracking(view, packageName, mode)
        }

        // Go Back
        btnBack.setOnClickListener {
            vibrateLight()
            applyMicroBounce(it)
            scope.launch { rulesEngine.logCancel(packageName, mode) }
            showEmotionTracking(view, packageName, mode, goingBack = true)
        }

        // Snooze
        btnSnooze.setOnClickListener {
            vibrateLight()
            applyMicroBounce(it)
            scope.launch {
                val snoozeMins = preferencesManager.snoozeDurationMinutes.first()
                rulesEngine.logSnooze(packageName, snoozeMins)
            }
            dismissOverlay()
            goHome()
        }
    }

    /**
     * Show emotion tracking chips after the user makes their decision.
     */
    private fun showEmotionTracking(
        view: View,
        packageName: String,
        mode: AppMode,
        goingBack: Boolean = false
    ) {
        currentState = OverlayState.EMOTION

        // Hide buttons and show emotion container
        view.findViewById<LinearLayout>(R.id.buttonsContainer).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.emotionContainer).visibility = View.VISIBLE

        // If going back, show emotion then dismiss
        // If continuing, show emotion -> intention -> dismiss
        val onEmotionSelected: (String) -> Unit = { emotionId ->
            selectedEmotion = emotionId

            if (goingBack) {
                // Save emotion and go home
                scope.launch {
                    rulesEngine.logEmotionIntention(packageName, emotionId, null)
                }
                dismissOverlay()
                goHome()
            } else {
                // Show intention duration picker
                showIntentionPicker(view, packageName)
            }
        }

        // Setup chip click listeners
        setupEmotionChipListener(view, R.id.chipAnxious, "anxious", onEmotionSelected)
        setupEmotionChipListener(view, R.id.chipHappy, "happy", onEmotionSelected)
        setupEmotionChipListener(view, R.id.chipTired, "tired", onEmotionSelected)
        setupEmotionChipListener(view, R.id.chipBored, "bored", onEmotionSelected)
    }

    private fun setupEmotionChipListener(
        view: View,
        chipId: Int,
        emotionId: String,
        onSelected: (String) -> Unit
    ) {
        view.findViewById<TextView>(chipId).setOnClickListener {
            vibrateLight()
            applyMicroBounce(it)
            // Highlight selected chip
            it.setBackgroundColor(context.getColor(R.color.zen_primary_alpha))
            onSelected(emotionId)
        }
    }

    /**
     * Show intention duration picker ("How long do you plan to stay?")
     */
    private fun showIntentionPicker(view: View, packageName: String) {
        view.findViewById<LinearLayout>(R.id.emotionContainer).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.intentionContainer).visibility = View.VISIBLE

        val onIntentionSelected: (Int) -> Unit = { minutes ->
            selectedIntention = minutes
            scope.launch {
                rulesEngine.logEmotionIntention(packageName, selectedEmotion, minutes)
            }
            dismissOverlay()
        }

        view.findViewById<TextView>(R.id.btn5min).setOnClickListener {
            vibrateLight(); applyMicroBounce(it); onIntentionSelected(5)
        }
        view.findViewById<TextView>(R.id.btn15min).setOnClickListener {
            vibrateLight(); applyMicroBounce(it); onIntentionSelected(15)
        }
        view.findViewById<TextView>(R.id.btn30min).setOnClickListener {
            vibrateLight(); applyMicroBounce(it); onIntentionSelected(30)
        }
    }

    private fun setupEmotionChips(view: View) {
        // Initial state: hidden (shown after decision)
        view.findViewById<LinearLayout>(R.id.emotionContainer).visibility = View.GONE
    }

    private fun setupIntentionButtons(view: View) {
        // Initial state: hidden (shown after emotion)
        view.findViewById<LinearLayout>(R.id.intentionContainer).visibility = View.GONE
    }

    private fun startCountdown(
        delaySeconds: Int,
        requiresReason: Boolean,
        params: WindowManager.LayoutParams
    ) {
        val view = overlayView ?: return
        val tvCountdown = view.findViewById<TextView>(R.id.tvCountdown)
        val buttonsContainer = view.findViewById<LinearLayout>(R.id.buttonsContainer)
        val etReason = view.findViewById<EditText>(R.id.etReason)

        // For non-breathing interventions, skip the countdown
        if (currentInterventionType == InterventionType.REFLECTION ||
            currentInterventionType == InterventionType.MATH_PUZZLE) {
            // Show buttons immediately
            currentState = OverlayState.DECISION
            buttonsContainer.visibility = View.VISIBLE
            vibrateLight()
            return
        }

        countdownTimer = object : CountDownTimer(delaySeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt() + 1
                tvCountdown.text = secondsLeft.toString()
            }

            override fun onFinish() {
                currentState = OverlayState.DECISION
                tvCountdown.text = "✓"
                vibrateLight()

                buttonsContainer.visibility = View.VISIBLE

                if (requiresReason || currentInterventionType == InterventionType.REASON) {
                    etReason.visibility = View.VISIBLE
                    try {
                        params.flags = params.flags and
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
                        windowManager.updateViewLayout(overlayView, params)
                    } catch (_: Exception) {}
                }
            }
        }.start()
    }

    fun dismissOverlay() {
        countdownTimer?.cancel()
        countdownTimer = null

        overlayView?.let { view ->
            view.findViewById<BreathingRingView>(R.id.breathingRing)?.stopAnimation()
            try {
                windowManager.removeView(view)
            } catch (_: Exception) {}
        }
        overlayView = null
        isShowing = false
        currentState = OverlayState.DISMISSED
        currentPackage = null
        currentMathPuzzle = null
    }

    private fun goHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(homeIntent)
    }

    private fun createLayoutParams(focusable: Boolean): WindowManager.LayoutParams {
        val flags = if (focusable) {
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        } else {
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        }

        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            flags,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun vibrateLight() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                manager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } catch (_: Exception) {}
    }

    private fun applyMicroBounce(view: View) {
        view.animate()
            .scaleX(0.96f).scaleY(0.96f)
            .setDuration(80)
            .withEndAction {
                view.animate()
                    .scaleX(1f).scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    fun destroy() {
        dismissOverlay()
        scope.cancel()
    }
}

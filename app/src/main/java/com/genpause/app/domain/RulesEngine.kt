package com.genpause.app.domain

import com.genpause.app.data.entity.EventEntity
import com.genpause.app.data.entity.ScheduleEntity
import com.genpause.app.data.entity.TargetAppEntity
import com.genpause.app.data.repository.ZenPauseRepository
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RulesEngine @Inject constructor(
    private val repository: ZenPauseRepository
) {
    companion object {
        private const val ESCALATION_WINDOW_MILLIS = 30 * 60 * 1000L  // 30 minutes
        private const val MIN_DELAY = 2
        private const val MAX_DELAY = 30
    }

    /**
     * Check if we should intercept this package.
     * Returns null if not interceptable, or TargetAppEntity if should intercept.
     */
    suspend fun shouldIntercept(packageName: String, ownPackage: String): TargetAppEntity? {
        // Never intercept ourselves
        if (packageName == ownPackage) return null

        // Ignore system packages
        if (isSystemPackage(packageName)) return null

        val app = repository.getApp(packageName) ?: return null

        // Not enabled
        if (!app.enabled) return null

        // Snoozed
        if (app.snoozedUntilMillis > System.currentTimeMillis()) return null

        return app
    }

    /**
     * Calculate delay for this app, factoring in escalation policy.
     */
    suspend fun getDelay(app: TargetAppEntity): Int {
        val baseDelay = app.baseDelaySeconds
        val policy = try {
            EscalationPolicy.valueOf(app.escalationPolicy)
        } catch (_: Exception) {
            EscalationPolicy.NONE
        }

        if (policy == EscalationPolicy.NONE) return baseDelay

        val since = System.currentTimeMillis() - ESCALATION_WINDOW_MILLIS
        val recentAttempts = repository.countByAppAndAction(
            app.packageName, AppAction.ATTEMPT.name, since
        )

        val escalatedDelay = when (policy) {
            EscalationPolicy.LINEAR -> baseDelay + (recentAttempts * 2)
            EscalationPolicy.MULTIPLIER -> {
                var d = baseDelay
                repeat(recentAttempts.coerceAtMost(5)) { d = (d * 1.5).toInt() }
                d
            }
            else -> baseDelay
        }

        return escalatedDelay.coerceIn(MIN_DELAY, MAX_DELAY)
    }

    /**
     * Check if current mode requires hard mode (reason input).
     */
    fun requiresReason(app: TargetAppEntity, currentMode: AppMode): Boolean {
        if (app.hardModeEnabled) return true
        if (currentMode == AppMode.BOSS) return true
        return false
    }

    /**
     * Get effective mode considering active focus block schedules.
     */
    suspend fun getEffectiveMode(baseMode: AppMode): AppMode {
        val activeSchedule = getActiveSchedule() ?: return baseMode

        val scheduleStrictness = try {
            StrictnessLevel.valueOf(activeSchedule.strictnessLevel)
        } catch (_: Exception) {
            StrictnessLevel.MEDIUM
        }

        return when (scheduleStrictness) {
            StrictnessLevel.SOFT -> if (baseMode.ordinal > AppMode.CHILL.ordinal) baseMode else AppMode.CHILL
            StrictnessLevel.MEDIUM -> if (baseMode.ordinal > AppMode.FOCUS.ordinal) baseMode else AppMode.FOCUS
            StrictnessLevel.STRICT -> AppMode.BOSS
        }
    }

    /**
     * Get currently active focus block schedule, if any.
     */
    suspend fun getActiveSchedule(): ScheduleEntity? {
        val now = Calendar.getInstance()
        val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
        // Convert Calendar.DAY_OF_WEEK (1=Sun..7=Sat) to our bitmask (Mon=1,Tue=2,...,Sun=64)
        val dayBit = when (dayOfWeek) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 4
            Calendar.THURSDAY -> 8
            Calendar.FRIDAY -> 16
            Calendar.SATURDAY -> 32
            Calendar.SUNDAY -> 64
            else -> 0
        }
        val currentMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

        val schedules = repository.getEnabledSchedules()
        return schedules.firstOrNull { schedule ->
            (schedule.daysOfWeekMask and dayBit) != 0 &&
            currentMinutes in schedule.startMinutes..schedule.endMinutes
        }
    }

    /**
     * Log an attempt event.
     */
    suspend fun logAttempt(packageName: String, delaySeconds: Int, mode: AppMode, promptId: String?) {
        repository.logEvent(EventEntity(
            packageName = packageName,
            action = AppAction.ATTEMPT.name,
            delaySeconds = delaySeconds,
            mode = mode.name,
            promptId = promptId
        ))
    }

    suspend fun logOpen(packageName: String, mode: AppMode, reasonText: String? = null) {
        repository.logEvent(EventEntity(
            packageName = packageName,
            action = AppAction.OPEN.name,
            mode = mode.name,
            reasonText = reasonText
        ))
    }

    suspend fun logCancel(packageName: String, mode: AppMode) {
        repository.logEvent(EventEntity(
            packageName = packageName,
            action = AppAction.CANCEL.name,
            mode = mode.name
        ))
    }

    suspend fun logSnooze(packageName: String, snoozeDurationMinutes: Int) {
        val until = System.currentTimeMillis() + (snoozeDurationMinutes * 60 * 1000L)
        repository.snoozeApp(packageName, until)
        repository.logEvent(EventEntity(
            packageName = packageName,
            action = AppAction.SNOOZE.name,
            delaySeconds = snoozeDurationMinutes * 60
        ))
    }

    /**
     * Get today's attempt count for a package (for overlay stat line).
     */
    suspend fun getTodayAttemptCount(packageName: String): Int {
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return repository.countByAppAndAction(packageName, AppAction.ATTEMPT.name, todayStart)
    }

    /**
     * Log emotion and intention data from the overlay.
     * This updates the last OPEN event for this package with emotion/intention data.
     */
    suspend fun logEmotionIntention(packageName: String, emotionId: String?, intentionMinutes: Int?) {
        // We log this as a separate event update rather than modifying the previous event
        // This is simpler and produces a useful audit trail
        repository.logEvent(EventEntity(
            packageName = packageName,
            action = "EMOTION_TRACK",
            emotionId = emotionId,
            intentionDurationMin = intentionMinutes
        ))
    }

    private fun isSystemPackage(packageName: String): Boolean {
        // Exact system packages to always ignore
        val exactIgnore = setOf(
            "android",
            "com.android.systemui",
            "com.android.launcher",
            "com.android.launcher3",
            "com.google.android.apps.nexuslauncher",
            "com.google.android.inputmethod.latin",
            "com.google.android.gms",
            "com.google.android.gsf",
            "com.android.settings",
            "com.android.phone",
            "com.android.dialer",
            "com.android.contacts",
            "com.android.mms"
        )
        if (packageName in exactIgnore) return true

        // Prefix-based system packages (OEM skins)
        val systemPrefixes = listOf(
            "com.android.systemui",
            "com.sec.android.",
            "com.samsung.android.launcher",
            "com.miui.home",
            "com.huawei.android.launcher"
        )
        return systemPrefixes.any { packageName.startsWith(it) }
    }
}

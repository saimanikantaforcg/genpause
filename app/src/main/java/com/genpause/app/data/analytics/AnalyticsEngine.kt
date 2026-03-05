package com.genpause.app.data.analytics

import android.app.usage.UsageStatsManager
import android.content.Context
import com.genpause.app.data.dao.AppAttemptCount
import com.genpause.app.data.dao.HourCount
import com.genpause.app.data.repository.ZenPauseRepository
import com.genpause.app.domain.AppAction
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Analytics engine that provides dashboard data from local Room events
 * and optionally from UsageStatsManager for enhanced insights.
 */
@Singleton
class AnalyticsEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ZenPauseRepository
) {
    private val todayStart: Long
        get() = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    private val weekStart: Long
        get() = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -7)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    // ── Today Stats ──
    suspend fun getTodayAttempts(): Int = repository.countByAction(AppAction.ATTEMPT.name, todayStart)
    suspend fun getTodayCancels(): Int = repository.countByAction(AppAction.CANCEL.name, todayStart)
    suspend fun getTodayOpens(): Int = repository.countByAction(AppAction.OPEN.name, todayStart)

    // ── Week Stats ──
    suspend fun getWeekAttempts(): Int = repository.countByAction(AppAction.ATTEMPT.name, weekStart)
    suspend fun getWeekCancels(): Int = repository.countByAction(AppAction.CANCEL.name, weekStart)
    suspend fun getWeekOpens(): Int = repository.countByAction(AppAction.OPEN.name, weekStart)

    // ── Time Saved Estimate ──
    suspend fun getTimeSavedMinutes(): Int {
        val cancels = getTodayCancels()
        // Estimate: each cancel saves ~5 minutes of scrolling
        return cancels * 5
    }

    suspend fun getWeekTimeSavedMinutes(): Int {
        val cancels = getWeekCancels()
        return cancels * 5
    }

    // ── Top Apps ──
    suspend fun getTopDistractingApps(limit: Int = 5): List<AppAttemptCount> {
        return repository.getTopAttemptedApps(weekStart, limit)
    }

    // ── High Risk Hours ──
    suspend fun getHighRiskHours(limit: Int = 5): List<HourCount> {
        return repository.getHighRiskHours(weekStart, limit)
    }

    // ── Optional: UsageStats integration ──
    fun isUsageAccessGranted(): Boolean {
        return try {
            val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            val end = System.currentTimeMillis()
            val start = end - 60_000
            val stats = usm?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
            stats != null && stats.isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }
}

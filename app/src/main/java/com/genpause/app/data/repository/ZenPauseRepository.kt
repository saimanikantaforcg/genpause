package com.genpause.app.data.repository

import com.genpause.app.data.dao.*
import com.genpause.app.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ZenPauseRepository @Inject constructor(
    private val targetAppDao: TargetAppDao,
    private val eventDao: EventDao,
    private val scheduleDao: ScheduleDao
) {
    // ── Target Apps ──
    fun getAllApps(): Flow<List<TargetAppEntity>> = targetAppDao.getAllApps()
    fun getEnabledApps(): Flow<List<TargetAppEntity>> = targetAppDao.getEnabledApps()
    suspend fun getEnabledAppsList(): List<TargetAppEntity> = targetAppDao.getEnabledAppsList()
    suspend fun getApp(packageName: String): TargetAppEntity? = targetAppDao.getApp(packageName)
    suspend fun insertApp(app: TargetAppEntity) = targetAppDao.insertApp(app)
    suspend fun insertApps(apps: List<TargetAppEntity>) = targetAppDao.insertApps(apps)
    suspend fun updateApp(app: TargetAppEntity) = targetAppDao.updateApp(app)
    suspend fun deleteApp(app: TargetAppEntity) = targetAppDao.deleteApp(app)
    suspend fun snoozeApp(packageName: String, until: Long) = targetAppDao.snoozeApp(packageName, until)
    suspend fun setAppEnabled(packageName: String, enabled: Boolean) = targetAppDao.setEnabled(packageName, enabled)
    suspend fun isTracked(packageName: String): Boolean = targetAppDao.isTracked(packageName) > 0

    // ── Events ──
    suspend fun logEvent(event: EventEntity): Long = eventDao.insertEvent(event)
    fun getEventsSince(since: Long): Flow<List<EventEntity>> = eventDao.getEventsSince(since)
    suspend fun getEventsSinceList(since: Long): List<EventEntity> = eventDao.getEventsSinceList(since)
    suspend fun getEventsForApp(packageName: String, since: Long): List<EventEntity> =
        eventDao.getEventsForApp(packageName, since)
    suspend fun countByAction(action: String, since: Long): Int = eventDao.countByAction(action, since)
    suspend fun countByAppAndAction(packageName: String, action: String, since: Long): Int =
        eventDao.countByAppAndAction(packageName, action, since)
    fun observeAttemptCount(since: Long): Flow<Int> = eventDao.observeAttemptCount(since)
    fun observeCancelCount(since: Long): Flow<Int> = eventDao.observeCancelCount(since)
    fun observeOpenCount(since: Long): Flow<Int> = eventDao.observeOpenCount(since)
    suspend fun getTopAttemptedApps(since: Long, limit: Int = 5): List<AppAttemptCount> =
        eventDao.getTopAttemptedApps(since, limit)
    suspend fun getHighRiskHours(since: Long, limit: Int = 5): List<HourCount> =
        eventDao.getHighRiskHours(since, limit)
    suspend fun getHourlyAttemptCounts(since: Long): List<HourCount> =
        eventDao.getHourlyAttemptCounts(since)

    // ── Schedules ──
    fun getAllSchedules(): Flow<List<ScheduleEntity>> = scheduleDao.getAllSchedules()
    suspend fun getEnabledSchedules(): List<ScheduleEntity> = scheduleDao.getEnabledSchedules()
    suspend fun getSchedule(id: Long): ScheduleEntity? = scheduleDao.getSchedule(id)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long = scheduleDao.insertSchedule(schedule)
    suspend fun updateSchedule(schedule: ScheduleEntity) = scheduleDao.updateSchedule(schedule)
    suspend fun deleteSchedule(id: Long) = scheduleDao.deleteById(id)
}

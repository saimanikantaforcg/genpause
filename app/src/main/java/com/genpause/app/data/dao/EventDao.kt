package com.genpause.app.data.dao

import androidx.room.*
import com.genpause.app.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert
    suspend fun insertEvent(event: EventEntity): Long

    @Query("SELECT * FROM events WHERE timestamp >= :since ORDER BY timestamp DESC")
    fun getEventsSince(since: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE timestamp >= :since ORDER BY timestamp DESC")
    suspend fun getEventsSinceList(since: Long): List<EventEntity>

    @Query("SELECT * FROM events WHERE packageName = :packageName AND timestamp >= :since ORDER BY timestamp DESC")
    suspend fun getEventsForApp(packageName: String, since: Long): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events WHERE action = :action AND timestamp >= :since")
    suspend fun countByAction(action: String, since: Long): Int

    @Query("SELECT COUNT(*) FROM events WHERE packageName = :packageName AND action = :action AND timestamp >= :since")
    suspend fun countByAppAndAction(packageName: String, action: String, since: Long): Int

    @Query("SELECT COUNT(*) FROM events WHERE action = 'ATTEMPT' AND timestamp >= :since")
    fun observeAttemptCount(since: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM events WHERE action = 'CANCEL' AND timestamp >= :since")
    fun observeCancelCount(since: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM events WHERE action = 'OPEN' AND timestamp >= :since")
    fun observeOpenCount(since: Long): Flow<Int>

    @Query("""
        SELECT packageName, COUNT(*) as cnt FROM events 
        WHERE action = 'ATTEMPT' AND timestamp >= :since
        GROUP BY packageName ORDER BY cnt DESC LIMIT :limit
    """)
    suspend fun getTopAttemptedApps(since: Long, limit: Int = 5): List<AppAttemptCount>

    @Query("""
        SELECT (timestamp / 3600000) % 24 as hour, COUNT(*) as cnt FROM events 
        WHERE action = 'ATTEMPT' AND timestamp >= :since
        GROUP BY hour ORDER BY cnt DESC LIMIT :limit
    """)
    suspend fun getHighRiskHours(since: Long, limit: Int = 5): List<HourCount>

    @Query("""
        SELECT (timestamp / 3600000) % 24 as hour, COUNT(*) as cnt FROM events 
        WHERE action = 'ATTEMPT' AND timestamp >= :since
        GROUP BY hour ORDER BY hour ASC
    """)
    suspend fun getHourlyAttemptCounts(since: Long): List<HourCount>

    @Query("DELETE FROM events WHERE timestamp < :before")
    suspend fun deleteOlderThan(before: Long)
}

data class AppAttemptCount(
    val packageName: String,
    val cnt: Int
)

data class HourCount(
    val hour: Int,
    val cnt: Int
)

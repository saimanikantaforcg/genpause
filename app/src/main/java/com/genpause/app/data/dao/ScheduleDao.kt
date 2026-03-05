package com.genpause.app.data.dao

import androidx.room.*
import com.genpause.app.data.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedules ORDER BY startMinutes ASC")
    fun getAllSchedules(): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE enabled = 1")
    suspend fun getEnabledSchedules(): List<ScheduleEntity>

    @Query("SELECT * FROM schedules WHERE id = :id")
    suspend fun getSchedule(id: Long): ScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Update
    suspend fun updateSchedule(schedule: ScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: ScheduleEntity)

    @Query("DELETE FROM schedules WHERE id = :id")
    suspend fun deleteById(id: Long)
}

package com.genpause.app.data.dao

import androidx.room.*
import com.genpause.app.data.entity.TargetAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TargetAppDao {

    @Query("SELECT * FROM target_apps ORDER BY displayName ASC")
    fun getAllApps(): Flow<List<TargetAppEntity>>

    @Query("SELECT * FROM target_apps WHERE enabled = 1")
    fun getEnabledApps(): Flow<List<TargetAppEntity>>

    @Query("SELECT * FROM target_apps WHERE enabled = 1")
    suspend fun getEnabledAppsList(): List<TargetAppEntity>

    @Query("SELECT * FROM target_apps WHERE packageName = :packageName")
    suspend fun getApp(packageName: String): TargetAppEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: TargetAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<TargetAppEntity>)

    @Update
    suspend fun updateApp(app: TargetAppEntity)

    @Delete
    suspend fun deleteApp(app: TargetAppEntity)

    @Query("DELETE FROM target_apps WHERE packageName = :packageName")
    suspend fun deleteByPackage(packageName: String)

    @Query("UPDATE target_apps SET snoozedUntilMillis = :until WHERE packageName = :packageName")
    suspend fun snoozeApp(packageName: String, until: Long)

    @Query("UPDATE target_apps SET enabled = :enabled WHERE packageName = :packageName")
    suspend fun setEnabled(packageName: String, enabled: Boolean)

    @Query("SELECT COUNT(*) FROM target_apps WHERE packageName = :packageName AND enabled = 1")
    suspend fun isTracked(packageName: String): Int
}

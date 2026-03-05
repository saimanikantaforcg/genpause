package com.genpause.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.genpause.app.data.dao.EventDao
import com.genpause.app.data.dao.ScheduleDao
import com.genpause.app.data.dao.TargetAppDao
import com.genpause.app.data.entity.EventEntity
import com.genpause.app.data.entity.ScheduleEntity
import com.genpause.app.data.entity.TargetAppEntity

@Database(
    entities = [
        TargetAppEntity::class,
        EventEntity::class,
        ScheduleEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class ZenPauseDatabase : RoomDatabase() {
    abstract fun targetAppDao(): TargetAppDao
    abstract fun eventDao(): EventDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        /**
         * v1 → v2: add re-intervention columns to target_apps.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE target_apps ADD COLUMN reInterventionEnabled INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE target_apps ADD COLUMN reInterventionIntervalMin INTEGER NOT NULL DEFAULT 10"
                )
            }
        }

        /**
         * v2 → v3: add emotion/intention tracking columns to events.
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE events ADD COLUMN emotionId TEXT DEFAULT NULL"
                )
                db.execSQL(
                    "ALTER TABLE events ADD COLUMN intentionDurationMin INTEGER DEFAULT NULL"
                )
            }
        }
    }
}

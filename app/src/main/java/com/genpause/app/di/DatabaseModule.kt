package com.genpause.app.di

import android.content.Context
import androidx.room.Room
import com.genpause.app.data.dao.EventDao
import com.genpause.app.data.dao.ScheduleDao
import com.genpause.app.data.dao.TargetAppDao
import com.genpause.app.data.db.ZenPauseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ZenPauseDatabase {
        return Room.databaseBuilder(
            context,
            ZenPauseDatabase::class.java,
            "zenpause_db"
        ).addMigrations(
            ZenPauseDatabase.MIGRATION_1_2,
            ZenPauseDatabase.MIGRATION_2_3
        ).build()
    }

    @Provides
    fun provideTargetAppDao(db: ZenPauseDatabase): TargetAppDao = db.targetAppDao()

    @Provides
    fun provideEventDao(db: ZenPauseDatabase): EventDao = db.eventDao()

    @Provides
    fun provideScheduleDao(db: ZenPauseDatabase): ScheduleDao = db.scheduleDao()
}

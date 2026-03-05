package com.genpause.app.di

import android.content.Context
import com.genpause.app.data.analytics.AnalyticsEngine
import com.genpause.app.data.preferences.PreferencesManager
import com.genpause.app.data.repository.ZenPauseRepository
import com.genpause.app.domain.RulesEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideRulesEngine(repository: ZenPauseRepository): RulesEngine {
        return RulesEngine(repository)
    }

    @Provides
    @Singleton
    fun provideAnalyticsEngine(
        @ApplicationContext context: Context,
        repository: ZenPauseRepository
    ): AnalyticsEngine {
        return AnalyticsEngine(context, repository)
    }
}

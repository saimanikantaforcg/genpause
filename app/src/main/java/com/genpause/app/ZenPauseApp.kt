package com.genpause.app

import android.app.Application
import com.genpause.app.data.preferences.PreferencesManager
import com.genpause.app.domain.RulesEngine
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ZenPauseApp : Application() {

    /**
     * Exposed for AccessibilityService which can't use Hilt constructor injection.
     * The service accesses these via (application as ZenPauseApp).rulesEngine etc.
     */
    @Inject lateinit var rulesEngine: RulesEngine
    @Inject lateinit var preferencesManager: PreferencesManager
}

package com.genpause.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.genpause.app.data.entity.TargetAppEntity
import com.genpause.app.data.preferences.PreferencesManager
import com.genpause.app.data.repository.ZenPauseRepository
import com.genpause.app.ui.screens.*
import kotlinx.coroutines.launch

object Routes {
    const val WELCOME = "welcome"
    const val DISCLOSURE = "disclosure"
    const val PERMISSIONS = "permissions"
    const val QUIZ = "quiz"
    const val BATTERY_GUIDE = "battery_guide"
    const val APP_PICKER = "app_picker"
    const val DASHBOARD = "dashboard"
    const val MANAGED_APPS = "managed_apps"
    const val FOCUS_BLOCKS = "focus_blocks"
    const val PROMPT_SETTINGS = "prompt_settings"
    const val SETTINGS = "settings"
    const val APP_SETTINGS = "app_settings/{packageName}"

    fun appSettings(packageName: String) = "app_settings/$packageName"
}

@Composable
fun GenPauseNavGraph(
    navController: NavHostController,
    startDestination: String,
    repository: ZenPauseRepository,
    preferencesManager: PreferencesManager
) {
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ── Onboarding ──
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Routes.DISCLOSURE)
                }
            )
        }

        composable(Routes.DISCLOSURE) {
            DisclosureScreen(
                onAgree = {
                    scope.launch {
                        preferencesManager.recordConsent(version = 1)
                    }
                    navController.navigate(Routes.PERMISSIONS)
                },
                onNotNow = {
                    // Stay on disclosure screen - user can come back later
                }
            )
        }

        composable(Routes.PERMISSIONS) {
            PermissionScreen(
                onAllGranted = {
                    navController.navigate(Routes.QUIZ)
                }
            )
        }

        composable(Routes.QUIZ) {
            QuizScreen(
                onComplete = { motivation ->
                    scope.launch {
                        preferencesManager.setPrimaryMotivation(motivation)
                    }
                    navController.navigate(Routes.BATTERY_GUIDE)
                }
            )
        }

        composable(Routes.BATTERY_GUIDE) {
            BatteryGuideScreen(
                onContinue = {
                    navController.navigate(Routes.APP_PICKER)
                }
            )
        }

        composable(Routes.APP_PICKER) {
            AppPickerScreen(
                onDone = { selectedApps ->
                    scope.launch {
                        repository.insertApps(selectedApps)
                        preferencesManager.setOnboardingComplete(true)
                    }
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        // ── Main App ──
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToApps = {
                    navController.navigate(Routes.MANAGED_APPS)
                },
                onNavigateToFocus = {
                    navController.navigate(Routes.FOCUS_BLOCKS)
                },
                onNavigateToPrompts = {
                    navController.navigate(Routes.PROMPT_SETTINGS)
                }
            )
        }

        composable(Routes.MANAGED_APPS) {
            ManagedAppsScreen(
                onNavigateToAppSettings = { pkg ->
                    navController.navigate(Routes.appSettings(pkg))
                },
                onAddApps = {
                    navController.navigate(Routes.APP_PICKER)
                }
            )
        }

        composable(Routes.FOCUS_BLOCKS) {
            FocusBlocksScreen()
        }

        composable(Routes.PROMPT_SETTINGS) {
            PromptSettingsScreen()
        }

        composable(Routes.SETTINGS) {
            SettingsScreen()
        }

        composable(
            route = Routes.APP_SETTINGS,
            arguments = listOf(navArgument("packageName") { type = NavType.StringType })
        ) { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
            AppSettingsScreen(
                packageName = packageName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

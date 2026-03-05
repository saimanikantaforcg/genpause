package com.genpause.app.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.genpause.app.data.preferences.PreferencesManager
import com.genpause.app.data.repository.ZenPauseRepository
import com.genpause.app.service.OverlayForegroundService
import com.genpause.app.ui.navigation.Routes
import com.genpause.app.ui.navigation.GenPauseNavGraph
import com.genpause.app.ui.theme.GenPauseTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var preferencesManager: PreferencesManager
    @Inject lateinit var repository: ZenPauseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onboardingComplete = runBlocking { preferencesManager.onboardingComplete.first() }
        val startDestination = if (onboardingComplete) Routes.DASHBOARD else Routes.WELCOME

        if (onboardingComplete) {
            startOverlayService()
        }

        setContent {
            GenPauseTheme {
                val navController = rememberNavController()

                // Show bottom nav only in main app (after onboarding)
                Scaffold(
                    bottomBar = {
                        if (onboardingComplete) {
                            ZenBottomNavBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        GenPauseNavGraph(
                            navController = navController,
                            startDestination = startDestination,
                            repository = repository,
                            preferencesManager = preferencesManager
                        )
                    }
                }
            }
        }
    }

    private fun startOverlayService() {
        try {
            val intent = Intent(this, OverlayForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        } catch (_: Exception) {
            // Service might already be running
        }
    }
}

// Bottom tab descriptor
private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
private fun ZenBottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(Routes.DASHBOARD, "Stats", Icons.Outlined.BarChart),
        BottomNavItem(Routes.MANAGED_APPS, "Apps", Icons.Outlined.Shield),
        BottomNavItem(Routes.FOCUS_BLOCKS, "Focus", Icons.Outlined.Lock),
        BottomNavItem(Routes.SETTINGS, "Settings", Icons.Outlined.Settings)
    )

    // Routes where bottom bar should NOT show (onboarding flow)
    val onboardingRoutes = setOf(
        Routes.WELCOME, Routes.DISCLOSURE, Routes.PERMISSIONS,
        Routes.QUIZ, Routes.BATTERY_GUIDE, Routes.APP_PICKER
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide during onboarding
    if (currentRoute in onboardingRoutes || currentRoute?.startsWith("app_settings/") == true) return

    NavigationBar(
        containerColor = com.genpause.app.ui.theme.ZenSurface,
        tonalElevation = androidx.compose.ui.unit.Dp.Unspecified
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = com.genpause.app.ui.theme.ZenPrimary,
                    selectedTextColor = com.genpause.app.ui.theme.ZenPrimary,
                    unselectedIconColor = com.genpause.app.ui.theme.ZenTextSecondary,
                    unselectedTextColor = com.genpause.app.ui.theme.ZenTextSecondary,
                    indicatorColor = com.genpause.app.ui.theme.ZenPrimary.copy(alpha = 0.12f)
                )
            )
        }
    }
}

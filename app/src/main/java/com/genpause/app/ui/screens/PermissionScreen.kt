package com.genpause.app.ui.screens

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.genpause.app.ui.components.GradientButton
import com.genpause.app.ui.components.PermissionRow
import com.genpause.app.ui.theme.*

@Composable
fun PermissionScreen(
    onAllGranted: () -> Unit
) {
    val context = LocalContext.current

    var accessibilityGranted by remember { mutableStateOf(isAccessibilityEnabled(context)) }
    var overlayGranted by remember { mutableStateOf(Settings.canDrawOverlays(context)) }
    var usageGranted by remember { mutableStateOf(isUsageAccessGranted(context)) }

    // Refresh on resume
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        accessibilityGranted = isAccessibilityEnabled(context)
        overlayGranted = Settings.canDrawOverlays(context)
        usageGranted = isUsageAccessGranted(context)
    }

    val allRequired = accessibilityGranted && overlayGranted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Permissions Setup 🔧",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Almost there!",
            color = ZenTextSecondary,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Accessibility
        PermissionRow(
            title = "Accessibility Service",
            description = "Detect app opens",
            isGranted = accessibilityGranted,
            onEnable = {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            },
            icon = Icons.Outlined.Accessibility
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Overlay
        PermissionRow(
            title = "Display Over Apps",
            description = "Show pause overlay",
            isGranted = overlayGranted,
            onEnable = {
                try {
                    // Using generic intent without package URI. 
                    // Some devices/emulators crash the Settings app when a package URI is provided.
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                } catch (e: Exception) {
                    try {
                        val fallback = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        fallback.data = Uri.parse("package:${context.packageName}")
                        fallback.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(fallback)
                    } catch (_: Exception) { }
                }
            },
            icon = Icons.Outlined.Layers
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Usage access (optional)
        PermissionRow(
            title = "Usage Access",
            description = "Better stats (optional)",
            isGranted = usageGranted,
            onEnable = {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            },
            icon = Icons.Outlined.BarChart
        )

        Spacer(modifier = Modifier.weight(1f))

        // Continue button
        GradientButton(
            text = if (allRequired) "All set, let's go →" else "Required permissions pending",
            onClick = onAllGranted,
            enabled = allRequired,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

private fun isAccessibilityEnabled(context: Context): Boolean {
    val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
    return enabledServices.any {
        it.resolveInfo.serviceInfo.packageName == context.packageName
    }
}

private fun isUsageAccessGranted(context: Context): Boolean {
    return try {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        }
        mode == android.app.AppOpsManager.MODE_ALLOWED
    } catch (_: Exception) {
        false
    }
}

package com.genpause.app.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BatteryAlert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genpause.app.ui.components.GradientButton
import com.genpause.app.ui.theme.*

private data class VendorGuide(
    val name: String,
    val steps: List<String>,
    val intentAction: String? = null
)

private fun getVendorGuide(): VendorGuide {
    val manufacturer = Build.MANUFACTURER.lowercase()
    return when {
        manufacturer.contains("samsung") -> VendorGuide(
            name = "Samsung One UI",
            steps = listOf(
                "Open Settings → Device Care → Battery",
                "Tap 'Background usage limits'",
                "Under 'Never sleeping apps', tap '+' and add GenPause",
                "Go back to Battery → disable 'Optimize battery usage' for GenPause"
            )
        )
        manufacturer.contains("xiaomi") || manufacturer.contains("redmi") || manufacturer.contains("poco") -> VendorGuide(
            name = "Xiaomi MIUI / HyperOS",
            steps = listOf(
                "Open Settings → Apps → Manage Apps → GenPause",
                "Tap 'Battery Saver' → select 'No Restrictions'",
                "Enable 'Autostart' for GenPause",
                "In Security app → Battery → App battery saver → GenPause → No restrictions"
            )
        )
        manufacturer.contains("huawei") || manufacturer.contains("honor") -> VendorGuide(
            name = "Huawei EMUI / HarmonyOS",
            steps = listOf(
                "Open Settings → Apps → Apps → GenPause",
                "Tap 'App launch' → toggle to 'Manage manually'",
                "Enable 'Auto-launch', 'Secondary launch', and 'Run in background'",
                "Also check Settings → Battery → App launch"
            )
        )
        manufacturer.contains("oppo") || manufacturer.contains("realme") || manufacturer.contains("oneplus") -> VendorGuide(
            name = "OPPO / Realme / OnePlus",
            steps = listOf(
                "Open Settings → Battery → More battery settings",
                "Disable 'Optimize battery usage' for GenPause",
                "Also enable 'Auto-launch' in App Management settings"
            )
        )
        else -> VendorGuide(
            name = "Stock Android",
            steps = listOf(
                "Open Settings → Apps → GenPause → Battery",
                "Select 'Unrestricted' battery usage",
                "This prevents Android from killing GenPause in the background"
            ),
            intentAction = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
        )
    }
}

@Composable
fun BatteryGuideScreen(
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    val guide = remember { getVendorGuide() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Icon(
            imageVector = Icons.Outlined.BatteryAlert,
            contentDescription = null,
            tint = ZenPrimary,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Keep GenPause Alive 🔋",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your phone may kill GenPause in the background.\nFollow these steps to prevent that.",
            color = ZenTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Vendor name badge
        Surface(
            color = ZenPrimary.copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "Detected: ${guide.name}",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = ZenPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }

        // Steps
        guide.steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ZenElevatedSurface)
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${index + 1}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZenPrimary,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ZenPrimary.copy(alpha = 0.15f))
                        .wrapContentSize(Alignment.Center)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = step,
                    fontSize = 14.sp,
                    color = ZenTextPrimary,
                    lineHeight = 20.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Open battery settings button
        OutlinedButton(
            onClick = {
                try {
                    val intent = if (guide.intentAction != null) {
                        Intent(guide.intentAction)
                    } else {
                        Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                    }
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                } catch (_: Exception) {
                    try {
                        val fallback = Intent(Settings.ACTION_SETTINGS)
                        context.startActivity(fallback)
                    } catch (_: Exception) { }
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ZenPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Battery Settings")
        }

        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(
            text = "Continue →",
            onClick = onContinue,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

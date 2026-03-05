package com.genpause.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genpause.app.ui.components.GradientButton
import com.genpause.app.ui.components.NeonButton
import com.genpause.app.ui.components.ZenCard
import com.genpause.app.ui.theme.*

/**
 * Prominent Disclosure + Consent screen.
 * MUST be shown BEFORE redirecting user to Accessibility settings.
 * Required for Google Play policy compliance.
 */
@Composable
fun DisclosureScreen(
    onAgree: () -> Unit,
    onNotNow: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Icon
        Icon(
            imageVector = Icons.Outlined.Shield,
            contentDescription = null,
            tint = ZenPrimary,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "One moment 🙏",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Permission Check",
            fontSize = 16.sp,
            color = ZenTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // What ZenPause does
        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "What GenPause does",
                fontWeight = FontWeight.SemiBold,
                color = ZenPrimary,
                fontSize = 16.sp
            )
            Text(
                text = "GenPause uses Android's Accessibility Service to detect when you open a selected distracting app and show a pause overlay.",
                color = ZenTextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // What we DO
        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "✅ What we DO",
                fontWeight = FontWeight.SemiBold,
                color = ZenAccent,
                fontSize = 16.sp
            )
            Text(
                text = "• Detect which app you opened (package name only)\n• Show our pause overlay for mindfulness\n• Log GenPause actions (attempt/open/cancel/snooze)\n• All data stays on your device",
                color = ZenTextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // What we DON'T do
        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "❌ What we DON'T do",
                fontWeight = FontWeight.SemiBold,
                color = ZenError,
                fontSize = 16.sp
            )
            Text(
                text = "• We do NOT read your messages\n• We do NOT see typed text or passwords\n• We do NOT capture your screen\n• We do NOT collect personal data\n• We do NOT send data to servers",
                color = ZenTextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Privacy note
        Text(
            text = "Your data stays on your device. Always. 🔒",
            color = ZenTextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Agree button
        GradientButton(
            text = "I Agree & Continue",
            onClick = onAgree
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Not now
        NeonButton(
            text = "Not Now",
            onClick = onNotNow,
            color = ZenTextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

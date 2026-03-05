package com.genpause.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genpause.app.ui.components.GradientButton
import com.genpause.app.ui.theme.*

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit
) {
    // Breathing ring animation for the logo circle
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Animated breathing ring/logo
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.scale(breathScale)
        ) {
            // Outer glow
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .alpha(glowAlpha)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                ZenPrimary.copy(alpha = 0.3f),
                                ZenSecondary.copy(alpha = 0.1f),
                                ZenBackground
                            )
                        ),
                        shape = CircleShape
                    )
            )
            // Inner circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(ZenPrimary, ZenSecondary)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🧘",
                    fontSize = 40.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // App name
        Text(
            text = "GenPause",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tagline
        Text(
            text = "Take a breath. Take control. 🧘",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = ZenPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Description
        Text(
            text = "The antidote to mindless scrolling.\nWe add a tiny pause before your distracting apps open — that's it.",
            fontSize = 15.sp,
            color = ZenTextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // CTA
        GradientButton(
            text = "Get Started 🚀",
            onClick = onGetStarted,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

private val EaseInOutSine: Easing = CubicBezierEasing(0.37f, 0f, 0.63f, 1f)

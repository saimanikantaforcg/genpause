package com.genpause.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genpause.app.ui.components.GradientButton
import com.genpause.app.ui.theme.*

data class MotivationOption(
    val id: String,
    val emoji: String,
    val label: String,
    val description: String
)

private val motivationOptions = listOf(
    MotivationOption("boredom", "😐", "Boredom", "I open apps when I have nothing to do"),
    MotivationOption("escapism", "🏃", "Escapism", "I scroll to avoid stress or responsibilities"),
    MotivationOption("fomo", "😰", "FOMO", "I'm afraid of missing out on updates"),
    MotivationOption("networking", "💼", "Work / Networking", "I use apps for professional reasons"),
    MotivationOption("habit", "🔄", "Pure Habit", "My fingers just open apps automatically")
)

@Composable
fun QuizScreen(
    onComplete: (String) -> Unit
) {
    var selectedMotivation by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "What drives your scrolling? 🧠",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This helps us personalize your experience",
            color = ZenTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        motivationOptions.forEach { option ->
            val isSelected = selectedMotivation == option.id
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) ZenPrimary else ZenElevatedSurface,
                label = "border"
            )
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) ZenPrimary.copy(alpha = 0.15f) else ZenElevatedSurface,
                label = "bg"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(bgColor)
                    .clickable { selectedMotivation = option.id }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.emoji,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = option.label,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ZenTextPrimary
                    )
                    Text(
                        text = option.description,
                        fontSize = 13.sp,
                        color = ZenTextSecondary
                    )
                }
                if (isSelected) {
                    Text(text = "✓", color = ZenPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GradientButton(
            text = "Continue →",
            onClick = { selectedMotivation?.let { onComplete(it) } },
            enabled = selectedMotivation != null,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

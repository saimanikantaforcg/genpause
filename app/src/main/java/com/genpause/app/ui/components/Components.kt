package com.genpause.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genpause.app.ui.theme.*

/**
 * Card with ZenPause dark neon styling.
 */
@Composable
fun ZenCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ZenSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

/**
 * Neon-outlined button.
 */
@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = ZenPrimary,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "bounce"
    )

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale),
        enabled = enabled,
        shape = RoundedCornerShape(24.dp),
        border = ButtonDefaults.outlinedButtonBorder(enabled).copy(
            brush = Brush.linearGradient(listOf(color, color.copy(alpha = 0.6f)))
        ),
        interactionSource = interactionSource,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color,
            disabledContentColor = color.copy(alpha = 0.4f)
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Gradient-filled button (cyan to purple).
 */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "bounce"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = if (enabled) {
                    Brush.horizontalGradient(listOf(ZenPrimary, ZenSecondary))
                } else {
                    Brush.horizontalGradient(
                        listOf(ZenPrimary.copy(alpha = 0.3f), ZenSecondary.copy(alpha = 0.3f))
                    )
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) ZenBackground else ZenTextSecondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Stat card showing a value and label.
 */
@Composable
fun StatCard(
    value: String,
    label: String,
    accentColor: Color = ZenPrimary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ZenSurface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                color = accentColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                color = ZenTextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Permission status row.
 */
@Composable
fun PermissionRow(
    title: String,
    description: String,
    isGranted: Boolean,
    onEnable: () -> Unit,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    ZenCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isGranted) ZenAccent else ZenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ZenTextSecondary
                )
            }

            if (isGranted) {
                Text(
                    text = "✅",
                    fontSize = 20.sp
                )
            } else {
                TextButton(onClick = onEnable) {
                    Text(
                        text = "Enable",
                        color = ZenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Section header with optional accent bar.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(20.dp)
                .background(
                    brush = Brush.verticalGradient(listOf(ZenPrimary, ZenSecondary)),
                    shape = RoundedCornerShape(2.dp)
                )
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = ZenTextPrimary
        )
    }
}

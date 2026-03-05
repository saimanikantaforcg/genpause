package com.genpause.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genpause.app.data.analytics.AnalyticsEngine
import com.genpause.app.data.dao.AppAttemptCount
import com.genpause.app.data.dao.HourCount
import com.genpause.app.ui.components.*
import com.genpause.app.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val analyticsEngine: AnalyticsEngine
) : ViewModel() {

    var todayAttempts by mutableIntStateOf(0); private set
    var todayCancels by mutableIntStateOf(0); private set
    var todayOpens by mutableIntStateOf(0); private set
    var weekAttempts by mutableIntStateOf(0); private set
    var weekCancels by mutableIntStateOf(0); private set
    var weekOpens by mutableIntStateOf(0); private set
    var timeSaved by mutableIntStateOf(0); private set
    var weekTimeSaved by mutableIntStateOf(0); private set
    var topApps by mutableStateOf<List<AppAttemptCount>>(emptyList()); private set
    var highRiskHours by mutableStateOf<List<HourCount>>(emptyList()); private set
    var isToday by mutableStateOf(true); private set

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            todayAttempts = analyticsEngine.getTodayAttempts()
            todayCancels = analyticsEngine.getTodayCancels()
            todayOpens = analyticsEngine.getTodayOpens()
            weekAttempts = analyticsEngine.getWeekAttempts()
            weekCancels = analyticsEngine.getWeekCancels()
            weekOpens = analyticsEngine.getWeekOpens()
            timeSaved = analyticsEngine.getTimeSavedMinutes()
            weekTimeSaved = analyticsEngine.getWeekTimeSavedMinutes()
            topApps = analyticsEngine.getTopDistractingApps()
            highRiskHours = analyticsEngine.getHighRiskHours()
        }
    }

    fun toggleTimePeriod() {
        isToday = !isToday
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToApps: () -> Unit,
    onNavigateToFocus: () -> Unit,
    onNavigateToPrompts: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val isToday = viewModel.isToday

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Your Progress 📊",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Period toggle
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = isToday,
                onClick = { if (!isToday) viewModel.toggleTimePeriod() },
                label = { Text("Today") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ZenPrimary.copy(alpha = 0.2f),
                    selectedLabelColor = ZenPrimary
                )
            )
            FilterChip(
                selected = !isToday,
                onClick = { if (isToday) viewModel.toggleTimePeriod() },
                label = { Text("7 Days") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ZenPrimary.copy(alpha = 0.2f),
                    selectedLabelColor = ZenPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stat cards grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                value = if (isToday) viewModel.todayAttempts.toString() else viewModel.weekAttempts.toString(),
                label = "Attempts",
                accentColor = ZenPrimary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = if (isToday) viewModel.todayCancels.toString() else viewModel.weekCancels.toString(),
                label = "Cancelled",
                accentColor = ZenAccent,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = if (isToday) viewModel.todayOpens.toString() else viewModel.weekOpens.toString(),
                label = "Opened",
                accentColor = ZenError,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Time saved hero
        TimeSavedHeroCard(
            timeSavedMinutes = if (isToday) viewModel.timeSaved else viewModel.weekTimeSaved,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Attempt frequency chart (hourly)
        AttemptBarChart(
            hourlyCounts = viewModel.highRiskHours.map { it.hour to it.cnt },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Top distracting apps
        if (viewModel.topApps.isNotEmpty()) {
            // App distribution donut chart
            AppDistributionDonut(
                appCounts = viewModel.topApps.map {
                    it.packageName.substringAfterLast(".") to it.cnt
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = "Top Distracting Apps")
            Spacer(modifier = Modifier.height(8.dp))
            ZenCard(modifier = Modifier.fillMaxWidth()) {
                viewModel.topApps.forEachIndexed { index, app ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}. ${app.packageName.substringAfterLast(".")}",
                            color = ZenTextPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${app.cnt} times",
                            color = ZenPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // High risk hours
        if (viewModel.highRiskHours.isNotEmpty()) {
            SectionHeader(title = "High-Risk Hours")
            Spacer(modifier = Modifier.height(8.dp))
            ZenCard(modifier = Modifier.fillMaxWidth()) {
                viewModel.highRiskHours.forEach { hourCount ->
                    val hourLabel = when {
                        hourCount.hour == 0 -> "12 AM"
                        hourCount.hour < 12 -> "${hourCount.hour} AM"
                        hourCount.hour == 12 -> "12 PM"
                        else -> "${hourCount.hour - 12} PM"
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = hourLabel, color = ZenTextPrimary, fontSize = 14.sp)
                        Text(
                            text = "${hourCount.cnt} attempts",
                            color = ZenError,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick actions
        SectionHeader(title = "Quick Actions")
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                emoji = "🎯",
                label = "Manage Apps",
                onClick = onNavigateToApps,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                emoji = "🔒",
                label = "Boss Mode",
                onClick = onNavigateToFocus,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                emoji = "✨",
                label = "Prompts",
                onClick = onNavigateToPrompts,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun QuickActionCard(
    emoji: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ZenElevatedSurface),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Text(
                text = label,
                color = ZenTextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

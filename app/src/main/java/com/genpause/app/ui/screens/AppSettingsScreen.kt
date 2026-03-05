package com.genpause.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genpause.app.data.entity.TargetAppEntity
import com.genpause.app.data.repository.ZenPauseRepository
import com.genpause.app.domain.EscalationPolicy
import com.genpause.app.domain.InterventionType
import com.genpause.app.ui.components.*
import com.genpause.app.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val repository: ZenPauseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val packageName: String = savedStateHandle["packageName"] ?: ""

    var app by mutableStateOf<TargetAppEntity?>(null); private set
    var delaySeconds by mutableIntStateOf(5); private set
    var hardModeEnabled by mutableStateOf(false); private set
    var escalationPolicy by mutableStateOf(EscalationPolicy.NONE); private set
    var interventionType by mutableStateOf(InterventionType.BREATH); private set
    var enabled by mutableStateOf(true); private set
    var reInterventionEnabled by mutableStateOf(false); private set
    var reInterventionIntervalMin by mutableIntStateOf(10); private set

    init {
        viewModelScope.launch {
            repository.getApp(packageName)?.let { entity ->
                app = entity
                delaySeconds = entity.baseDelaySeconds
                hardModeEnabled = entity.hardModeEnabled
                enabled = entity.enabled
                escalationPolicy = try {
                    EscalationPolicy.valueOf(entity.escalationPolicy)
                } catch (_: Exception) { EscalationPolicy.NONE }
                interventionType = try {
                    InterventionType.valueOf(entity.interventionType)
                } catch (_: Exception) { InterventionType.BREATH }
                reInterventionEnabled = entity.reInterventionEnabled
                reInterventionIntervalMin = entity.reInterventionIntervalMin
            }
        }
    }

    fun updateDelay(seconds: Int) {
        delaySeconds = seconds
        saveApp()
    }

    fun toggleHardMode(value: Boolean) {
        hardModeEnabled = value
        saveApp()
    }

    fun updateEscalation(policy: EscalationPolicy) {
        escalationPolicy = policy
        saveApp()
    }

    fun toggleReIntervention(value: Boolean) {
        reInterventionEnabled = value
        saveApp()
    }

    fun updateReInterventionInterval(minutes: Int) {
        reInterventionIntervalMin = minutes
        saveApp()
    }

    fun toggleEnabled(value: Boolean) {
        enabled = value
        saveApp()
    }

    private fun saveApp() {
        val current = app ?: return
        val updated = current.copy(
            baseDelaySeconds = delaySeconds,
            hardModeEnabled = hardModeEnabled,
            escalationPolicy = escalationPolicy.name,
            interventionType = interventionType.name,
            enabled = enabled,
            reInterventionEnabled = reInterventionEnabled,
            reInterventionIntervalMin = reInterventionIntervalMin
        )
        app = updated
        viewModelScope.launch { repository.updateApp(updated) }
    }
}

@Composable
fun AppSettingsScreen(
    packageName: String,
    onBack: () -> Unit,
    viewModel: AppSettingsViewModel = hiltViewModel()
) {
    val app = viewModel.app

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = app?.displayName ?: packageName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = packageName,
            color = ZenTextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Enabled toggle
        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Monitoring Active",
                    color = ZenTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = viewModel.enabled,
                    onCheckedChange = { viewModel.toggleEnabled(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ZenPrimary,
                        checkedTrackColor = ZenPrimary.copy(alpha = 0.3f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delay
        SectionHeader(title = "Countdown Delay")
        Spacer(modifier = Modifier.height(8.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "${viewModel.delaySeconds} seconds",
                color = ZenPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Slider(
                value = viewModel.delaySeconds.toFloat(),
                onValueChange = { viewModel.updateDelay(it.toInt()) },
                valueRange = 1f..15f,
                steps = 13,
                colors = SliderDefaults.colors(
                    thumbColor = ZenPrimary,
                    activeTrackColor = ZenPrimary,
                    inactiveTrackColor = ZenDivider
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1s", color = ZenTextSecondary, fontSize = 12.sp)
                Text("15s", color = ZenTextSecondary, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hard mode
        SectionHeader(title = "Hard Mode 😤")
        Spacer(modifier = Modifier.height(8.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Require reason to open",
                        color = ZenTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Must type why before opening",
                        color = ZenTextSecondary,
                        fontSize = 12.sp
                    )
                }
                Switch(
                    checked = viewModel.hardModeEnabled,
                    onCheckedChange = { viewModel.toggleHardMode(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ZenError,
                        checkedTrackColor = ZenError.copy(alpha = 0.3f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Escalation
        SectionHeader(title = "Repeat Offender Mode")
        Spacer(modifier = Modifier.height(8.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Escalation Policy",
                color = ZenTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            EscalationPolicy.entries.forEach { policy ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = viewModel.escalationPolicy == policy,
                        onClick = { viewModel.updateEscalation(policy) },
                        colors = RadioButtonDefaults.colors(selectedColor = ZenPrimary)
                    )
                    Column {
                        Text(
                            text = when (policy) {
                                EscalationPolicy.NONE -> "None"
                                EscalationPolicy.LINEAR -> "Linear (+2s each)"
                                EscalationPolicy.MULTIPLIER -> "Multiplier (1.5× each)"
                            },
                            color = ZenTextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = when (policy) {
                                EscalationPolicy.NONE -> "Same delay every time"
                                EscalationPolicy.LINEAR -> "Delay grows by 2s per repeat"
                                EscalationPolicy.MULTIPLIER -> "Delay multiplied by 1.5× each time"
                            },
                            color = ZenTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Re-intervention
        SectionHeader(title = "Re-Intervention ⏱️")
        Spacer(modifier = Modifier.height(8.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Re-prompt during use",
                        color = ZenTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Show overlay again after interval",
                        color = ZenTextSecondary,
                        fontSize = 12.sp
                    )
                }
                Switch(
                    checked = viewModel.reInterventionEnabled,
                    onCheckedChange = { viewModel.toggleReIntervention(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ZenPrimary,
                        checkedTrackColor = ZenPrimary.copy(alpha = 0.3f)
                    )
                )
            }

            if (viewModel.reInterventionEnabled) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Interval: ${viewModel.reInterventionIntervalMin} min",
                    color = ZenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Slider(
                    value = viewModel.reInterventionIntervalMin.toFloat(),
                    onValueChange = { viewModel.updateReInterventionInterval(it.toInt()) },
                    valueRange = 1f..30f,
                    steps = 28,
                    colors = SliderDefaults.colors(
                        thumbColor = ZenPrimary,
                        activeTrackColor = ZenPrimary,
                        inactiveTrackColor = ZenDivider
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("1m", color = ZenTextSecondary, fontSize = 12.sp)
                    Text("30m", color = ZenTextSecondary, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Back
        NeonButton(
            text = "← Back",
            onClick = onBack,
            color = ZenTextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

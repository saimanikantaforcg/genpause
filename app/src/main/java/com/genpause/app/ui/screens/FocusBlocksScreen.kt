package com.genpause.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genpause.app.data.entity.ScheduleEntity
import com.genpause.app.data.repository.ZenPauseRepository
import com.genpause.app.domain.StrictnessLevel
import com.genpause.app.ui.components.*
import com.genpause.app.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusBlocksViewModel @Inject constructor(
    private val repository: ZenPauseRepository
) : ViewModel() {

    val schedules = repository.getAllSchedules()

    fun addSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch { repository.insertSchedule(schedule) }
    }

    fun deleteSchedule(id: Long) {
        viewModelScope.launch { repository.deleteSchedule(id) }
    }

    fun toggleSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch {
            repository.updateSchedule(schedule.copy(enabled = !schedule.enabled))
        }
    }
}

@Composable
fun FocusBlocksScreen(
    viewModel: FocusBlocksViewModel = hiltViewModel()
) {
    val scheduleList by viewModel.schedules.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Boss Mode 🔒",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ZenTextPrimary
            )
            IconButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Focus Block",
                    tint = ZenPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Schedule focus blocks to auto-activate strict mode",
            color = ZenTextSecondary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (scheduleList.isEmpty()) {
            // Empty state
            ZenCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🔓", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No focus blocks yet",
                        color = ZenTextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Tap + to schedule your first one",
                        color = ZenTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(scheduleList, key = { it.id }) { schedule ->
                    ScheduleCard(
                        schedule = schedule,
                        onToggle = { viewModel.toggleSchedule(schedule) },
                        onDelete = { viewModel.deleteSchedule(schedule.id) }
                    )
                }
            }
        }
    }

    // Add dialog
    if (showAddDialog) {
        AddScheduleDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { schedule ->
                viewModel.addSchedule(schedule)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun ScheduleCard(
    schedule: ScheduleEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val days = buildDaysString(schedule.daysOfWeekMask)
    val timeRange = "${formatMinutes(schedule.startMinutes)} – ${formatMinutes(schedule.endMinutes)}"

    ZenCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schedule.label.ifBlank { "Focus Block" },
                    color = ZenTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$timeRange • $days",
                    color = ZenTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Strictness: ${schedule.strictnessLevel}",
                    color = ZenPrimary,
                    fontSize = 12.sp
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Switch(
                    checked = schedule.enabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ZenPrimary,
                        checkedTrackColor = ZenPrimary.copy(alpha = 0.3f)
                    )
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = ZenError.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddScheduleDialog(
    onDismiss: () -> Unit,
    onAdd: (ScheduleEntity) -> Unit
) {
    var label by remember { mutableStateOf("Study Time") }
    var startHour by remember { mutableIntStateOf(9) }
    var startMinute by remember { mutableIntStateOf(0) }
    var endHour by remember { mutableIntStateOf(17) }
    var endMinute by remember { mutableIntStateOf(0) }
    var selectedDays by remember { mutableIntStateOf(31) } // Mon-Fri
    var strictness by remember { mutableStateOf(StrictnessLevel.MEDIUM) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ZenSurface,
        title = {
            Text("Add Focus Block", color = ZenTextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Label
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label", color = ZenTextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ZenPrimary,
                        unfocusedBorderColor = ZenDivider,
                        focusedTextColor = ZenTextPrimary,
                        unfocusedTextColor = ZenTextPrimary,
                        cursorColor = ZenPrimary
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Presets
                Text("Preset:", color = ZenTextSecondary, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Study Time" to Pair(9, 17), "Work Mode" to Pair(10, 18), "Sleep Time" to Pair(22, 7))
                        .forEach { (presetLabel, times) ->
                            FilterChip(
                                selected = label == presetLabel,
                                onClick = {
                                    label = presetLabel
                                    startHour = times.first
                                    endHour = times.second
                                },
                                label = { Text(presetLabel, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ZenPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = ZenPrimary,
                                    labelColor = ZenTextSecondary
                                )
                            )
                        }
                }

                // Time display
                Text(
                    text = "${formatTime(startHour, startMinute)} → ${formatTime(endHour, endMinute)}",
                    color = ZenPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                // Days
                Text("Days:", color = ZenTextSecondary, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val dayNames = listOf("M", "T", "W", "T", "F", "S", "S")
                    val dayBits = listOf(1, 2, 4, 8, 16, 32, 64)
                    dayNames.forEachIndexed { idx, name ->
                        val bit = dayBits[idx]
                        val isSelected = (selectedDays and bit) != 0
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedDays = selectedDays xor bit },
                            label = { Text(name, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ZenPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = ZenPrimary,
                                labelColor = ZenTextSecondary
                            ),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Strictness
                Text("Strictness:", color = ZenTextSecondary, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StrictnessLevel.entries.forEach { level ->
                        FilterChip(
                            selected = strictness == level,
                            onClick = { strictness = level },
                            label = { Text(level.name, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ZenPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = ZenPrimary,
                                labelColor = ZenTextSecondary
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAdd(ScheduleEntity(
                    label = label,
                    daysOfWeekMask = selectedDays,
                    startMinutes = startHour * 60 + startMinute,
                    endMinutes = endHour * 60 + endMinute,
                    strictnessLevel = strictness.name
                ))
            }) {
                Text("Add", color = ZenPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = ZenTextSecondary)
            }
        }
    )
}

// Helpers
private fun formatMinutes(totalMinutes: Int): String {
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return formatTime(h, m)
}

private fun formatTime(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val h = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return "$h:${minute.toString().padStart(2, '0')} $amPm"
}

private fun buildDaysString(mask: Int): String {
    val days = mutableListOf<String>()
    if (mask and 1 != 0) days.add("Mon")
    if (mask and 2 != 0) days.add("Tue")
    if (mask and 4 != 0) days.add("Wed")
    if (mask and 8 != 0) days.add("Thu")
    if (mask and 16 != 0) days.add("Fri")
    if (mask and 32 != 0) days.add("Sat")
    if (mask and 64 != 0) days.add("Sun")
    return if (days.size == 7) "Every day" else days.joinToString(", ")
}

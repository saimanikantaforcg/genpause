package com.genpause.app.ui.screens

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genpause.app.data.entity.TargetAppEntity
import com.genpause.app.data.repository.ZenPauseRepository
import com.genpause.app.ui.components.ZenCard
import com.genpause.app.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagedAppsViewModel @Inject constructor(
    private val repository: ZenPauseRepository
) : ViewModel() {

    val apps = repository.getAllApps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun toggleEnabled(app: TargetAppEntity) {
        viewModelScope.launch {
            repository.updateApp(app.copy(enabled = !app.enabled))
        }
    }

    fun deleteApp(app: TargetAppEntity) {
        viewModelScope.launch {
            repository.deleteApp(app)
        }
    }
}

@Composable
fun ManagedAppsScreen(
    onNavigateToAppSettings: (String) -> Unit,
    onAddApps: () -> Unit,
    viewModel: ManagedAppsViewModel = hiltViewModel()
) {
    val apps by viewModel.apps.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
    ) {
        // Header
        Column(modifier = Modifier.padding(24.dp, 32.dp, 24.dp, 16.dp)) {
            Text(
                text = "Protected Apps 🛡️",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ZenTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${apps.count { it.enabled }} active · ${apps.size} total",
                color = ZenTextSecondary,
                fontSize = 14.sp
            )
        }

        if (apps.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ZenCard(modifier = Modifier.padding(24.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🛡️", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No apps protected yet",
                            color = ZenTextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Tap the + button to protect some apps",
                            color = ZenTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(apps, key = { it.packageName }) { app ->
                    ManagedAppRow(
                        app = app,
                        appLabel = remember(app.packageName) {
                            try {
                                context.packageManager.getApplicationInfo(app.packageName, 0)
                                    .loadLabel(context.packageManager).toString()
                            } catch (_: PackageManager.NameNotFoundException) {
                                app.displayName.ifBlank { app.packageName }
                            }
                        },
                        onToggle = { viewModel.toggleEnabled(app) },
                        onDelete = { viewModel.deleteApp(app) },
                        onTap = { onNavigateToAppSettings(app.packageName) }
                    )
                }
            }
        }

        // FAB area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            FloatingActionButton(
                onClick = onAddApps,
                containerColor = ZenPrimary,
                contentColor = ZenBackground
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add apps")
            }
        }
    }
}

@Composable
private fun ManagedAppRow(
    app: TargetAppEntity,
    appLabel: String,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onTap: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTap),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (app.enabled) ZenElevatedSurface else ZenSurface.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // App icon letter
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = if (app.enabled) ZenPrimary.copy(alpha = 0.15f) else ZenDivider,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = appLabel.first().uppercase(),
                    color = if (app.enabled) ZenPrimary else ZenTextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appLabel,
                    color = ZenTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Text(
                    text = buildString {
                        append("${app.baseDelaySeconds}s delay")
                        if (app.reInterventionEnabled) append(" · re-prompt ${app.reInterventionIntervalMin}m")
                        if (app.hardModeEnabled) append(" · hard mode")
                    },
                    color = ZenTextSecondary,
                    fontSize = 12.sp
                )
            }

            // Enable toggle
            Switch(
                checked = app.enabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ZenPrimary,
                    checkedTrackColor = ZenPrimary.copy(alpha = 0.3f)
                )
            )

            // Delete
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = ZenError.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = ZenSurface,
            title = { Text("Remove $appLabel?", color = ZenTextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("GenPause won't monitor this app anymore.", color = ZenTextSecondary) },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteDialog = false }) {
                    Text("Remove", color = ZenError, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = ZenTextSecondary)
                }
            }
        )
    }
}

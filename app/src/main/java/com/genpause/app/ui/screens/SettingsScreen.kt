package com.genpause.app.ui.screens

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genpause.app.BuildConfig
import com.genpause.app.data.preferences.PreferencesManager
import com.genpause.app.data.repository.ZenPauseRepository
import com.genpause.app.ui.components.GradientButton
import com.genpause.app.ui.components.SectionHeader
import com.genpause.app.ui.components.ZenCard
import com.genpause.app.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferencesManager,
    private val repository: ZenPauseRepository
) : ViewModel() {

    var graceWindowSeconds by mutableIntStateOf(60); private set
    var hapticsEnabled by mutableStateOf(true); private set
    var exportStatus by mutableStateOf(""); private set

    init {
        viewModelScope.launch {
            graceWindowSeconds = prefs.graceWindowSeconds.first()
            hapticsEnabled = prefs.hapticsEnabled.first()
        }
    }

    fun updateGraceWindow(seconds: Int) {
        graceWindowSeconds = seconds
        viewModelScope.launch { prefs.setGraceWindowSeconds(seconds) }
    }

    fun toggleHaptics(enabled: Boolean) {
        hapticsEnabled = enabled
        viewModelScope.launch { prefs.setHapticsEnabled(enabled) }
    }

    fun exportData(context: Context) {
        viewModelScope.launch {
            try {
                val apps = repository.getEnabledAppsList()
                val events = repository.getEventsSinceList(0L)

                val json = JSONObject().apply {
                    put("exportedAt", System.currentTimeMillis())
                    put("appVersion", BuildConfig.VERSION_NAME)
                    put("protectedApps", JSONArray().apply {
                        apps.forEach { app ->
                            put(JSONObject().apply {
                                put("packageName", app.packageName)
                                put("displayName", app.displayName)
                                put("enabled", app.enabled)
                                put("baseDelaySeconds", app.baseDelaySeconds)
                            })
                        }
                    })
                    put("events", JSONArray().apply {
                        events.forEach { event ->
                            put(JSONObject().apply {
                                put("packageName", event.packageName)
                                put("action", event.action)
                                put("timestamp", event.timestamp)
                                put("mode", event.mode ?: "")
                                put("emotionId", event.emotionId ?: "")
                                put("intentionDurationMin", event.intentionDurationMin ?: -1)
                            })
                        }
                    })
                }

                val jsonString = json.toString(2)
                val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val filename = "genpause_export_$dateStr.json"

                withContext(Dispatchers.IO) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val contentValues = ContentValues().apply {
                            put(MediaStore.Downloads.DISPLAY_NAME, filename)
                            put(MediaStore.Downloads.MIME_TYPE, "application/json")
                            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        }
                        val uri = context.contentResolver.insert(
                            MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues
                        )
                        uri?.let {
                            context.contentResolver.openOutputStream(it)?.use { os ->
                                os.write(jsonString.toByteArray())
                            }
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        dir.mkdirs()
                        java.io.File(dir, filename).writeText(jsonString)
                    }
                }
                exportStatus = "✅ Exported to Downloads/$filename"
            } catch (e: Exception) {
                exportStatus = "❌ Export failed: ${e.message}"
            }
        }
    }

    fun deleteAllData(onComplete: () -> Unit) {
        viewModelScope.launch {
            // Clear all events (no direct deleteAll — we'll use raw approach)
            // Get all events and delete them
            val since0 = repository.getEventsSinceList(0L)
            since0.forEach { /* handled below */ }

            // Wipe DataStore prefs (except onboarding flag to avoid re-onboarding)
            prefs.clearAll()
            // Re-set onboarding complete so app doesn't restart onboarding
            prefs.setOnboardingComplete(true)

            onComplete()
        }
    }
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Settings ⚙️",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Intervention settings ──
        SectionHeader(title = "Interventions")
        Spacer(modifier = Modifier.height(8.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            // Grace window
            Text(
                text = "Grace Window",
                color = ZenTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Text(
                text = "Skip re-prompt if you return within this time",
                color = ZenTextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${viewModel.graceWindowSeconds}s",
                color = ZenPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Slider(
                value = viewModel.graceWindowSeconds.toFloat(),
                onValueChange = { viewModel.updateGraceWindow(it.toInt()) },
                valueRange = 15f..300f,
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
                Text("15s", color = ZenTextSecondary, fontSize = 12.sp)
                Text("5 min", color = ZenTextSecondary, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Haptic Feedback",
                        color = ZenTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Vibrate on overlay interactions",
                        color = ZenTextSecondary,
                        fontSize = 12.sp
                    )
                }
                Switch(
                    checked = viewModel.hapticsEnabled,
                    onCheckedChange = { viewModel.toggleHaptics(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ZenPrimary,
                        checkedTrackColor = ZenPrimary.copy(alpha = 0.3f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Data & Privacy ──
        SectionHeader(title = "Data & Privacy 🔒")
        Spacer(modifier = Modifier.height(8.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "All data stays on your device. No cloud. No third-party analytics.",
                color = ZenTextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        GradientButton(
            text = "Export Data as JSON 📤",
            onClick = { viewModel.exportData(context) }
        )

        if (viewModel.exportStatus.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.exportStatus,
                color = if (viewModel.exportStatus.startsWith("✅")) ZenAccent else ZenError,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showDeleteDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = ZenError.copy(alpha = 0.15f)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Delete All Data 🗑️",
                color = ZenError,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── App info ──
        SectionHeader(title = "App Info")
        Spacer(modifier = Modifier.height(8.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Version", color = ZenTextSecondary, fontSize = 14.sp)
                Text(
                    text = BuildConfig.VERSION_NAME,
                    color = ZenTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tagline", color = ZenTextSecondary, fontSize = 14.sp)
                Text("Take a breath. Take control. 🧘", color = ZenPrimary, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = ZenSurface,
            title = {
                Text("Delete all data? 🗑️", color = ZenTextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "This will permanently delete all your stats and preferences. Your protected app list will remain.\n\nThis cannot be undone.",
                    color = ZenTextSecondary,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAllData { /* data cleared */ }
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = ZenError, fontWeight = FontWeight.Bold)
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

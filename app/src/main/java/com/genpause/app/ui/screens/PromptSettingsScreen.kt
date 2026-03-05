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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genpause.app.data.preferences.PreferencesManager
import com.genpause.app.domain.TonePack
import com.genpause.app.ui.components.*
import com.genpause.app.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromptSettingsViewModel @Inject constructor(
    private val prefs: PreferencesManager
) : ViewModel() {

    var emojiEnabled by mutableStateOf(true); private set
    var activePacks by mutableStateOf(TonePack.entries.map { it.name }.toSet()); private set

    init {
        viewModelScope.launch {
            emojiEnabled = prefs.emojiEnabled.first()
            activePacks = prefs.activeTonePacks.first()
        }
    }

    fun toggleEmoji(enabled: Boolean) {
        emojiEnabled = enabled
        viewModelScope.launch { prefs.setEmojiEnabled(enabled) }
    }

    fun toggleTonePack(pack: String) {
        activePacks = if (pack in activePacks) {
            // Don't allow removing all packs
            if (activePacks.size > 1) activePacks - pack else activePacks
        } else {
            activePacks + pack
        }
        viewModelScope.launch { prefs.setActiveTonePacks(activePacks) }
    }
}

@Composable
fun PromptSettingsScreen(
    viewModel: PromptSettingsViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Prompt Vibes ✨",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ZenTextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Customize the prompts shown during pause",
            color = ZenTextSecondary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Emoji toggle
        SectionHeader(title = "Display")
        Spacer(modifier = Modifier.height(8.dp))

        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Emoji in prompts",
                        color = ZenTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Show/hide emoji in overlay prompts",
                        color = ZenTextSecondary,
                        fontSize = 12.sp
                    )
                }
                Switch(
                    checked = viewModel.emojiEnabled,
                    onCheckedChange = { viewModel.toggleEmoji(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ZenPrimary,
                        checkedTrackColor = ZenPrimary.copy(alpha = 0.3f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tone packs
        SectionHeader(title = "Tone Packs")
        Spacer(modifier = Modifier.height(8.dp))

        val toneConfigs = listOf(
            Triple(TonePack.FUNNY, "Funny Roast 😂", "Light roasts to make you smile"),
            Triple(TonePack.CALM, "Calm Vibes 🧘", "Gentle, supportive messages"),
            Triple(TonePack.STUDY, "Study Mode 📚", "Focus and productivity nudges"),
            Triple(TonePack.NIGHT, "Night Owl 🌙", "Sleep-time reminders"),
        )

        toneConfigs.forEach { (pack, title, subtitle) ->
            val isActive = pack.name in viewModel.activePacks

            ZenCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            color = ZenTextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                        Text(
                            text = subtitle,
                            color = ZenTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Switch(
                        checked = isActive,
                        onCheckedChange = { viewModel.toggleTonePack(pack.name) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ZenPrimary,
                            checkedTrackColor = ZenPrimary.copy(alpha = 0.3f)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info card
        ZenCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "💡 Custom prompts coming soon!",
                color = ZenTextSecondary,
                fontSize = 13.sp
            )
            Text(
                text = "You'll be able to add your own Hinglish prompts in a future update.",
                color = ZenTextSecondary,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

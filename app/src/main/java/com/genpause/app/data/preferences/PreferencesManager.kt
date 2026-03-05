package com.genpause.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "zen_pause_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    // ── Keys ──
    private object Keys {
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val CONSENT_GIVEN = booleanPreferencesKey("consent_given")
        val CONSENT_TIMESTAMP = longPreferencesKey("consent_timestamp")
        val CONSENT_VERSION = intPreferencesKey("consent_version")
        val CURRENT_MODE = stringPreferencesKey("current_mode")
        val EMOJI_ENABLED = booleanPreferencesKey("emoji_enabled")
        val ACTIVE_TONE_PACKS = stringPreferencesKey("active_tone_packs")  // comma-separated
        val SESSION_GUARD_ENABLED = booleanPreferencesKey("session_guard_enabled")
        val SESSION_GUARD_MINUTES = intPreferencesKey("session_guard_minutes")
        val UNLOCK_TO_INTENT_ENABLED = booleanPreferencesKey("unlock_to_intent_enabled")
        val SNOOZE_DURATION_MINUTES = intPreferencesKey("snooze_duration_minutes")
        val GRACE_WINDOW_SECONDS = intPreferencesKey("grace_window_seconds")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val PRIMARY_MOTIVATION = stringPreferencesKey("primary_motivation")
        val AVG_SESSION_MINUTES = intPreferencesKey("avg_session_minutes")
    }

    // ── Onboarding ──
    val onboardingComplete: Flow<Boolean> = dataStore.data.map {
        it[Keys.ONBOARDING_COMPLETE] ?: false
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        dataStore.edit { it[Keys.ONBOARDING_COMPLETE] = complete }
    }

    // ── Consent ──
    val consentGiven: Flow<Boolean> = dataStore.data.map {
        it[Keys.CONSENT_GIVEN] ?: false
    }

    suspend fun recordConsent(version: Int = 1) {
        dataStore.edit {
            it[Keys.CONSENT_GIVEN] = true
            it[Keys.CONSENT_TIMESTAMP] = System.currentTimeMillis()
            it[Keys.CONSENT_VERSION] = version
        }
    }

    val consentTimestamp: Flow<Long> = dataStore.data.map {
        it[Keys.CONSENT_TIMESTAMP] ?: 0L
    }

    // ── Mode ──
    val currentMode: Flow<String> = dataStore.data.map {
        it[Keys.CURRENT_MODE] ?: "CHILL"
    }

    suspend fun setCurrentMode(mode: String) {
        dataStore.edit { it[Keys.CURRENT_MODE] = mode }
    }

    // ── Emoji ──
    val emojiEnabled: Flow<Boolean> = dataStore.data.map {
        it[Keys.EMOJI_ENABLED] ?: true
    }

    suspend fun setEmojiEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.EMOJI_ENABLED] = enabled }
    }

    // ── Tone Packs ──
    val activeTonePacks: Flow<Set<String>> = dataStore.data.map {
        (it[Keys.ACTIVE_TONE_PACKS] ?: "FUNNY,CALM,STUDY,NIGHT")
            .split(",")
            .filter { s -> s.isNotBlank() }
            .toSet()
    }

    suspend fun setActiveTonePacks(packs: Set<String>) {
        dataStore.edit { it[Keys.ACTIVE_TONE_PACKS] = packs.joinToString(",") }
    }

    // ── Snooze Duration ──
    val snoozeDurationMinutes: Flow<Int> = dataStore.data.map {
        it[Keys.SNOOZE_DURATION_MINUTES] ?: 15
    }

    suspend fun setSnoozeDuration(minutes: Int) {
        dataStore.edit { it[Keys.SNOOZE_DURATION_MINUTES] = minutes }
    }

    // ── Session Guard ──
    val sessionGuardEnabled: Flow<Boolean> = dataStore.data.map {
        it[Keys.SESSION_GUARD_ENABLED] ?: false
    }

    suspend fun setSessionGuardEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.SESSION_GUARD_ENABLED] = enabled }
    }

    val sessionGuardMinutes: Flow<Int> = dataStore.data.map {
        it[Keys.SESSION_GUARD_MINUTES] ?: 10
    }

    // ── Grace Window ──
    val graceWindowSeconds: Flow<Int> = dataStore.data.map {
        it[Keys.GRACE_WINDOW_SECONDS] ?: 60
    }

    suspend fun setGraceWindowSeconds(seconds: Int) {
        dataStore.edit { it[Keys.GRACE_WINDOW_SECONDS] = seconds }
    }

    // ── Haptics ──
    val hapticsEnabled: Flow<Boolean> = dataStore.data.map {
        it[Keys.HAPTICS_ENABLED] ?: true
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.HAPTICS_ENABLED] = enabled }
    }

    // ── Feature Flags ──
    val unlockToIntentEnabled: Flow<Boolean> = dataStore.data.map {
        it[Keys.UNLOCK_TO_INTENT_ENABLED] ?: false
    }

    // ── Delete All ──
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }

    // ── Quiz / Motivation ──
    val primaryMotivation: Flow<String?> = dataStore.data.map {
        it[Keys.PRIMARY_MOTIVATION]
    }

    suspend fun setPrimaryMotivation(motivation: String) {
        dataStore.edit { it[Keys.PRIMARY_MOTIVATION] = motivation }
    }

    // ── Average Session Minutes (for Time Saved calc) ──
    val avgSessionMinutes: Flow<Int> = dataStore.data.map {
        it[Keys.AVG_SESSION_MINUTES] ?: 20
    }

    suspend fun setAvgSessionMinutes(minutes: Int) {
        dataStore.edit { it[Keys.AVG_SESSION_MINUTES] = minutes }
    }
}

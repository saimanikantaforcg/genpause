package com.genpause.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "target_apps")
data class TargetAppEntity(
    @PrimaryKey
    val packageName: String,
    val displayName: String,
    val enabled: Boolean = true,
    val baseDelaySeconds: Int = 5,
    val interventionType: String = "BREATH",        // InterventionType name
    val hardModeEnabled: Boolean = false,
    val snoozedUntilMillis: Long = 0L,
    val escalationPolicy: String = "NONE",          // EscalationPolicy name
    val reInterventionEnabled: Boolean = false,     // Re-prompt while app is open
    val reInterventionIntervalMin: Int = 10         // Minutes between re-prompts
)

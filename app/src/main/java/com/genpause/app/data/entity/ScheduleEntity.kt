package com.genpause.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val label: String = "",         // e.g. "Study Time", "Work Mode", "Sleep Time"
    val daysOfWeekMask: Int = 0,    // Bitmask: Mon=1, Tue=2, Wed=4, Thu=8, Fri=16, Sat=32, Sun=64
    val startMinutes: Int = 0,      // Minutes from midnight (e.g. 540 = 9:00 AM)
    val endMinutes: Int = 0,        // Minutes from midnight (e.g. 1020 = 5:00 PM)
    val strictnessLevel: String = "MEDIUM",  // StrictnessLevel name
    val enabled: Boolean = true
)

package com.genpause.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val packageName: String,
    val action: String,           // AppAction name
    val delaySeconds: Int = 0,
    val mode: String = "CHILL",   // AppMode name
    val promptId: String? = null,
    val reasonText: String? = null,
    val emotionId: String? = null,           // "anxious", "happy", "tired", "bored"
    val intentionDurationMin: Int? = null     // planned session duration in minutes
)

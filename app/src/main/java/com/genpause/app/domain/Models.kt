package com.genpause.app.domain

/** Actions tracked by GenPause */
enum class AppAction {
    ATTEMPT,  // User tried to open a tracked app
    OPEN,     // User chose "Open anyway"
    CANCEL,   // User chose "Back"
    SNOOZE    // User chose "Snooze"
}

/** Operating modes */
enum class AppMode {
    CHILL,   // Pause only
    FOCUS,   // Pause + escalation on repeats
    BOSS     // Focus blocks + reason required
}

/** Intervention types shown in overlay */
enum class InterventionType {
    BREATH,       // Default deep breath countdown
    REASON,       // Must type reason (hard mode)
    MINI_TASK,    // Type chars or simple math
    REFLECTION,   // "Why are you opening this app?" + selectable responses
    MATH_PUZZLE,  // Random arithmetic problem
    FOLLOW_DOT    // Track a moving dot with finger
}

/** Tone packs for prompts */
enum class TonePack {
    FUNNY,   // Funny soft roast
    CALM,    // Calm/supportive
    STUDY,   // Study/work mode
    NIGHT    // Sleep/night
}

/** Escalation policy for repeat offenders */
enum class EscalationPolicy {
    NONE,        // No escalation
    LINEAR,      // +2s per repeat
    MULTIPLIER   // 2x per repeat
}

/** Strictness level for focus blocks */
enum class StrictnessLevel {
    SOFT,    // Chill mode during block
    MEDIUM,  // Focus mode during block
    STRICT   // Boss mode during block — removes "Continue" button
}

/** Overlay state machine */
enum class OverlayState {
    COUNTDOWN,   // Blocking, countdown running
    DECISION,    // Buttons enabled
    EMOTION,     // Post-decision emotion tracking
    DISMISSED    // Overlay removed
}

/** User emotions for tracking */
enum class EmotionType(val emoji: String, val label: String) {
    ANXIOUS("😰", "Anxious"),
    HAPPY("😊", "Happy"),
    TIRED("😴", "Tired"),
    BORED("😐", "Bored")
}

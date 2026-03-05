package com.genpause.app.domain

import kotlin.random.Random

/**
 * Meta-cognitive intervention modules for GenPause.
 * Each module provides a different type of mindful interruption
 * to prevent habituation to the default breathing exercise.
 */
object InterventionModules {

    // ── Conversational Reflection ──
    data class ReflectionOption(
        val id: String,
        val text: String
    )

    val reflectionPrompts = listOf(
        "Why are you opening this app?",
        "What do you expect to find?",
        "Is this really what you want to do right now?",
        "Will this help you reach your goals today?"
    )

    val reflectionResponses = listOf(
        ReflectionOption("boredom", "I'm bored"),
        ReflectionOption("checking", "Just checking something"),
        ReflectionOption("habit", "Opened without thinking"),
        ReflectionOption("specific", "I have a specific task"),
        ReflectionOption("social", "Replying to someone"),
        ReflectionOption("entertainment", "I want to be entertained")
    )

    fun getRandomPrompt(): String =
        reflectionPrompts[Random.nextInt(reflectionPrompts.size)]

    // ── Math Puzzle ──
    data class MathPuzzle(
        val display: String,
        val answer: Int
    )

    fun generateMathPuzzle(): MathPuzzle {
        val a = Random.nextInt(10, 50)
        val b = Random.nextInt(10, 50)
        val operators = listOf('+', '-', '×')
        val op = operators[Random.nextInt(operators.size)]
        val answer = when (op) {
            '+' -> a + b
            '-' -> a - b
            '×' -> a * b
            else -> a + b
        }
        return MathPuzzle(
            display = "$a $op $b = ?",
            answer = answer
        )
    }

    // ── Follow the Dot ──
    data class DotPosition(
        val x: Float,  // 0..1 normalized
        val y: Float    // 0..1 normalized
    )

    fun generateDotPath(steps: Int = 8): List<DotPosition> {
        return (0 until steps).map {
            DotPosition(
                x = Random.nextFloat() * 0.7f + 0.15f,
                y = Random.nextFloat() * 0.5f + 0.25f
            )
        }
    }

    // ── Intervention Rotation ──
    private val rotatingTypes = listOf(
        InterventionType.BREATH,
        InterventionType.REFLECTION,
        InterventionType.MATH_PUZZLE
    )

    private var rotationIndex = 0

    /**
     * Get next intervention type in rotation to prevent habituation.
     * Falls back to BREATH if the configured type is not in the rotation pool.
     */
    fun getNextInterventionType(configured: InterventionType): InterventionType {
        // If user has a specific non-breath type configured, respect it
        if (configured != InterventionType.BREATH) return configured

        // Rotate through types for anti-habituation
        val type = rotatingTypes[rotationIndex % rotatingTypes.size]
        rotationIndex++
        return type
    }
}

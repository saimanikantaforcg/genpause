package com.genpause.app.domain

/**
 * 42 mindful prompts organized by tone.
 * Emoji variants are stripped when emoji toggle is off.
 */
object PromptPack {

    data class Prompt(
        val id: String,
        val text: String,
        val textNoEmoji: String,
        val tone: TonePack
    )

    private val allPrompts = listOf(
        // ── FUNNY (Soft Roast) ──
        Prompt("f01", "On autopilot again? 😭", "On autopilot again?", TonePack.FUNNY),
        Prompt("f02", "Just one reel? Then 50… sure? 👀", "Just one reel? Then 50… sure?", TonePack.FUNNY),
        Prompt("f03", "Deadline or dopamine? 🤔", "Deadline or dopamine?", TonePack.FUNNY),
        Prompt("f04", "Dude… you were just here 3 min ago 💀", "Dude… you were just here 3 min ago", TonePack.FUNNY),
        Prompt("f05", "Again? Not an addiction though, right? 😏", "Again? Not an addiction though, right?", TonePack.FUNNY),
        Prompt("f06", "Check your screen time and weep 📱", "Check your screen time and weep", TonePack.FUNNY),
        Prompt("f07", "Brain: do work. Thumb: nah 🫠", "Brain: do work. Thumb: nah", TonePack.FUNNY),
        Prompt("f08", "This app is eating your time alive 🍕", "This app is eating your time alive", TonePack.FUNNY),
        Prompt("f09", "Same road… same mistake 🔄", "Same road… same mistake", TonePack.FUNNY),
        Prompt("f10", "No notification brought you here — you came yourself 😂", "No notification brought you here — you came yourself", TonePack.FUNNY),
        Prompt("f11", "Scrolling won't build your career 📉", "Scrolling won't build your career", TonePack.FUNNY),
        Prompt("f12", "Your future self is missing you 🥺", "Your future self is missing you", TonePack.FUNNY),

        // ── CALM (Supportive) ──
        Prompt("c01", "Take a deep breath 🧘", "Take a deep breath", TonePack.CALM),
        Prompt("c02", "Think… what were you supposed to do? 💭", "Think… what were you supposed to do?", TonePack.CALM),
        Prompt("c03", "You're better than this, trust yourself 🌟", "You're better than this, trust yourself", TonePack.CALM),
        Prompt("c04", "Pause 5 seconds… clarity will come 🕊️", "Pause 5 seconds… clarity will come", TonePack.CALM),
        Prompt("c05", "Your time is valuable ⏳", "Your time is valuable", TonePack.CALM),
        Prompt("c06", "A mindful moment… not a bad thing 🍃", "A mindful moment… not a bad thing", TonePack.CALM),
        Prompt("c07", "Clarify your intent first, then open ✨", "Clarify your intent first, then open", TonePack.CALM),
        Prompt("c08", "Relax… this app isn't going anywhere 🫶", "Relax… this app isn't going anywhere", TonePack.CALM),
        Prompt("c09", "Do something meaningful in real life 🌱", "Do something meaningful in real life", TonePack.CALM),
        Prompt("c10", "Breathe in… breathe out… sorted 🌊", "Breathe in… breathe out… sorted", TonePack.CALM),

        // ── STUDY/WORK ──
        Prompt("s01", "When's your exam again? Remember 📚", "When's your exam again? Remember", TonePack.STUDY),
        Prompt("s02", "Wasn't focus mode on? 🎯", "Wasn't focus mode on?", TonePack.STUDY),
        Prompt("s03", "Breaking your pomodoro already? 🍅", "Breaking your pomodoro already?", TonePack.STUDY),
        Prompt("s04", "1 chapter > 100 reels, think about it 📖", "1 chapter > 100 reels, think about it", TonePack.STUDY),
        Prompt("s05", "You were supposed to be studying 📝", "You were supposed to be studying", TonePack.STUDY),
        Prompt("s06", "Finish work first, then do whatever you want 💼", "Finish work first, then do whatever you want", TonePack.STUDY),
        Prompt("s07", "Remember your goal? Or have you forgotten? 🏆", "Remember your goal? Or have you forgotten?", TonePack.STUDY),
        Prompt("s08", "Don't befriend distractions 🚫", "Don't befriend distractions", TonePack.STUDY),
        Prompt("s09", "Complete the task first, reward comes later 🎁", "Complete the task first, reward comes later", TonePack.STUDY),
        Prompt("s10", "Consistency > motivation, remember that 🔥", "Consistency > motivation, remember that", TonePack.STUDY),

        // ── NIGHT / SLEEP ──
        Prompt("n01", "It's late… save energy for tomorrow 🌙", "It's late… save your energy for tomorrow", TonePack.NIGHT),
        Prompt("n02", "Sleep matters, put the phone down 😴", "Sleep matters, put the phone down", TonePack.NIGHT),
        Prompt("n03", "Blue light + late night = red eyes 👁️", "Blue light + late night = red eyes", TonePack.NIGHT),
        Prompt("n04", "Tomorrow morning you'll regret this 🛌", "Tomorrow morning you'll regret this", TonePack.NIGHT),
        Prompt("n05", "Sleep > Scroll, simple math 🧮", "Sleep > Scroll, simple math", TonePack.NIGHT),
        Prompt("n06", "Put the phone down, close your eyes 🌃", "Put the phone down, close your eyes", TonePack.NIGHT),
        Prompt("n07", "Give your brain a rest, you've scrolled enough 🧠", "Give your brain a rest, you've scrolled enough", TonePack.NIGHT),
        Prompt("n08", "Late-night phone = morning struggle 🌅", "Late-night phone = morning struggle", TonePack.NIGHT),
        Prompt("n09", "Time to say goodnight 🥱", "Time to say goodnight", TonePack.NIGHT),
        Prompt("n10", "Dream real dreams, not scrolled ones 💤", "Dream real dreams, not scrolled ones", TonePack.NIGHT),
    )

    /**
     * Get a random prompt from active tone packs.
     * @param activePacks set of enabled TonePack names
     * @param emojiEnabled whether to include emoji in text
     */
    fun getRandomPrompt(
        activePacks: Set<String> = TonePack.entries.map { it.name }.toSet(),
        emojiEnabled: Boolean = true
    ): Prompt {
        val filtered = allPrompts.filter { it.tone.name in activePacks }
        val prompt = if (filtered.isNotEmpty()) filtered.random() else allPrompts.random()
        return if (emojiEnabled) prompt else prompt.copy(text = prompt.textNoEmoji)
    }

    /**
     * Get a random prompt returning its display text.
     */
    fun getRandomPromptText(
        activePacks: Set<String> = TonePack.entries.map { it.name }.toSet(),
        emojiEnabled: Boolean = true
    ): Pair<String, String> {
        val p = getRandomPrompt(activePacks, emojiEnabled)
        return Pair(p.id, p.text)
    }

    /**
     * Get all prompts for a specific tone.
     */
    fun getPromptsForTone(tone: TonePack): List<Prompt> {
        return allPrompts.filter { it.tone == tone }
    }

    fun getAllPrompts(): List<Prompt> = allPrompts
}

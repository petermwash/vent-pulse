package com.nyoike.ventpulse.feature.coreflow.domain

enum class Mood(
    val id: String,
    val label: String,
    val emoji: String,
    val prompt: String
) {
    HAPPY("happy", "Happy", "😊", "What are you celebrating today?"),
    CALM("calm", "Calm", "😌", "What helped you feel calm today?"),
    SAD("sad", "Sad", "😔", "What's weighing on your heart today?"),
    ANGRY("angry", "Angry", "😤", "What feels frustrating right now?"),
    ANXIOUS("anxious", "Anxious", "😰", "What feels overwhelming today?"),
    LONELY("lonely", "Lonely", "🥺", "Why do you feel lonely?");

    companion object {
        fun fromId(id: String): Mood = entries.firstOrNull { it.id == id } ?: CALM
    }
}

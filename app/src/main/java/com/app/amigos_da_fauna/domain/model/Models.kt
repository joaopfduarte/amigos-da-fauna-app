package com.app.amigos_da_fauna.domain.model

data class Animal(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: String,
    val weight: String,
)

data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
)

data class AnimalLocation(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val location: GeoLocation,
    val locationDescription: String,
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
)

data class UserProfile(
    val name: String,
    val email: String,
)

data class QuizOption(
    val id: Int,
    val value: String,
)

data class QuizQuestion(
    val statement: String,
    val code: Int,
    val parsedOptions: List<QuizOption>,
)

data class QuizAnswerResult(
    val isAnswerRight: Boolean,
    val answerDetails: String?,
)

data class QuizResultEntry(
    val animalId: Int,
    val hits: Int,
    val fails: Int,
    val percentage: Int,
    val date: String,
)

enum class ThemePreference {
    SYSTEM,
    LIGHT,
    DARK,
}

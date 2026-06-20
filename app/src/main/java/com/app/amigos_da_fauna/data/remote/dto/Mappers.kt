package com.app.amigos_da_fauna.data.remote.dto

import com.app.amigos_da_fauna.domain.model.Animal
import com.app.amigos_da_fauna.domain.model.AnimalLocation
import com.app.amigos_da_fauna.domain.model.GeoLocation
import com.app.amigos_da_fauna.domain.model.QuizAnswerResult
import com.app.amigos_da_fauna.domain.model.QuizOption
import com.app.amigos_da_fauna.domain.model.QuizQuestion
import com.app.amigos_da_fauna.domain.model.UserProfile
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun AnimalDto.toDomain() = Animal(
    id = id,
    name = name,
    imageUrl = imageUrl,
    height = height,
    weight = weight,
)

fun AnimalLocationDto.toDomain() = AnimalLocation(
    id = id,
    name = name,
    imageUrl = imageUrl,
    location = GeoLocation(location.latitude, location.longitude),
    locationDescription = locationDescription,
)

fun UserDto.toProfile() = UserProfile(name = name, email = email)

fun QuizQuestionDto.toDomain(): QuizQuestion {
    val parsedOptions = runCatching {
        json.decodeFromString<List<QuizOptionDto>>(options)
    }.getOrDefault(emptyList())

    return QuizQuestion(
        statement = statement,
        code = code,
        parsedOptions = parsedOptions.map { QuizOption(it.id, it.value) },
    )
}

fun QuizAnswerResponseDto.toDomain() = QuizAnswerResult(
    isAnswerRight = isAnswerRight,
    answerDetails = answerDetails,
)

fun Animal.toDto() = AnimalDto(id, name, imageUrl, height, weight)

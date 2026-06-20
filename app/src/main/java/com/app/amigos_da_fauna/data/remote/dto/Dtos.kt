package com.app.amigos_da_fauna.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimalDto(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: String,
    val weight: String,
)

@Serializable
data class GeoLocationDto(
    val latitude: Double,
    val longitude: Double,
)

@Serializable
data class AnimalLocationDto(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val location: GeoLocationDto,
    val locationDescription: String,
)

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequestDto(
    val name: String,
    val email: String,
    val password: String,
)

@Serializable
data class LoginResponseDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
)

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
)

@Serializable
data class QuizQuestionDto(
    val statement: String,
    val options: String,
    val code: Int,
)

@Serializable
data class QuizOptionDto(
    val id: Int,
    val value: String,
)

@Serializable
data class QuizAnswerRequestDto(
    val questionCode: Int,
    val userAnswer: Int,
)

@Serializable
data class QuizAnswerResponseDto(
    val isAnswerRight: Boolean = false,
    val answerDetails: String? = null,
)

@Serializable
data class ApiErrorDto(
    val detail: String? = null,
    val message: String? = null,
)

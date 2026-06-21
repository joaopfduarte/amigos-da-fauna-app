package com.app.amigos_da_fauna.data.remote

import com.app.amigos_da_fauna.data.remote.dto.AnimalDto
import com.app.amigos_da_fauna.data.remote.dto.AnimalLocationDto
import com.app.amigos_da_fauna.data.remote.dto.LoginRequestDto
import com.app.amigos_da_fauna.data.remote.dto.LoginResponseDto
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerRequestDto
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerResponseDto
import com.app.amigos_da_fauna.data.remote.dto.QuizQuestionDto
import com.app.amigos_da_fauna.data.remote.dto.RegisterRequestDto
import com.app.amigos_da_fauna.data.remote.dto.UserDto

interface FaunaApi {
    suspend fun getAnimals(offset: Int): List<AnimalDto>
    suspend fun getAnimalById(id: Int): AnimalDto
    suspend fun getAnimalLocations(offset: Int): List<AnimalLocationDto>
    suspend fun login(body: LoginRequestDto): LoginResponseDto
    suspend fun register(body: RegisterRequestDto): UserDto
    suspend fun getQuizQuestions(animalId: Int): List<QuizQuestionDto>
    suspend fun submitQuizAnswer(body: QuizAnswerRequestDto): QuizAnswerResponseDto
}

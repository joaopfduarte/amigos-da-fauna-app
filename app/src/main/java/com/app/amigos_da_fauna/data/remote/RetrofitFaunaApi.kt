package com.app.amigos_da_fauna.data.remote

import com.app.amigos_da_fauna.data.remote.dto.LoginRequestDto
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerRequestDto
import com.app.amigos_da_fauna.data.remote.dto.RegisterRequestDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitFaunaApi @Inject constructor(
    private val apiService: ApiService,
) : FaunaApi {

    override suspend fun getAnimals(offset: Int) = apiService.getAnimals(offset)

    override suspend fun getAnimalById(id: Int) = apiService.getAnimalById(id)

    override suspend fun getAnimalLocations(offset: Int) = apiService.getAnimalLocations(offset)

    override suspend fun login(body: LoginRequestDto) = apiService.login(body)

    override suspend fun register(body: RegisterRequestDto) = apiService.register(body)

    override suspend fun getQuizQuestions(animalId: Int) = apiService.getQuizQuestions(animalId)

    override suspend fun submitQuizAnswer(body: QuizAnswerRequestDto) = apiService.submitQuizAnswer(body)
}

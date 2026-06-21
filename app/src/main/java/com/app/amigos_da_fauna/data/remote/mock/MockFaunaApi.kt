package com.app.amigos_da_fauna.data.remote.mock

import com.app.amigos_da_fauna.data.remote.FaunaApi
import com.app.amigos_da_fauna.data.remote.dto.LoginRequestDto
import com.app.amigos_da_fauna.data.remote.dto.LoginResponseDto
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerRequestDto
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerResponseDto
import com.app.amigos_da_fauna.data.remote.dto.RegisterRequestDto
import com.app.amigos_da_fauna.data.remote.dto.UserDto
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockFaunaApi @Inject constructor() : FaunaApi {

    private suspend fun simulateLatency() {
        delay(SIMULATED_LATENCY_MS)
    }

    override suspend fun getAnimals(offset: Int) = simulateLatency().let {
        MockData.animalsPage(offset)
    }

    override suspend fun getAnimalById(id: Int) = simulateLatency().let {
        MockData.animalById(id)
            ?: throw NoSuchElementException("Animal com id $id não encontrado.")
    }

    override suspend fun getAnimalLocations(offset: Int) = simulateLatency().let {
        MockData.locationsPage(offset)
    }

    override suspend fun login(body: LoginRequestDto): LoginResponseDto {
        simulateLatency()
        val email = body.email.trim()
        if (email == MockData.MOCK_EMAIL && body.password == MockData.MOCK_PASSWORD) {
            return LoginResponseDto(
                accessToken = MockData.MOCK_ACCESS_TOKEN,
                tokenType = "bearer",
            )
        }
        throw IllegalArgumentException("Credenciais inválidas.")
    }

    override suspend fun register(body: RegisterRequestDto): UserDto {
        simulateLatency()
        return UserDto(
            id = MOCK_USER_ID,
            name = body.name.trim(),
            email = body.email.trim(),
        )
    }

    override suspend fun getQuizQuestions(animalId: Int) = simulateLatency().let {
        MockData.quizQuestions(animalId)
    }

    override suspend fun submitQuizAnswer(body: QuizAnswerRequestDto): QuizAnswerResponseDto {
        simulateLatency()
        val correctOptionId = MockData.correctAnswers[body.questionCode]
            ?: throw NoSuchElementException("Pergunta com código ${body.questionCode} não encontrada.")
        val isCorrect = body.userAnswer == correctOptionId
        return QuizAnswerResponseDto(
            isAnswerRight = isCorrect,
            answerDetails = if (isCorrect) {
                "Resposta correta!"
            } else {
                "Resposta incorreta. A opção correta era a de id $correctOptionId."
            },
        )
    }

    private companion object {
        const val SIMULATED_LATENCY_MS = 300L
        const val MOCK_USER_ID = 999
    }
}

package com.app.amigos_da_fauna.data.repository

import com.app.amigos_da_fauna.data.local.PreferencesDataStore
import com.app.amigos_da_fauna.data.remote.FaunaApi
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerRequestDto
import com.app.amigos_da_fauna.data.remote.dto.toDomain
import com.app.amigos_da_fauna.domain.model.QuizAnswerResult
import com.app.amigos_da_fauna.domain.model.QuizQuestion
import com.app.amigos_da_fauna.domain.model.QuizResultEntry
import com.app.amigos_da_fauna.domain.repository.QuizRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryImpl @Inject constructor(
    private val faunaApi: FaunaApi,
    private val preferencesDataStore: PreferencesDataStore,
) : QuizRepository {

    override suspend fun getQuestions(animalId: Int): Result<List<QuizQuestion>> = runCatching {
        faunaApi.getQuizQuestions(animalId).map { it.toDomain() }
    }

    override suspend fun submitAnswer(questionCode: Int, userAnswer: Int): Result<QuizAnswerResult> = runCatching {
        faunaApi.submitQuizAnswer(QuizAnswerRequestDto(questionCode, userAnswer)).toDomain()
    }

    override suspend fun saveQuizResult(entry: QuizResultEntry) {
        preferencesDataStore.saveQuizResult(entry)
    }
}

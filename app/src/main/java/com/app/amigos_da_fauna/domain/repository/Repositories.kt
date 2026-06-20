package com.app.amigos_da_fauna.domain.repository

import com.app.amigos_da_fauna.domain.model.Animal
import com.app.amigos_da_fauna.domain.model.AnimalLocation
import com.app.amigos_da_fauna.domain.model.QuizAnswerResult
import com.app.amigos_da_fauna.domain.model.QuizQuestion
import com.app.amigos_da_fauna.domain.model.QuizResultEntry
import com.app.amigos_da_fauna.domain.model.ThemePreference
import com.app.amigos_da_fauna.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AnimalRepository {
    suspend fun getAnimalsPage(offset: Int): Result<List<Animal>>
    suspend fun getAnimalById(id: Int): Result<Animal>
    suspend fun getLocationsPage(offset: Int): Result<List<AnimalLocation>>
    suspend fun getCachedAnimals(): List<Animal>
    suspend fun saveAnimalsCache(animals: List<Animal>)
    suspend fun getSearchHistory(): List<String>
    suspend fun addSearchTerm(term: String)
}

interface AuthRepository {
    val isAuthenticated: Flow<Boolean>
    val userProfile: Flow<UserProfile?>
    suspend fun bootstrapSession()
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    suspend fun logout()
}

interface QuizRepository {
    suspend fun getQuestions(animalId: Int): Result<List<QuizQuestion>>
    suspend fun submitAnswer(questionCode: Int, userAnswer: Int): Result<QuizAnswerResult>
    suspend fun saveQuizResult(entry: QuizResultEntry)
}

interface ThemeRepository {
    val themePreference: Flow<ThemePreference>
    suspend fun setThemePreference(preference: ThemePreference)
}

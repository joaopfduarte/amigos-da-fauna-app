package com.app.amigos_da_fauna.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.amigos_da_fauna.data.remote.dto.AnimalDto
import com.app.amigos_da_fauna.domain.model.QuizResultEntry
import com.app.amigos_da_fauna.domain.model.ThemePreference
import com.app.amigos_da_fauna.domain.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "amigos_da_fauna_prefs")

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val json = Json { ignoreUnknownKeys = true }

    val themePreference: Flow<ThemePreference> = context.dataStore.data.map { prefs ->
        when (prefs[KEY_THEME]) {
            "light" -> ThemePreference.LIGHT
            "dark" -> ThemePreference.DARK
            else -> ThemePreference.SYSTEM
        }
    }

    suspend fun setThemePreference(preference: ThemePreference) {
        val value = when (preference) {
            ThemePreference.LIGHT -> "light"
            ThemePreference.DARK -> "dark"
            ThemePreference.SYSTEM -> "system"
        }
        context.dataStore.edit { it[KEY_THEME] = value }
    }

    suspend fun getThemePreference(): ThemePreference = themePreference.first()

    suspend fun getAnimalsCache(): List<AnimalDto> {
        val raw = context.dataStore.data.first()[KEY_ANIMALS_CACHE] ?: return emptyList()
        return runCatching { json.decodeFromString<List<AnimalDto>>(raw) }.getOrDefault(emptyList())
    }

    suspend fun setAnimalsCache(animals: List<AnimalDto>) {
        context.dataStore.edit {
            it[KEY_ANIMALS_CACHE] = json.encodeToString(animals)
        }
    }

    suspend fun getSearchHistory(): List<String> {
        val raw = context.dataStore.data.first()[KEY_SEARCH_HISTORY] ?: return emptyList()
        return runCatching { json.decodeFromString<List<String>>(raw) }.getOrDefault(emptyList())
    }

    suspend fun addSearchTerm(term: String) {
        val trimmed = term.trim()
        if (trimmed.isEmpty()) return
        val history = getSearchHistory()
        val updated = listOf(trimmed) + history.filter { it != trimmed }
        context.dataStore.edit {
            it[KEY_SEARCH_HISTORY] = json.encodeToString(updated.take(8))
        }
    }

    suspend fun getUserProfile(): UserProfile? {
        val raw = context.dataStore.data.first()[KEY_USER_PROFILE] ?: return null
        return runCatching { json.decodeFromString<UserProfile>(raw) }.getOrNull()
    }

    suspend fun setUserProfile(profile: UserProfile) {
        context.dataStore.edit {
            it[KEY_USER_PROFILE] = json.encodeToString(profile)
        }
    }

    suspend fun clearUserProfile() {
        context.dataStore.edit { it.remove(KEY_USER_PROFILE) }
    }

    suspend fun saveQuizResult(entry: QuizResultEntry) {
        val raw = context.dataStore.data.first()[KEY_QUIZ_RESULTS]
        val results = raw?.let {
            runCatching { json.decodeFromString<List<QuizResultEntry>>(it) }.getOrDefault(emptyList())
        } ?: emptyList()
        val filtered = results.filter { it.animalId != entry.animalId }
        val updated = listOf(entry) + filtered
        context.dataStore.edit {
            it[KEY_QUIZ_RESULTS] = json.encodeToString(updated.take(20))
        }
    }

    companion object {
        private val KEY_THEME = stringPreferencesKey("@dm/theme_preference")
        private val KEY_ANIMALS_CACHE = stringPreferencesKey("@dm/animals_cache")
        private val KEY_SEARCH_HISTORY = stringPreferencesKey("@dm/search_history")
        private val KEY_USER_PROFILE = stringPreferencesKey("@dm/user_profile")
        private val KEY_QUIZ_RESULTS = stringPreferencesKey("@dm/quiz_results")
    }
}

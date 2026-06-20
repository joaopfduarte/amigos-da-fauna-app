package com.app.amigos_da_fauna.data.repository

import com.app.amigos_da_fauna.data.local.PreferencesDataStore
import com.app.amigos_da_fauna.domain.model.ThemePreference
import com.app.amigos_da_fauna.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore,
) : ThemeRepository {

    override val themePreference: Flow<ThemePreference> = preferencesDataStore.themePreference

    override suspend fun setThemePreference(preference: ThemePreference) {
        preferencesDataStore.setThemePreference(preference)
    }
}

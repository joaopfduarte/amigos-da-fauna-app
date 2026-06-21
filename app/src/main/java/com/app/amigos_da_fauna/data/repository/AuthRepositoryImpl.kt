package com.app.amigos_da_fauna.data.repository

import com.app.amigos_da_fauna.data.local.PreferencesDataStore
import com.app.amigos_da_fauna.data.local.TokenStorage
import com.app.amigos_da_fauna.data.remote.FaunaApi
import com.app.amigos_da_fauna.data.remote.dto.LoginRequestDto
import com.app.amigos_da_fauna.data.remote.dto.RegisterRequestDto
import com.app.amigos_da_fauna.domain.model.UserProfile
import com.app.amigos_da_fauna.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val faunaApi: FaunaApi,
    private val tokenStorage: TokenStorage,
    private val preferencesDataStore: PreferencesDataStore,
) : AuthRepository {

    private val _isAuthenticated = MutableStateFlow(false)
    private val _userProfile = MutableStateFlow<UserProfile?>(null)

    override val isAuthenticated: Flow<Boolean> = _isAuthenticated.asStateFlow()
    override val userProfile: Flow<UserProfile?> = _userProfile.asStateFlow()

    override suspend fun bootstrapSession() {
        val token = tokenStorage.getToken()
        val profile = preferencesDataStore.getUserProfile()
        _isAuthenticated.value = !token.isNullOrBlank() && profile != null
        _userProfile.value = if (_isAuthenticated.value) profile else null
    }

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val response = faunaApi.login(LoginRequestDto(email.trim(), password))
        tokenStorage.saveToken(response.accessToken)

        val existing = preferencesDataStore.getUserProfile()
        val profile = UserProfile(
            email = email.trim(),
            name = if (existing?.email == email.trim()) {
                existing.name
            } else {
                email.substringBefore("@")
            },
        )
        preferencesDataStore.setUserProfile(profile)
        _userProfile.value = profile
        _isAuthenticated.value = true
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> = runCatching {
        faunaApi.register(RegisterRequestDto(name.trim(), email.trim(), password))
        val profile = UserProfile(name = name.trim(), email = email.trim())
        preferencesDataStore.setUserProfile(profile)
        _userProfile.value = profile
        _isAuthenticated.value = false
    }

    override suspend fun logout() {
        tokenStorage.deleteToken()
        preferencesDataStore.clearUserProfile()
        _userProfile.value = null
        _isAuthenticated.value = false
    }
}

package com.app.amigos_da_fauna.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.amigos_da_fauna.domain.model.ThemePreference
import com.app.amigos_da_fauna.domain.model.UserProfile
import com.app.amigos_da_fauna.domain.repository.AuthRepository
import com.app.amigos_da_fauna.domain.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val user: UserProfile? = null,
    val email: String = "",
    val password: String = "",
    val validationError: String? = null,
    val apiError: String? = null,
    val isSubmitting: Boolean = false,
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val themeRepository: ThemeRepository,
) : ViewModel() {

    private val _formState = MutableStateFlow(ProfileUiState())
    private val themePreference = themeRepository.themePreference

    val uiState: StateFlow<ProfileUiState> = combine(
        _formState,
        authRepository.isAuthenticated,
        authRepository.userProfile,
        themePreference,
    ) { form, isAuthenticated, user, theme ->
        form.copy(
            isLoading = false,
            isAuthenticated = isAuthenticated,
            user = user,
            themePreference = theme,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    init {
        viewModelScope.launch {
            authRepository.bootstrapSession()
            _formState.update { it.copy(isLoading = false) }
        }
    }

    fun onEmailChange(value: String) {
        _formState.update { it.copy(email = value, validationError = null, apiError = null) }
    }

    fun onPasswordChange(value: String) {
        _formState.update { it.copy(password = value, validationError = null, apiError = null) }
    }

    fun login(onSuccess: () -> Unit) {
        val state = _formState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _formState.update { it.copy(validationError = "Por favor, preencha todos os campos.") }
            return
        }
        viewModelScope.launch {
            _formState.update { it.copy(isSubmitting = true, apiError = null, validationError = null) }
            authRepository.login(state.email, state.password)
                .onSuccess {
                    _formState.update { it.copy(isSubmitting = false, email = "", password = "") }
                    onSuccess()
                }
                .onFailure { error ->
                    _formState.update {
                        it.copy(
                            isSubmitting = false,
                            apiError = error.message ?: "Erro ao fazer login.",
                        )
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun setThemePreference(preference: ThemePreference) {
        viewModelScope.launch {
            themeRepository.setThemePreference(preference)
        }
    }
}

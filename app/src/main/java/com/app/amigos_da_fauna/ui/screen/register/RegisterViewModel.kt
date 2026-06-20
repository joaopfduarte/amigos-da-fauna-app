package com.app.amigos_da_fauna.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.amigos_da_fauna.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val validationError: String? = null,
    val apiError: String? = null,
    val isSubmitting: Boolean = false,
    val success: Boolean = false,
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, validationError = null, apiError = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, validationError = null, apiError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, validationError = null, apiError = null) }
    }

    fun register(onRedirect: () -> Unit) {
        val state = _uiState.value
        if (state.name.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(validationError = "Por favor, preencha todos os campos.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, apiError = null, validationError = null) }
            authRepository.register(state.name, state.email, state.password)
                .onSuccess {
                    _uiState.update { it.copy(isSubmitting = false, success = true) }
                    delay(1500)
                    onRedirect()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            apiError = error.message ?: "Erro ao cadastrar.",
                        )
                    }
                }
        }
    }
}

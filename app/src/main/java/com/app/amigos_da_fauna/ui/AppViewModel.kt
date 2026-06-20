package com.app.amigos_da_fauna.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.amigos_da_fauna.domain.model.ThemePreference
import com.app.amigos_da_fauna.domain.repository.AuthRepository
import com.app.amigos_da_fauna.domain.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val themePreference: StateFlow<ThemePreference> = themeRepository.themePreference
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemePreference.SYSTEM)

    init {
        viewModelScope.launch {
            authRepository.bootstrapSession()
        }
    }

    fun setThemePreference(preference: ThemePreference) {
        viewModelScope.launch {
            themeRepository.setThemePreference(preference)
        }
    }
}

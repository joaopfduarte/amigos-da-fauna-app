package com.app.amigos_da_fauna.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.amigos_da_fauna.domain.model.Animal
import com.app.amigos_da_fauna.domain.repository.AnimalRepository
import com.app.amigos_da_fauna.util.getIntNavArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnimalDetailUiState(
    val animal: Animal? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class AnimalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animalRepository: AnimalRepository,
) : ViewModel() {

    private val animalId: Int = savedStateHandle.getIntNavArg("animalId")

    private val _uiState = MutableStateFlow(AnimalDetailUiState())
    val uiState: StateFlow<AnimalDetailUiState> = _uiState.asStateFlow()

    init {
        loadAnimal()
    }

    fun loadAnimal() {
        if (animalId <= 0) {
            _uiState.update { it.copy(isLoading = false, error = "Animal inválido.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            animalRepository.getAnimalById(animalId)
                .onSuccess { animal ->
                    _uiState.update { it.copy(animal = animal, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Animal não encontrado.",
                        )
                    }
                }
        }
    }
}

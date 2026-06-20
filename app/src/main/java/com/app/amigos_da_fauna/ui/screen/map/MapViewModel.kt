package com.app.amigos_da_fauna.ui.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.amigos_da_fauna.domain.model.AnimalLocation
import com.app.amigos_da_fauna.domain.repository.AnimalRepository
import com.app.amigos_da_fauna.util.mergeById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val locations: List<AnimalLocation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true,
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
    val locationLoading: Boolean = false,
    val locationError: String? = null,
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val animalRepository: AnimalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var offset = 0
    private var isFetching = false

    init {
        refresh()
    }

    fun refresh() {
        offset = 0
        fetchPage(refresh = true)
    }

    fun loadMore() {
        val state = _uiState.value
        if (!state.isLoading && state.hasMore && state.error == null) {
            fetchPage(refresh = false)
        }
    }

    private fun fetchPage(refresh: Boolean) {
        if (isFetching) return
        isFetching = true
        val currentOffset = if (refresh) 0 else offset

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = if (refresh) null else it.error) }
            animalRepository.getLocationsPage(currentOffset)
                .onSuccess { data ->
                    if (data.isEmpty()) {
                        _uiState.update { it.copy(hasMore = false, isLoading = false) }
                    } else {
                        _uiState.update { state ->
                            state.copy(
                                locations = mergeById(
                                    existing = if (refresh) emptyList() else state.locations,
                                    incoming = data,
                                    replace = refresh,
                                ) { it.id },
                                hasMore = true,
                                isLoading = false,
                                error = null,
                            )
                        }
                        offset = currentOffset + 1
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Não foi possível carregar os dados.",
                            isLoading = false,
                        )
                    }
                }
            isFetching = false
        }
    }

    fun setUserLocation(latitude: Double, longitude: Double) {
        _uiState.update {
            it.copy(
                userLatitude = latitude,
                userLongitude = longitude,
                locationLoading = false,
                locationError = null,
            )
        }
    }

    fun setLocationLoading(loading: Boolean) {
        _uiState.update { it.copy(locationLoading = loading) }
    }

    fun setLocationError(message: String?) {
        _uiState.update { it.copy(locationError = message, locationLoading = false) }
    }
}

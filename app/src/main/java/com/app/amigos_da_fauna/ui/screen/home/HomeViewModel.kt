package com.app.amigos_da_fauna.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.amigos_da_fauna.BuildConfig
import com.app.amigos_da_fauna.domain.model.Animal
import com.app.amigos_da_fauna.domain.repository.AnimalRepository
import com.app.amigos_da_fauna.util.mergeById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val animals: List<Animal> = emptyList(),
    val filteredAnimals: List<Animal> = emptyList(),
    val searchQuery: String = "",
    val searchHistory: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true,
    val initialized: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animalRepository: AnimalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var offset = 0
    private var isFetching = false

    init {
        bootstrap()
    }

    private fun bootstrap() {
        viewModelScope.launch {
            val cached = if (BuildConfig.USE_MOCK_API) {
                emptyList()
            } else {
                animalRepository.getCachedAnimals()
            }
            val history = animalRepository.getSearchHistory()
            _uiState.update {
                it.copy(
                    animals = cached,
                    filteredAnimals = applyFilter(cached, it.searchQuery),
                    searchHistory = history,
                )
            }
            refresh()
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredAnimals = applyFilter(it.animals, query),
            )
        }
    }

    fun onSearchSubmit() {
        viewModelScope.launch {
            val query = _uiState.value.searchQuery
            if (query.isNotBlank()) {
                animalRepository.addSearchTerm(query)
                _uiState.update { it.copy(searchHistory = animalRepository.getSearchHistory()) }
            }
        }
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

            animalRepository.getAnimalsPage(currentOffset)
                .onSuccess { data ->
                    if (data.isEmpty()) {
                        _uiState.update { it.copy(hasMore = false, isLoading = false, initialized = true) }
                    } else {
                        _uiState.update { state ->
                            val merged = mergeById(
                                existing = if (refresh) emptyList() else state.animals,
                                incoming = data,
                                replace = refresh,
                            ) { it.id }
                            animalRepository.saveAnimalsCache(merged)
                            state.copy(
                                animals = merged,
                                filteredAnimals = applyFilter(merged, state.searchQuery),
                                hasMore = true,
                                isLoading = false,
                                initialized = true,
                                error = null,
                            )
                        }
                        offset = currentOffset + 1
                    }
                }
                .onFailure { error ->
                    if (currentOffset == 0) {
                        val cached = animalRepository.getCachedAnimals()
                        _uiState.update { state ->
                            state.copy(
                                animals = if (cached.isNotEmpty()) cached else state.animals,
                                filteredAnimals = applyFilter(
                                    if (cached.isNotEmpty()) cached else state.animals,
                                    state.searchQuery,
                                ),
                                error = if (cached.isNotEmpty()) {
                                    "Sem conexão. Exibindo dados salvos localmente."
                                } else {
                                    error.message ?: "Não foi possível carregar os dados."
                                },
                                isLoading = false,
                                initialized = true,
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                error = "Erro ao carregar mais itens.",
                                isLoading = false,
                            )
                        }
                    }
                }

            isFetching = false
        }
    }

    private fun applyFilter(animals: List<Animal>, query: String): List<Animal> {
        if (query.isBlank()) return animals
        return animals.filter { it.name.contains(query, ignoreCase = true) }
    }
}

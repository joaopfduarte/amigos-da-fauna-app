package com.app.amigos_da_fauna.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.amigos_da_fauna.ui.components.AnimalCard
import com.app.amigos_da_fauna.ui.components.ErrorBanner
import com.app.amigos_da_fauna.ui.theme.FaunaTheme
import com.app.amigos_da_fauna.util.ShareUtils

@Composable
fun HomeScreen(
    onAnimalClick: (Int) -> Unit,
    onQuizClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = FaunaTheme.colors
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= layoutInfo.totalItemsCount - 2
        }.collect { shouldLoadMore ->
            if (shouldLoadMore) viewModel.loadMore()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Animais da Mata Atlântica",
            color = colors.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )

        uiState.error?.let { error ->
            ErrorBanner(message = error, onRetry = { viewModel.refresh() })
        }

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar animal por nome...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { viewModel.onSearchSubmit() }),
        )

        if (uiState.searchHistory.isNotEmpty() && uiState.searchQuery.isBlank()) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.searchHistory.take(4).forEach { term ->
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.onSearchQueryChange(term) },
                        label = { Text(term) },
                    )
                }
            }
        }

        if (!uiState.initialized && uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp),
                color = colors.primary,
            )
        } else if (uiState.filteredAnimals.isEmpty() && !uiState.isLoading) {
            Text(
                text = if (uiState.searchQuery.isNotBlank()) {
                    "Nenhum animal encontrado para \"${uiState.searchQuery}\""
                } else {
                    "Nenhum animal disponível no momento."
                },
                color = colors.textMuted,
                modifier = Modifier.padding(top = 24.dp),
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(uiState.filteredAnimals, key = { it.id }) { animal ->
                    AnimalCard(
                        animal = animal,
                        onClick = { onAnimalClick(animal.id) },
                        onQuizClick = { onQuizClick(animal.id) },
                        onShareClick = { ShareUtils.shareAnimal(context, animal) },
                    )
                }
                if (uiState.isLoading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            color = colors.primary,
                        )
                    }
                }
            }
        }
    }
}

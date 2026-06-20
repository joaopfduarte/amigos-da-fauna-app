package com.app.amigos_da_fauna.ui.screen.detail

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.app.amigos_da_fauna.ui.components.ErrorBanner
import com.app.amigos_da_fauna.ui.theme.FaunaTheme
import com.app.amigos_da_fauna.util.ShareUtils

@Composable
fun AnimalDetailScreen(
    onQuizClick: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnimalDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = FaunaTheme.colors
    val context = LocalContext.current

    when {
        uiState.isLoading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            ) {
                CircularProgressIndicator(color = colors.primary)
                Text("Carregando animal…", modifier = Modifier.padding(top = 12.dp))
            }
        }

        uiState.error != null || uiState.animal == null -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            ) {
                ErrorBanner(
                    message = uiState.error ?: "Animal não encontrado.",
                    onRetry = { viewModel.loadAnimal() },
                )
                Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Voltar")
                }
            }
        }

        else -> {
            val animal = uiState.animal!!
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                AsyncImage(
                    model = animal.imageUrl,
                    contentDescription = animal.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = animal.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colors.card),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        StatRow("Altura", animal.height)
                        StatRow("Peso", animal.weight)
                    }
                }
                Button(
                    onClick = { onQuizClick(animal.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                ) {
                    Text("Realizar Quiz")
                }
                OutlinedButton(
                    onClick = { ShareUtils.shareAnimal(context, animal) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                ) {
                    Text("Compartilhar", color = colors.primary)
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    val colors = FaunaTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
    ) {
        Text(label.uppercase(), color = colors.textMuted, fontWeight = FontWeight.Bold)
        Text(value, color = colors.text, fontWeight = FontWeight.SemiBold)
    }
}

package com.app.amigos_da_fauna.ui.screen.quiz

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.amigos_da_fauna.ui.components.ErrorBanner
import com.app.amigos_da_fauna.ui.theme.FaunaTheme

@Composable
fun QuizScreen(
    onNavigateHome: () -> Unit,
    onNavigateProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = FaunaTheme.colors
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is QuizEvent.NavigateToProfile -> onNavigateProfile()
                is QuizEvent.NavigateHome -> onNavigateHome()
                is QuizEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    when {
        uiState.isLoading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(color = colors.primary)
                Text("Carregando quiz...", modifier = Modifier.padding(top = 16.dp))
            }
        }

        uiState.error != null && !uiState.requiresLogin -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Erro", color = colors.error, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(uiState.error ?: "", textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 12.dp))
                Button(onClick = { viewModel.loadQuestions() }) { Text("Tentar novamente") }
                Button(onClick = onNavigateHome, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Voltar para Home")
                }
            }
        }

        uiState.questions.isEmpty() -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Nenhuma pergunta encontrada", color = colors.brown, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Aguarde novas perguntas.", color = colors.textSecondary)
                Button(onClick = onNavigateHome, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Voltar para Home")
                }
            }
        }

        uiState.finished -> {
            QuizResultContent(
                percentage = viewModel.hitPercentage(),
                hits = uiState.hits,
                fails = uiState.fails,
                onNavigateHome = onNavigateHome,
            )
        }

        else -> {
            val question = uiState.questions[uiState.currentIndex]
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("É hora de praticar!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = colors.brown)
                CounterText(uiState)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.card),
                    border = androidx.compose.foundation.BorderStroke(2.dp, colors.brown),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Pergunta ${uiState.currentIndex + 1} de ${uiState.questions.size}",
                            color = colors.accent,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = question.statement,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.brown,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                        )
                        question.parsedOptions.forEach { option ->
                            OptionButton(
                                text = option.value,
                                isSelected = uiState.selectedOptionId == option.id,
                                answerResult = uiState.answerResult,
                                enabled = !uiState.isSubmitting && uiState.answerResult == null,
                                onClick = { viewModel.submitAnswer(option.id) },
                            )
                        }
                    }
                }

                if (uiState.answerDetails.isNotBlank()) {
                    Text(
                        text = uiState.answerDetails,
                        color = colors.brown,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun CounterText(uiState: QuizUiState) {
    val colors = FaunaTheme.colors
    val message = when {
        uiState.counter <= 0 -> ""
        uiState.currentIndex == uiState.questions.lastIndex -> "Resultados em ${uiState.counter}"
        else -> "Próxima pergunta em ${uiState.counter}"
    }
    if (message.isNotBlank()) {
        Text(
            text = message,
            color = colors.brown,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun OptionButton(
    text: String,
    isSelected: Boolean,
    answerResult: AnswerResult?,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val colors = FaunaTheme.colors
    val background = when {
        isSelected && answerResult == AnswerResult.CORRECT -> colors.primary
        isSelected && answerResult == AnswerResult.INCORRECT -> colors.accent
        else -> colors.card
    }
    val contentColor = if (isSelected && answerResult != null) colors.white else colors.text

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            containerColor = background,
            contentColor = contentColor,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
            if (isSelected && answerResult == AnswerResult.CORRECT) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = colors.white)
            }
            if (isSelected && answerResult == AnswerResult.INCORRECT) {
                Icon(Icons.Default.Close, contentDescription = null, tint = colors.white)
            }
        }
    }
}

@Composable
private fun QuizResultContent(
    percentage: Int,
    hits: Int,
    fails: Int,
    onNavigateHome: () -> Unit,
) {
    val colors = FaunaTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Resultado do Quiz", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = colors.brown)
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 24.dp)) {
            CircularProgressChart(percentage = percentage)
            Text("$percentage%", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = colors.brown)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ResultTag(text = "Acertos: $hits", color = colors.success)
            ResultTag(text = "Erros: $fails", color = colors.accent)
        }
        Text(
            text = if (percentage >= 70) {
                "Ótimo trabalho! Você conhece bem nossa fauna!"
            } else {
                "Continue aprendendo sobre nossos animais!"
            },
            textAlign = TextAlign.Center,
            color = colors.brown,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 24.dp),
        )
        Button(onClick = onNavigateHome) { Text("Voltar para Home") }
    }
}

@Composable
private fun CircularProgressChart(percentage: Int) {
    val colors = FaunaTheme.colors
    Canvas(modifier = Modifier.size(180.dp)) {
        val stroke = 16f
        drawArc(
            color = colors.border,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
        drawArc(
            color = colors.primary,
            startAngle = -90f,
            sweepAngle = 360f * (percentage / 100f),
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

@Composable
private fun ResultTag(text: String, color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            color = FaunaTheme.colors.white,
            fontWeight = FontWeight.Bold,
        )
    }
}

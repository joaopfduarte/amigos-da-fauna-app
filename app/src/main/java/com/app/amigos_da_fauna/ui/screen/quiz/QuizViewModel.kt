package com.app.amigos_da_fauna.ui.screen.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.amigos_da_fauna.domain.model.QuizAnswerResult
import com.app.amigos_da_fauna.domain.model.QuizQuestion
import com.app.amigos_da_fauna.domain.model.QuizResultEntry
import com.app.amigos_da_fauna.domain.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.Instant
import javax.inject.Inject

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val hits: Int = 0,
    val fails: Int = 0,
    val finished: Boolean = false,
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val requiresLogin: Boolean = false,
    val selectedOptionId: Int? = null,
    val answerResult: AnswerResult? = null,
    val counter: Int = -1,
    val answerDetails: String = "",
)

enum class AnswerResult { CORRECT, INCORRECT }

sealed class QuizEvent {
    data object NavigateToProfile : QuizEvent()
    data object NavigateHome : QuizEvent()
    data class ShowToast(val message: String) : QuizEvent()
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val quizRepository: QuizRepository,
) : ViewModel() {

    private val animalId: Int = savedStateHandle.get<String>("animalId")?.toIntOrNull() ?: -1

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<QuizEvent>()
    val events: SharedFlow<QuizEvent> = _events.asSharedFlow()

    private var resultSaved = false

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        if (animalId <= 0) {
            _uiState.update { it.copy(isLoading = false, error = "Animal inválido.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, requiresLogin = false) }
            quizRepository.getQuestions(animalId)
                .onSuccess { questions ->
                    _uiState.update {
                        it.copy(questions = questions, isLoading = false, error = null)
                    }
                }
                .onFailure { error ->
                    val httpCode = (error as? HttpException)?.code()
                    if (httpCode == 401 || httpCode == 422) {
                        _uiState.update {
                            it.copy(isLoading = false, requiresLogin = true, error = null)
                        }
                        _events.emit(QuizEvent.ShowToast("É necessário fazer login para jogar o quiz."))
                        _events.emit(QuizEvent.NavigateToProfile)
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Erro ao carregar quiz.",
                            )
                        }
                    }
                }
        }
    }

    fun submitAnswer(optionId: Int) {
        val state = _uiState.value
        if (state.isSubmitting || state.answerResult != null || state.finished) return
        val question = state.questions.getOrNull(state.currentIndex) ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(selectedOptionId = optionId, isSubmitting = true) }
            var timeout = 4000L

            quizRepository.submitAnswer(question.code, optionId)
                .onSuccess { response ->
                    handleAnswerResponse(response, timeout)
                }
                .onFailure {
                    _uiState.update { s ->
                        s.copy(
                            fails = s.fails + 1,
                            answerResult = AnswerResult.INCORRECT,
                        )
                    }
                    _events.emit(QuizEvent.ShowToast("Erro ao enviar resposta. Tente novamente."))
                    startCountdownAndAdvance(timeout)
                }
        }
    }

    private suspend fun handleAnswerResponse(response: QuizAnswerResult, baseTimeout: Long) {
        var timeout = baseTimeout
        if (response.isAnswerRight) {
            _uiState.update { it.copy(hits = it.hits + 1, answerResult = AnswerResult.CORRECT) }
        } else {
            timeout = 10000L
            _uiState.update {
                it.copy(
                    fails = it.fails + 1,
                    answerResult = AnswerResult.INCORRECT,
                    answerDetails = response.answerDetails?.let { details ->
                        "Detalhes da resposta: $details"
                    } ?: "",
                )
            }
        }
        startCountdownAndAdvance(timeout)
    }

    private suspend fun startCountdownAndAdvance(timeout: Long) {
        val seconds = (timeout / 1000).toInt()
        for (i in seconds downTo 1) {
            _uiState.update { it.copy(counter = i) }
            delay(1000)
        }
        _uiState.update { it.copy(counter = 0) }
        delay(300)
        advanceQuestion()
    }

    private suspend fun advanceQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.lastIndex) {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedOptionId = null,
                    answerResult = null,
                    isSubmitting = false,
                    answerDetails = "",
                    counter = -1,
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    finished = true,
                    isSubmitting = false,
                    counter = -1,
                )
            }
            saveResultIfNeeded()
        }
    }

    private suspend fun saveResultIfNeeded() {
        if (resultSaved) return
        val state = _uiState.value
        val total = state.hits + state.fails
        val percentage = if (total > 0) ((state.hits.toFloat() / total) * 100).toInt() else 0
        quizRepository.saveQuizResult(
            QuizResultEntry(
                animalId = animalId,
                hits = state.hits,
                fails = state.fails,
                percentage = percentage,
                date = Instant.now().toString(),
            ),
        )
        resultSaved = true
    }

    fun hitPercentage(): Int {
        val state = _uiState.value
        val total = state.hits + state.fails
        return if (total > 0) ((state.hits.toFloat() / total) * 100).toInt() else 0
    }
}

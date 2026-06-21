package com.app.amigos_da_fauna

import com.app.amigos_da_fauna.data.remote.dto.LoginRequestDto
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerRequestDto
import com.app.amigos_da_fauna.data.remote.mock.MockData
import com.app.amigos_da_fauna.data.remote.mock.MockFaunaApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MockFaunaApiTest {

    private lateinit var mockFaunaApi: MockFaunaApi

    @Before
    fun setUp() {
        mockFaunaApi = MockFaunaApi()
    }

    @Test
    fun getAnimals_returnsPagedResults() = runBlocking {
        val page0 = mockFaunaApi.getAnimals(0)
        val page1 = mockFaunaApi.getAnimals(1)
        val page2 = mockFaunaApi.getAnimals(2)

        assertEquals(MockData.PAGE_SIZE, page0.size)
        assertEquals(MockData.PAGE_SIZE, page1.size)
        assertTrue(page2.isEmpty())
    }

    @Test
    fun getAnimalById_returnsAnimalForValidId() = runBlocking {
        val animal = mockFaunaApi.getAnimalById(1)

        assertEquals("Onça-pintada", animal.name)
    }

    @Test(expected = NoSuchElementException::class)
    fun getAnimalById_throwsForInvalidId() {
        runBlocking {
            mockFaunaApi.getAnimalById(999)
        }
    }

    @Test
    fun login_succeedsWithMockCredentials() = runBlocking {
        val response = mockFaunaApi.login(
            LoginRequestDto(MockData.MOCK_EMAIL, MockData.MOCK_PASSWORD),
        )

        assertEquals(MockData.MOCK_ACCESS_TOKEN, response.accessToken)
    }

    @Test(expected = IllegalArgumentException::class)
    fun login_failsWithInvalidCredentials() {
        runBlocking {
            mockFaunaApi.login(LoginRequestDto("wrong@test.com", "wrong"))
        }
    }

    @Test
    fun getQuizQuestions_returnsQuestionsForKnownAnimal() = runBlocking {
        val questions = mockFaunaApi.getQuizQuestions(1)

        assertTrue(questions.isNotEmpty())
        assertEquals(101, questions.first().code)
    }

    @Test
    fun submitQuizAnswer_marksCorrectAnswerAsRight() = runBlocking {
        val result = mockFaunaApi.submitQuizAnswer(
            QuizAnswerRequestDto(questionCode = 101, userAnswer = 1),
        )

        assertTrue(result.isAnswerRight)
    }

    @Test
    fun submitQuizAnswer_marksWrongAnswerAsIncorrect() = runBlocking {
        val result = mockFaunaApi.submitQuizAnswer(
            QuizAnswerRequestDto(questionCode = 101, userAnswer = 2),
        )

        assertFalse(result.isAnswerRight)
    }
}

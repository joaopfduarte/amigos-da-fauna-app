package com.app.amigos_da_fauna.data.remote

import com.app.amigos_da_fauna.data.remote.dto.AnimalDto
import com.app.amigos_da_fauna.data.remote.dto.AnimalLocationDto
import com.app.amigos_da_fauna.data.remote.dto.LoginRequestDto
import com.app.amigos_da_fauna.data.remote.dto.LoginResponseDto
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerRequestDto
import com.app.amigos_da_fauna.data.remote.dto.QuizAnswerResponseDto
import com.app.amigos_da_fauna.data.remote.dto.QuizQuestionDto
import com.app.amigos_da_fauna.data.remote.dto.RegisterRequestDto
import com.app.amigos_da_fauna.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("animals")
    suspend fun getAnimals(@Query("offset") offset: Int): List<AnimalDto>

    @GET("animals/{id}")
    suspend fun getAnimalById(@Path("id") id: Int): AnimalDto

    @GET("animals/location")
    suspend fun getAnimalLocations(@Query("offset") offset: Int): List<AnimalLocationDto>

    @POST("user/login")
    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto

    @POST("user")
    suspend fun register(@Body body: RegisterRequestDto): UserDto

    @GET("quiz/list")
    suspend fun getQuizQuestions(@Query("animalId") animalId: Int): List<QuizQuestionDto>

    @POST("quiz/answer")
    suspend fun submitQuizAnswer(@Body body: QuizAnswerRequestDto): QuizAnswerResponseDto
}

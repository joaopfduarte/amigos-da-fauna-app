package com.app.amigos_da_fauna.di

import com.app.amigos_da_fauna.data.repository.AnimalRepositoryImpl
import com.app.amigos_da_fauna.data.repository.AuthRepositoryImpl
import com.app.amigos_da_fauna.data.repository.QuizRepositoryImpl
import com.app.amigos_da_fauna.data.repository.ThemeRepositoryImpl
import com.app.amigos_da_fauna.domain.repository.AnimalRepository
import com.app.amigos_da_fauna.domain.repository.AuthRepository
import com.app.amigos_da_fauna.domain.repository.QuizRepository
import com.app.amigos_da_fauna.domain.repository.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnimalRepository(impl: AnimalRepositoryImpl): AnimalRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindQuizRepository(impl: QuizRepositoryImpl): QuizRepository

    @Binds
    @Singleton
    abstract fun bindThemeRepository(impl: ThemeRepositoryImpl): ThemeRepository
}

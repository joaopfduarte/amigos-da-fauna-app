package com.app.amigos_da_fauna.data.repository

import com.app.amigos_da_fauna.data.local.PreferencesDataStore
import com.app.amigos_da_fauna.data.remote.FaunaApi
import com.app.amigos_da_fauna.data.remote.dto.LoginRequestDto
import com.app.amigos_da_fauna.data.remote.dto.RegisterRequestDto
import com.app.amigos_da_fauna.data.remote.dto.toDomain
import com.app.amigos_da_fauna.data.remote.dto.toDto
import com.app.amigos_da_fauna.domain.model.Animal
import com.app.amigos_da_fauna.domain.model.AnimalLocation
import com.app.amigos_da_fauna.domain.repository.AnimalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalRepositoryImpl @Inject constructor(
    private val faunaApi: FaunaApi,
    private val preferencesDataStore: PreferencesDataStore,
) : AnimalRepository {

    override suspend fun getAnimalsPage(offset: Int): Result<List<Animal>> = runCatching {
        faunaApi.getAnimals(offset).map { it.toDomain() }
    }

    override suspend fun getAnimalById(id: Int): Result<Animal> = runCatching {
        faunaApi.getAnimalById(id).toDomain()
    }

    override suspend fun getLocationsPage(offset: Int): Result<List<AnimalLocation>> = runCatching {
        faunaApi.getAnimalLocations(offset).map { it.toDomain() }
    }

    override suspend fun getCachedAnimals(): List<Animal> {
        return preferencesDataStore.getAnimalsCache().map { it.toDomain() }
    }

    override suspend fun saveAnimalsCache(animals: List<Animal>) {
        preferencesDataStore.setAnimalsCache(animals.map { it.toDto() })
    }

    override suspend fun getSearchHistory(): List<String> = preferencesDataStore.getSearchHistory()

    override suspend fun addSearchTerm(term: String) = preferencesDataStore.addSearchTerm(term)
}

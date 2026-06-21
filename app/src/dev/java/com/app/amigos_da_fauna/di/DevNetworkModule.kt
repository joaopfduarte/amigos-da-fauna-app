package com.app.amigos_da_fauna.di

import com.app.amigos_da_fauna.data.remote.FaunaApi
import com.app.amigos_da_fauna.data.remote.mock.MockFaunaApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DevNetworkModule {

    @Binds
    @Singleton
    abstract fun bindFaunaApi(impl: MockFaunaApi): FaunaApi
}

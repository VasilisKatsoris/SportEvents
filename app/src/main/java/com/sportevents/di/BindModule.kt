package com.sportevents.di

import com.sportevents.network.functionality.RepositoryImpl
import com.sportevents.network.functionality.RepositoryInterface
import com.sportevents.storage.functionality.SportEventsLocalSource
import com.sportevents.storage.functionality.SportEventsLocalSourceImpl
import com.sportevents.util.DispatcherProvider
import com.sportevents.util.DispatchersProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun repository(impl: RepositoryImpl): RepositoryInterface

    @Binds
    fun dispatchers(impl: DispatchersProviderImpl): DispatcherProvider

    @Binds
    fun sportsLocalSource(impl: SportEventsLocalSourceImpl): SportEventsLocalSource

}
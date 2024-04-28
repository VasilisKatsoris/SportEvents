package com.sportevents.di

import android.content.Context
import com.sportevents.network.ApiCalls
import com.sportevents.network.RetrofitBuilder
import com.sportevents.storage.database.SportEventsDao
import com.sportevents.storage.database.SportEventsDatabase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ProvidersModule {

    @Reusable
    @Provides
    fun api(retrofitBuilder: RetrofitBuilder): ApiCalls = retrofitBuilder.apiCalls

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SportEventsDatabase =
        SportEventsDatabase.getInstance(context)

    @Provides
    fun provideDao(database: SportEventsDatabase): SportEventsDao = database.sportEventDao()
}
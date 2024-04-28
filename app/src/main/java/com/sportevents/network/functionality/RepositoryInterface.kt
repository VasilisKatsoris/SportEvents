package com.sportevents.network.functionality

import com.sportevents.data.remote.Sport
import com.sportevents.network.util.LoadingEvent
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    fun getSportEvents(): Flow<LoadingEvent<ArrayList<Sport>>>
}
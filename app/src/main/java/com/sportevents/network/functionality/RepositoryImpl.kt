package com.sportevents.network.functionality

import com.sportevents.data.remote.Sport
import com.sportevents.network.ApiCalls
import com.sportevents.network.util.LoadingEvent
import com.sportevents.network.util.statefulApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiCalls: ApiCalls
) : RepositoryInterface {

    override fun getSportEvents(): Flow<LoadingEvent<ArrayList<Sport>>> {
        return statefulApiCall { apiCalls.getSportEvents() }
    }

}
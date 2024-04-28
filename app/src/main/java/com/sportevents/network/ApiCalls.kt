package com.sportevents.network

import com.sportevents.data.remote.Sport
import retrofit2.Response
import retrofit2.http.GET

interface ApiCalls {

    @GET("api/sports")
    suspend fun getSportEvents(): Response<ArrayList<Sport>>

}
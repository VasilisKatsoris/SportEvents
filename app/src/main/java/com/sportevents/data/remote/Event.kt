package com.sportevents.data.remote

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("i") val id: String,
    @SerializedName("d") val eventName: String,
    @SerializedName("si") val sportId: String,
    @SerializedName("tt") val startTime: Long
)
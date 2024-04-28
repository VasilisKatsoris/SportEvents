package com.sportevents.data.remote

import com.google.gson.annotations.SerializedName


data class Sport(
    @SerializedName("i") val id: String,
    @SerializedName("d") val sportName: String,
    @SerializedName("e") val events: List<Event>
)


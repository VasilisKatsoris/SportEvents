package com.sportevents.storage.database

import androidx.room.Embedded
import androidx.room.Relation
import com.sportevents.data.local.EventLocal
import com.sportevents.data.local.SportLocal

data class SportsWithEventsLocal(
    @Embedded
    val sport: SportLocal,
    @Relation(parentColumn = "id", entityColumn = "sportId")
    val events: List<EventLocal>
)
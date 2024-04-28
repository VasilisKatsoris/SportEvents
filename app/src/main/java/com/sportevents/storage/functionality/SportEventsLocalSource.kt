package com.sportevents.storage.functionality

import com.sportevents.data.local.EventLocal
import com.sportevents.data.local.SportLocal
import com.sportevents.storage.database.SportsWithEventsLocal
import kotlinx.coroutines.flow.Flow

interface SportEventsLocalSource {
    fun getAll(): Flow<List<SportsWithEventsLocal>>
    suspend fun getEvent(id: String): EventLocal?
    suspend fun getSport(id: String): SportLocal?
    suspend fun deleteSportEvents(sportEvents: List<SportLocal>)
    suspend fun insertSportEvent(sportsWithEventsLocal: SportLocal)
    suspend fun updateSportEvent(sportsWithEventsLocal: SportLocal): Int
    suspend fun deleteEvents(sportsWithEventsLocal: List<EventLocal>)
    suspend fun insertEvent(sportsWithEventsLocal: EventLocal)
    suspend fun updateEvent(sportsWithEventsLocal: EventLocal): Int
    suspend fun insertSportEvents(sportsWithEventsLocal: List<SportLocal>)
    suspend fun insertEvents(sportsWithEventsLocal: List<EventLocal>)
    suspend fun clearDb()

}
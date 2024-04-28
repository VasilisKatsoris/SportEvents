package com.sportevents.storage.functionality

import com.sportevents.data.local.EventLocal
import com.sportevents.data.local.SportLocal
import com.sportevents.storage.database.SportEventsDao
import com.sportevents.storage.database.SportsWithEventsLocal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SportEventsLocalSourceImpl @Inject constructor(
    private val sportEventsDao: SportEventsDao
) : SportEventsLocalSource {
    override fun getAll(): Flow<List<SportsWithEventsLocal>> {
        return sportEventsDao.getAll()
    }

    override suspend fun getEvent(id: String): EventLocal? {
        return sportEventsDao.getEvent(id)
    }

    override suspend fun getSport(id: String): SportLocal? {
        return sportEventsDao.getSport(id)
    }

    override suspend fun deleteSportEvents(sportsWithEventsLocal: List<SportLocal>) {
        sportEventsDao.deleteSportEvents(sportsWithEventsLocal)
    }

    override suspend fun deleteEvents(sportsWithEventsLocal: List<EventLocal>) {
        sportEventsDao.deleteEvents(sportsWithEventsLocal)
    }

    override suspend fun insertSportEvent(sportsWithEventsLocal: SportLocal) {
        sportEventsDao.insertSportEvent(sportsWithEventsLocal)
    }

    override suspend fun insertEvent(sportsWithEventsLocal: EventLocal) {
        sportEventsDao.insertEvent(sportsWithEventsLocal)
    }

    override suspend fun updateSportEvent(sportsWithEventsLocal: SportLocal): Int {
        return sportEventsDao.updateSportEvent(sportsWithEventsLocal)
    }

    override suspend fun updateEvent(sportsWithEventsLocal: EventLocal): Int {
        return sportEventsDao.updateEvent(sportsWithEventsLocal)
    }

    override suspend fun insertSportEvents(sportsWithEventsLocal: List<SportLocal>) {
        sportEventsDao.insertSportEvents(sportsWithEventsLocal)
    }

    override suspend fun insertEvents(sportsWithEventsLocal: List<EventLocal>) {
        sportEventsDao.insertEvents(sportsWithEventsLocal)
    }

    override suspend fun clearDb() {
        sportEventsDao.clearSports()
        sportEventsDao.clearEvents()
    }

}
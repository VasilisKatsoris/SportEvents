package com.sportevents.ui.events

import com.sportevents.data.local.EventLocal
import com.sportevents.data.local.SportLocal
import com.sportevents.storage.functionality.SportEventsLocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SportEventsLocalUseCase @Inject constructor(
    private val localSource: SportEventsLocalSource
) {

    fun getAll(): Flow<List<SportListItem>> {

        return localSource.getAll().map { sportsWithEventsLocal ->
            sportsWithEventsLocal.flatMap { sport ->

                val sportHeader = sport.sport.toSportHeader()

                val sportEvents: List<SportListItem> = sport.events.map { event ->
                    event.toListEvent()
                }.filter {
                    sportHeader.expanded && (!sportHeader.filterFavorites || it.favorite)
                }

                listOf(sportHeader) + sportEvents
            }
        }
    }

    suspend fun updateEventWithId(id: String, process: (EventLocal) -> Unit) {
        localSource.getEvent(id)?.let {
            process(it)
            localSource.updateEvent(it)
        }
    }

    suspend fun updateSportWithId(id: String, process: (SportLocal) -> Unit) {
        localSource.getSport(id)?.let {
            process(it)
            localSource.updateSportEvent(it)
        }
    }

    suspend fun clearDb() {
        localSource.clearDb()
    }

}
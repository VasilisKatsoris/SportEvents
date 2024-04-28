package com.sportevents.ui.events

import com.sportevents.data.local.toLocalEvent
import com.sportevents.data.local.toLocalSportEvent
import com.sportevents.data.remote.Sport
import com.sportevents.network.functionality.RepositoryInterface
import com.sportevents.network.util.LoadingEvent
import com.sportevents.storage.functionality.SportEventsLocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SportEventsNetworkUseCase @Inject constructor(
    private val repository: RepositoryInterface,
    private val localSource: SportEventsLocalSource
) {
    fun getSportEvents(): Flow<LoadingEvent<List<Sport>>> {
        return repository.getSportEvents().onEach {
            //handle db saving in use case to keep view model clean
            if (it is LoadingEvent.Success) {
                val sportEventsLocal = it.data.map {
                    it.toLocalSportEvent()
                }

                val eventsLocal = it.data.flatMap { it.events }.map {
                    it.toLocalEvent()
                }

                localSource.insertSportEvents(sportEventsLocal)
                localSource.insertEvents(eventsLocal)
            }
        }
    }
}
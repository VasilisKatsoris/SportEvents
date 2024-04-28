package com.sportevents.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportevents.network.util.ErrorReason
import com.sportevents.network.util.LoadingEvent
import com.sportevents.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SportEventsViewModel @Inject constructor(
    private val eventsNetworkUseCase: SportEventsNetworkUseCase,
    private val eventsLocalUseCase: SportEventsLocalUseCase,
    private val dispatchersProvider: DispatcherProvider
) : ViewModel() {

    val isLoading = MutableStateFlow(false)
    val error = MutableStateFlow<ErrorReason?>(null)
    private var fetchFromNetworkIfEmpty = true

    //observe database changes
    val sportEvents = eventsLocalUseCase.getAll().flowOn(dispatchersProvider.io).onEach {
        //if database is empty fetch data from network
        fetchFromNetworkIfEmpty = fetchFromNetworkIfEmpty && it.isEmpty()
        if (it.isEmpty() && fetchFromNetworkIfEmpty) {
            fetchFromNetwork()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    fun refresh() {
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        viewModelScope.launch(dispatchersProvider.io) {
            eventsNetworkUseCase.getSportEvents().collect {
                isLoading.value = it is LoadingEvent.Loading
                error.value = if (it is LoadingEvent.Error) it.reason else null
                //data automatically saved to DB and shown to UI via the database observer
            }
        }
    }

    fun clearDb() {
        viewModelScope.launch(dispatchersProvider.io) {
            eventsLocalUseCase.clearDb()
        }
    }

    fun onFavoriteClicked(it: SportListItem.SportGridItem) {
        viewModelScope.launch(dispatchersProvider.io) {
            eventsLocalUseCase.updateEventWithId(it.id) {
                it.favorite = !it.favorite
            }
        }
    }

    fun onFilterFavoriteClicked(sportHeader: SportListItem.SportHeader) {
        viewModelScope.launch(dispatchersProvider.io) {
            eventsLocalUseCase.updateSportWithId(sportHeader.id) {
                it.filterFavorites = !it.filterFavorites
            }
        }
    }

    fun onExpandToggled(sportHeader: SportListItem.SportHeader) {
        viewModelScope.launch(dispatchersProvider.io) {
            eventsLocalUseCase.updateSportWithId(sportHeader.id) {
                it.expanded = !it.expanded
            }
        }
    }

}
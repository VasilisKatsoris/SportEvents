package com.sportevents.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

interface Refreshable<T : Any> {

    val flow: Flow<T>

    fun refresh()
}

/**
 * A wrapper that contains a flow of values and a method to refresh these values.
 *
 * @param createFlow Receives the trigger flow that must be used
 */
private class RefreshableImpl<T : Any>(
    createFlow: (refreshTrigger: Flow<Unit>) -> Flow<T>
) : Refreshable<T> {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply {
        // This emits, but has no effect until someone actually starts collecting
        tryEmit(Unit)
    }

    override val flow = createFlow(refreshTrigger).distinctUntilChanged()

    override fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }
}

fun <T : Any> refreshable(
    createFlow: (refreshTrigger: Flow<Unit>) -> Flow<T>
): Refreshable<T> = RefreshableImpl { refreshTrigger ->
    createFlow(refreshTrigger)
}
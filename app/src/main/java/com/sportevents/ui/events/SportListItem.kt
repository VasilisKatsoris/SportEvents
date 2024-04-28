package com.sportevents.ui.events

import com.sportevents.data.local.EventLocal
import com.sportevents.data.local.SportLocal

//used for showing data to the UI so that both events and
//their containers can be processed as one list of data
sealed class SportListItem() {
    abstract val id: String

    data class SportHeader(
        val name: String,
        override val id: String,
        val expanded: Boolean,
        val filterFavorites: Boolean
    ) : SportListItem()

    data class SportGridItem(
        val team1: String?,
        val team2: String?,
        override val id: String,
        val startTime: Long,
        val sportId: String,
        val favorite: Boolean
    ) : SportListItem()
}

fun SportLocal.toSportHeader(): SportListItem.SportHeader {
    return SportListItem.SportHeader(
        name = this.sportName,
        id = this.id,
        expanded = this.expanded,
        filterFavorites = this.filterFavorites
    )

}

fun EventLocal.toListEvent(): SportListItem.SportGridItem {
    val parts = this.eventName.split("-")
    return SportListItem.SportGridItem(
        team1 = parts.getOrNull(0)?.trim(),
        team2 = parts.getOrNull(1)?.trim(),
        id = this.id,
        startTime = this.startTime,
        sportId = this.sportId,
        favorite = this.favorite
    )
}
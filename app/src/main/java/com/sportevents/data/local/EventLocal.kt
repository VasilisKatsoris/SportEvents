package com.sportevents.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sportevents.data.remote.Event


@Entity(
    tableName = "events",
    foreignKeys = [ForeignKey(
        entity = SportLocal::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("sportId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class EventLocal(
    @PrimaryKey val id: String,
    val eventName: String,
    @ColumnInfo(name = "sportId") val sportId: String,
    val startTime: Long,
    var favorite: Boolean
)

fun Event.toLocalEvent(): EventLocal {
    return EventLocal(
        id = this.id,
        eventName = this.eventName,
        sportId = this.sportId,
        startTime = this.startTime,
        favorite = false
    )
}
package com.sportevents.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sportevents.data.remote.Sport

@Entity(tableName = "sports")
data class SportLocal(
    @ColumnInfo(name = "id") @PrimaryKey val id: String,
    val sportName: String,
    var expanded: Boolean,
    var filterFavorites: Boolean
)


fun Sport.toLocalSportEvent(): SportLocal {
    return SportLocal(
        id = this.id,
        sportName = this.sportName,
        expanded = true,
        filterFavorites = false
    )
}


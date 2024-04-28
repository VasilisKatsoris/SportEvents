package com.sportevents.storage.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sportevents.data.local.EventLocal
import com.sportevents.data.local.SportLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface SportEventsDao {

    @Transaction
    @Query(" SELECT * FROM sports")
    fun getAll(): Flow<List<SportsWithEventsLocal>>

    @Transaction
    @Query(" SELECT * FROM events WHERE id == :lookUpId")
    suspend fun getEvent(lookUpId: String): EventLocal?

    @Transaction
    @Query(" SELECT * FROM sports WHERE id == :lookUpId")
    suspend fun getSport(lookUpId: String): SportLocal?

    @Delete
    suspend fun deleteSportEvents(sportEvents: List<SportLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSportEvent(sportsWithEventsLocal: SportLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSportEvents(sportsWithEventsLocal: List<SportLocal>)

    @Update
    suspend fun updateSportEvent(sportsWithEventsLocal: SportLocal): Int

    @Delete
    suspend fun deleteEvents(sportsWithEventsLocal: List<EventLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(sportsWithEventsLocal: EventLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(sportsWithEventsLocal: List<EventLocal>)

    @Update
    suspend fun updateEvent(sportsWithEventsLocal: EventLocal): Int

    @Query("DELETE FROM sports")
    suspend fun clearSports()

    @Query("DELETE FROM events")
    suspend fun clearEvents()
}
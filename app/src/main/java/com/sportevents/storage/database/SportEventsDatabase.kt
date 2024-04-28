package com.sportevents.storage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sportevents.data.local.EventLocal
import com.sportevents.data.local.SportLocal

@Database(
    entities = [SportLocal::class, EventLocal::class],
    version = 1,
    exportSchema = false
)
abstract class SportEventsDatabase : RoomDatabase() {

    abstract fun sportEventDao(): SportEventsDao

    companion object {
        @Volatile
        private var INSTANCE: SportEventsDatabase? = null

        fun getInstance(context: Context): SportEventsDatabase {
            if (INSTANCE == null) {
                synchronized(SportEventsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            SportEventsDatabase::class.java,
                            "sport_events_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
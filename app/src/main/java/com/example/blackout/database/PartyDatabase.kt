package com.example.blackout.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Party::class, PartyImage::class], version = 5,  exportSchema = false)
abstract class PartyDatabase : RoomDatabase() {

    abstract val partyDatabaseDao: PartyDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: PartyDatabase? = null

        fun getInstance(context: Context): PartyDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PartyDatabase::class.java,
                        "party_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
package com.example.blackout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PartyDatabaseDao {

    @Insert
    fun insert(party: Party)

    @Update
    fun update(party: Party)

    @Query("SELECT * from party_table WHERE partyId = :key")
    fun get(key: Long): Party?

    @Query("DELETE FROM party_table")
    fun clear()

    @Query("SELECT * FROM party_table ORDER BY partyId DESC")
    fun getAllParties(): LiveData<List<Party>>

    @Query("SELECT * FROM party_table ORDER BY time_milli DESC LIMIT 1")
    fun getLatestParty(): Party?

    @Query("SELECT * FROM image_table WHERE party_id = :key ORDER BY time_taken_milli DESC")
    fun getImagesFromParty(key: Long): List<PartyImage>

    @Insert
    fun insert(image: PartyImage)

    @Query("SELECT MAX(score) from party_table")
    fun getMaxScore(): Double

    @Query("SELECT * FROM party_table WHERE time_milli >= :start AND time_milli < :stop")
    fun getPartiesBetweenDates(start: Long, stop: Long): List<Party>

    @Query("SELECT * FROM party_table ORDER BY time_milli ASC")
    fun getAllPartiesForCharts(): List<Party>
}
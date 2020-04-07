package com.example.blackout.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.blackout.utils.timeMilliFormatter

@Entity(tableName = "party_table")
data class Party(

    @PrimaryKey(autoGenerate = true)
    var partyId: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String = timeMilliFormatter(System.currentTimeMillis()),

    @ColumnInfo(name = "time_milli")
    val timeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "beer_count")
    var beerCount: Int = 0,

    @ColumnInfo(name = "wine_count")
    var wineCount: Int = 0,

    @ColumnInfo(name = "hard_count")
    var hardCount: Int = 0,

    @ColumnInfo(name = "score")
    var score: Double = 0.0

    )

@Entity(tableName = "image_table")
data class PartyImage(

    @PrimaryKey(autoGenerate = false)
    val uri: String,

    @ColumnInfo(name = "time_taken_milli")
    val timeTakenMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "party_id")
    val partyId: Long

)
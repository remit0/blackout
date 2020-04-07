package com.example.blackout.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blackout.database.PartyDatabaseDao
import com.example.blackout.viewmodels.PartyDetailViewModel
import java.lang.IllegalArgumentException

class PartyDetailViewModelFactory(private val database: PartyDatabaseDao,
                                  private val application: Application,
                                  private val partyId: Long)
    : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartyDetailViewModel::class.java)){
            return PartyDetailViewModel(
                database,
                application,
                partyId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}
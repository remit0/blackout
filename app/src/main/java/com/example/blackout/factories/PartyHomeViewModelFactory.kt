package com.example.blackout.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blackout.database.PartyDatabaseDao
import com.example.blackout.viewmodels.PartyHomeViewModel

class PartyHomeViewModelFactory(private val database: PartyDatabaseDao,
                            private val application: Application)
    : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartyHomeViewModel::class.java)){
            return PartyHomeViewModel(
                database,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}

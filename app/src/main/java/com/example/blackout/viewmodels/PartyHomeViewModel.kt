package com.example.blackout.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.blackout.database.PartyDatabaseDao

class PartyHomeViewModel(database: PartyDatabaseDao, application: Application):
    AndroidViewModel(application) {

    val parties = database.getAllParties()

}



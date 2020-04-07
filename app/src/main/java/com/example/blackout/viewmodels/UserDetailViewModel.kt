package com.example.blackout.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.blackout.R
import kotlinx.android.synthetic.main.fragment_user_detail.view.*


const val WEIGHT_KEY = "weight"
const val SEX_KEY = "sex"


class UserDetailViewModel(application: Application): AndroidViewModel(application) {

    private val app = application
    private val sharedPref = getSharedPrefHandle()

    private fun getSharedPrefHandle(): SharedPreferences {
        return app.getSharedPreferences("shared", Context.MODE_PRIVATE)
    }

    fun areDetailsValid(weightStr: String, chipId: Int): Boolean {
        return (weightStr.isNotEmpty() && (chipId > 0))
    }

    fun hasAlreadyInputDetails(): Boolean {
        val weight = sharedPref.getInt(WEIGHT_KEY, -1)
        return weight >= 0
    }

    fun saveUserDetail(weightStr: String, chipId: Int){
        with (sharedPref.edit()) {
            putInt(WEIGHT_KEY, weightStr.toInt())
            putString(SEX_KEY, if (chipId == R.id.m_chip) "M" else "F")
            commit()
        }
    }

    fun getWeight(): Int{
        return sharedPref.getInt(WEIGHT_KEY, -1)
    }

    fun getSex(): String? {
        return sharedPref.getString(SEX_KEY, "M")
    }
}

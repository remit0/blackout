package com.example.blackout.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun timeMilliFormatter(timeMilli: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(timeMilli))
}


fun Double.format(digits: Int) = "%.${digits}f".format(this)


fun formatAlcoholValue(alcohol_score: Double): String {
    return "${alcohol_score.format(1)} g/L"
}

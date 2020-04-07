package com.example.blackout.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.blackout.ui.MainActivity


const val PICTURE_INTENT_REQUEST_CODE = 1001


class PictureHandler(private val partyId: Long, private val context: Context,
                     private val activity: MainActivity) {

    lateinit var imageUri: Uri

    init {
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        getPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun getPermission(permission: String){
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), PICTURE_INTENT_REQUEST_CODE)
        }
    }

    fun getPictureIntent(): Intent {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "${partyId}_${System.currentTimeMillis()}.jpg")
        imageUri = activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        return cameraIntent
    }
}
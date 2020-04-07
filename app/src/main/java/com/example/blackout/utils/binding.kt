package com.example.blackout.utils

import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.blackout.database.Party
import com.example.blackout.database.PartyImage
import kotlin.math.min


@BindingAdapter("itemAlcText")
fun TextView.setItemAlcText(item: Party?) {
    item?.let {
        text = formatAlcoholValue(item.score)
    }
}

@BindingAdapter("itemNameText")
fun TextView.setItemNameText(item: Party?) {
    item?.let {
        text = "${item.name}"
    }
}

@BindingAdapter("imageView")
fun ImageView.setImage(item: PartyImage?) {
    item?.let {
        this.setImageURI(Uri.parse(item.uri))
    }
}

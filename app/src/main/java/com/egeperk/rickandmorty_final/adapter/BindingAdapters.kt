package com.egeperk.rickandmorty_final.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    imageView.load(url) { crossfade(true) }
}

@BindingAdapter("goneIf")
fun View.goneIf(value: Boolean?) {
    visibility = if (value == true) View.GONE else View.VISIBLE
}
package com.kks.nimbletest.util.extensions

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kks.nimbletest.R

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

fun Context.toast(msg: String, toastLength: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, msg,toastLength).show()

fun Context.loadImage(url: String?, view: ImageView) {
    val requestOptions = RequestOptions
        .placeholderOf(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .centerCrop()
    Glide.with(this)
        .setDefaultRequestOptions(requestOptions)
        .load(url)
        .into(view)
}
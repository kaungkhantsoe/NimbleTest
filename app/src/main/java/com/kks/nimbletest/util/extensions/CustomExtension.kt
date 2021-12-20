package com.kks.nimbletest.util.extensions

import android.content.Context
import android.widget.Toast

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

fun Context.toast(msg: String, toastLength: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, msg,toastLength).show()

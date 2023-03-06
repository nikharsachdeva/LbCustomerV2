package com.laundrybuoy.customer.utils

import android.content.Context

fun Context.convertDpToPixels(dp: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Long.getStandardizeCount(): String {
    return when {
        this < 1000 -> {
            this.toString()
        }
        this in 1000..999999 -> {
            (this / 1000).toString() + "K"
        }
        else -> {
            (this / 1000000).toString() + "M"
        }
    }
}



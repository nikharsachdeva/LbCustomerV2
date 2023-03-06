package com.laundrybuoy.customer.utils

import android.content.Context
import android.content.SharedPreferences
import com.laundrybuoy.customer.R

object SharedPreferenceManager {

    var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }


}
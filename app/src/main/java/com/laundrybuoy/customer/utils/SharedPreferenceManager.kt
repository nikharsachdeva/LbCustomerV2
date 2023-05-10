package com.laundrybuoy.customer.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.laundrybuoy.customer.R

object SharedPreferenceManager {

    private const val FIRST_TIME_OPEN = "FIRST_TIME_OPEN"
    private const val BEARER_TOKEN = "BEARER_TOKEN"
    var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    fun getBearerToken(): String? {
        return sharedPreferences?.getString(BEARER_TOKEN,null)
    }

    fun setBearerToken(value: String) {
        sharedPreferences?.edit {
            putString(
                BEARER_TOKEN,
                value
            )
        }
    }


    fun getFirstTimeStatus(): Boolean? {
        return sharedPreferences?.getBoolean(FIRST_TIME_OPEN,true)
    }

    fun setFirstTimeStatus(value: Boolean) {
        sharedPreferences?.edit {
            putBoolean(
                FIRST_TIME_OPEN,
                value
            )
        }
    }

}
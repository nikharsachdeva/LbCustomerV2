package com.laundrybuoy.customer

import android.app.Application
import com.laundrybuoy.customer.utils.SharedPreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomerApp : Application() {

    companion object {
        lateinit var instance: CustomerApp
        private const val TAG = "ApplicationClass"

        @JvmName("getInstance1")
        fun getInstance(): CustomerApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPreferenceManager.init(instance)
    }

}
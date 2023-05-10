package com.laundrybuoy.customer.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.gson.Gson
import java.io.IOException
import java.util.*

class GetAddrFromLatLog(
    context: Context,
    private val latitude: Double,
    private val longitude: Double,
) : Runnable {

    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

    private var addressListener: AddressListener? = null

    override fun run() {
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addressList.isNullOrEmpty()) {
                val address: Address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(",")
                }
                sb.deleteCharAt(sb.length - 1) // Here we remove the last comma that we have added above from the address.
                addressListener?.onAddressFound(sb.toString(),
                    address.adminArea,
                    address.postalCode,
                address.locality)
            } else {
                addressListener?.onError()
            }
        } catch (e: IOException) {
            Log.e("HappyPlaces", "Unable connect to Geocoder")
            addressListener?.onError()
        }
    }

    fun setAddressListener(callback: AddressListener?) {
        addressListener = callback
    }

    interface AddressListener {
        fun onAddressFound(address: String?, state: String?, pincode: String?, city: String?)
        fun onError()
    }
}

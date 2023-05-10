package com.laundrybuoy.customer.model.address


import com.google.gson.annotations.SerializedName

data class AddAddressPayload(
    @SerializedName("address")
    val address: Address?,
    @SerializedName("addressType")
    val addressType: String?,
    @SerializedName("altMobile")
    val altMobile: Long?
) {
    data class Address(
        @SerializedName("city")
        val city: String?,
        @SerializedName("landmark")
        val landmark: String?,
        @SerializedName("latitude")
        val latitude: String?,
        @SerializedName("line1")
        val line1: String?,
        @SerializedName("longitude")
        val longitude: String?,
        @SerializedName("pin")
        val pin: String?,
        @SerializedName("state")
        val state: String?
    )
}
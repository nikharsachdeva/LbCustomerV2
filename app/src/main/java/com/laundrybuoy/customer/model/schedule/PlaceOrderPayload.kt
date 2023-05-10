package com.laundrybuoy.customer.model.schedule


import com.google.gson.annotations.SerializedName

data class PlaceOrderPayload(
    @SerializedName("approxCloths")
    val approxCloths: String?,
    @SerializedName("deliveryAddress")
    val deliveryAddress: DeliveryAddress?,
    @SerializedName("isPrime")
    val isPrime: Boolean?,
    @SerializedName("pickupDate")
    val pickupDate: String?,
    @SerializedName("serviceId")
    val serviceId: String?,
    @SerializedName("userId")
    val userId: String?
) {
    data class DeliveryAddress(
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
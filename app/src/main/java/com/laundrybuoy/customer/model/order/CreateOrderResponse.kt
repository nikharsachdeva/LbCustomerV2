package com.laundrybuoy.customer.model.order


import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("sucess")
    val sucess: Boolean?
) {
    data class Data(
        @SerializedName("adminCreated")
        val adminCreated: Boolean?,
        @SerializedName("approxCloths")
        val approxCloths: String?,
        @SerializedName("bagIds")
        val bagIds: List<Any?>?,
        @SerializedName("billId")
        val billId: Any?,
        @SerializedName("couponId")
        val couponId: Any?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("deliveredOn")
        val deliveredOn: Any?,
        @SerializedName("deliveryAddress")
        val deliveryAddress: DeliveryAddress?,
        @SerializedName("deliveryDate")
        val deliveryDate: Any?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isDelivered")
        val isDelivered: Boolean?,
        @SerializedName("isPrime")
        val isPrime: Boolean?,
        @SerializedName("items")
        val items: List<Any?>?,
        @SerializedName("nextAction")
        val nextAction: String?,
        @SerializedName("ordNum")
        val ordNum: String?,
        @SerializedName("orderDate")
        val orderDate: String?,
        @SerializedName("orderStatus")
        val orderStatus: Int?,
        @SerializedName("orderType")
        val orderType: String?,
        @SerializedName("otp")
        val otp: Any?,
        @SerializedName("package")
        val packageX: Boolean?,
        @SerializedName("partnerId")
        val partnerId: Any?,
        @SerializedName("pickupDate")
        val pickupDate: String?,
        @SerializedName("ratings")
        val ratings: List<Any?>?,
        @SerializedName("riderId")
        val riderId: Any?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("serviceId")
        val serviceId: String?,
        @SerializedName("subscriptionId")
        val subscriptionId: Any?,
        @SerializedName("tagIds")
        val tagIds: List<Int?>?,
        @SerializedName("timeSlot")
        val timeSlot: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("userId")
        val userId: String?,
        @SerializedName("userName")
        val userName: String?,
        @SerializedName("__v")
        val v: Int?
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
}
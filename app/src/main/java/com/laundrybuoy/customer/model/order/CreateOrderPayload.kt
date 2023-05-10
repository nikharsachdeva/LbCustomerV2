package com.laundrybuoy.customer.model.order


import com.google.gson.annotations.SerializedName
import com.laundrybuoy.customer.model.address.GetUserAddressResponse

data class CreateOrderPayload(
    @SerializedName("approxCloths")
    var approxCloths: String?=null,
    @SerializedName("deliveryAddress")
    var deliveryAddress: GetUserAddressResponse.Data.Address?=null,
    @SerializedName("isPrime")
    var isPrime: Boolean?=null,
    @SerializedName("pickupDate")
    var pickupDate: String?=null,
    @SerializedName("serviceId")
    var serviceId: String?=null,
    @SerializedName("serviceName")
    var serviceName: String?=null,
    @SerializedName("timeSlot")
    var timeSlot: String?=null,
    @SerializedName("userId")
    var userId: String?=null,
    @SerializedName("coupon")
    var coupon: String?=null
) {
    data class DeliveryAddress(
        @SerializedName("city")
        var city: String?=null,
        @SerializedName("landmark")
        var landmark: String?=null,
        @SerializedName("latitude")
        var latitude: String?=null,
        @SerializedName("line1")
        var line1: String?=null,
        @SerializedName("longitude")
        var longitude: String?=null,
        @SerializedName("pin")
        var pin: String?=null,
        @SerializedName("state")
        var state: String?=null
    )
}
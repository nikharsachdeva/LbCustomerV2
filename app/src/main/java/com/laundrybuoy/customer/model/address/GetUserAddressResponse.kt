package com.laundrybuoy.customer.model.address


import com.google.gson.annotations.SerializedName

data class GetUserAddressResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("address")
        val address: Address?,
        @SerializedName("addressType")
        val addressType: String,
        @SerializedName("altMobile")
        val altMobile: Long?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isDefault")
        val isDefault: Boolean?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("userId")
        val userId: String?,
        @SerializedName("__v")
        val v: Int?
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
}
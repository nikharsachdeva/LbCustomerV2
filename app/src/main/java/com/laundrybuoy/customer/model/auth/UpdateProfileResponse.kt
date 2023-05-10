package com.laundrybuoy.customer.model.auth


import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("coin")
    val coin: Coin?,
    @SerializedName("data")
    val `data`: Any?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Coin(
        @SerializedName("quantity")
        val quantity: Int?,
        @SerializedName("status")
        val status: Boolean?
    )
}
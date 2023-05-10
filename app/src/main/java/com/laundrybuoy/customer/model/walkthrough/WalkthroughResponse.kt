package com.laundrybuoy.customer.model.walkthrough


import com.google.gson.annotations.SerializedName

data class WalkthroughResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("description")
        val description: String?,
        @SerializedName("heading")
        val heading: String?,
        @SerializedName("image")
        val image: String?
    )
}
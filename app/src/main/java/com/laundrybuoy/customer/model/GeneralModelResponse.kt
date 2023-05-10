package com.laundrybuoy.customer.model


import com.google.gson.annotations.SerializedName

data class GeneralModelResponse(
    @SerializedName("data")
    val `data`: Any?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
)
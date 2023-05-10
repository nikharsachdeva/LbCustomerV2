package com.laundrybuoy.customer.model.schedule


import com.google.gson.annotations.SerializedName

data class PickupDateResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?,
) {
    data class Data(
        @SerializedName("date")
        val date: String?,
        @SerializedName("day")
        val day: String?,
        @SerializedName("isValid")
        val isValid: Boolean?,
        @SerializedName("offset")
        val offset: String?,
        var dateOfSlot: String? = null,
        var monthOfSlot: String? = null,
        var isClickable: Boolean? = null
    )
}
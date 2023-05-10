package com.laundrybuoy.customer.model.plus


import com.google.gson.annotations.SerializedName

data class PlusIntroPointsModel(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("heading")
        val heading: String?,
        @SerializedName("subHeading")
        val subHeading: String?
    )
}
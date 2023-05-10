package com.laundrybuoy.customer.model.price


import com.google.gson.annotations.SerializedName

data class QuantityTypeModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("id")
        val id: String?,
        @SerializedName("qtyImage")
        val qtyImage: Int?,
        @SerializedName("qtyType")
        val qtyType: String?
    )
}
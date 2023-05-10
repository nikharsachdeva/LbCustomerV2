package com.laundrybuoy.customer.model.address


import com.google.gson.annotations.SerializedName

data class DeleteAddressResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("acknowledged")
        val acknowledged: Boolean?,
        @SerializedName("deletedCount")
        val deletedCount: Int?
    )
}
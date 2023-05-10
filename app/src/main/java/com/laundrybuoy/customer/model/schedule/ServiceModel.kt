package com.laundrybuoy.customer.model.schedule


import com.google.gson.annotations.SerializedName

data class ServiceModel(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("costPerCloth")
        val costPerCloth: Double?,
        @SerializedName("costPerKg")
        val costPerKg: Double?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("deliveryCloth")
        val deliveryCloth: Double?,
        @SerializedName("deliveryKg")
        val deliveryKg: Double?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("serviceImage")
        val serviceImage: String?,
        @SerializedName("serviceName")
        val serviceName: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}
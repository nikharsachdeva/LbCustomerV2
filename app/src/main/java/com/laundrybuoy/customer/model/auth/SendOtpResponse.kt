package com.laundrybuoy.customer.model.auth


import com.google.gson.annotations.SerializedName

data class SendOtpResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("isBlocked")
        val isBlocked: Boolean?,
        @SerializedName("isRegistered")
        val isRegistered: Boolean?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("otp")
        val otp: String?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("verificationLevel")
        val verificationLevel: Int?
    )
}
package com.laundrybuoy.customer.model.auth


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?,
) {
    data class Data(
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("isBlocked")
        val isBlocked: Boolean?,
        @SerializedName("isRegistered")
        val isRegistered: Boolean?,
        @SerializedName("loggedinUser")
        val loggedinUser: LoggedInUser?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("verificationLevel")
        val verificationLevel: Int?,
    ) {
        data class LoggedInUser(
            @SerializedName("id")
            val id: String?,
            @SerializedName("mobile")
            val mobile: Long?,
            @SerializedName("name")
            val name: String?
        )
    }
}
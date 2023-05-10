package com.laundrybuoy.customer.model.auth


import com.google.gson.annotations.SerializedName

data class GetProfileResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("jwtToken")
        val jwtToken: String?,
        @SerializedName("profile")
        val profile: Profile?,
        @SerializedName("isPreminum")
        val isPremium: Boolean?
    ) {
        data class Profile(
            @SerializedName("coins")
            val coins: Int?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("emailVarified")
            val emailVerified: Boolean?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isActive")
            val isActive: Boolean?,
            @SerializedName("isBlocked")
            val isBlocked: Boolean?,
            @SerializedName("mobile")
            val mobile: String?,
            @SerializedName("altMobile")
            val altMobile: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("profile")
            val profile: String?,
            @SerializedName("role")
            val role: String?,
            @SerializedName("tagId")
            val tagId: Int?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("__v")
            val v: Int?
        )
    }
}
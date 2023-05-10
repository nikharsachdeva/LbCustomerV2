package com.laundrybuoy.customer.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UserStructure(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @SerializedName("coins")
    val coins: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("emailVarified")
    val emailVerified: Boolean?,
    @SerializedName("isActive")
    val isActive: Boolean?,
    @SerializedName("isBlocked")
    val isBlocked: Boolean?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("altMobile")
    val altMobile: String?=null,
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
    val v: Int?,
    @SerializedName("jwtToken")
    val jwtToken: String?,
    @SerializedName("isPreminum")
    val isPremium: Boolean?
)

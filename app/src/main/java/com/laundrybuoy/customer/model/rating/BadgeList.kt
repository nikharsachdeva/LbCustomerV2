package com.laundrybuoy.customer.model.rating


import com.google.gson.annotations.SerializedName

data class BadgeList(
    @SerializedName("data")
    val `data`: List<BadgeItem?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?,
) {
    data class BadgeItem(
        @SerializedName("badgeIcon")
        val badgeIcon: String?,
        @SerializedName("badgeId")
        val badgeId: String?,
        @SerializedName("badgeTitle")
        val badgeTitle: String?,
        var isAwarded: Boolean? = false,
    )
}
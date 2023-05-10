package com.laundrybuoy.customer.model.rating


import com.google.gson.annotations.SerializedName

data class GiveRatingPayload(
    @SerializedName("badges")
    val badges: List<String?>?,
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("rating")
    val rating: Int?,
    @SerializedName("reviewId")
    val reviewId: String?
)
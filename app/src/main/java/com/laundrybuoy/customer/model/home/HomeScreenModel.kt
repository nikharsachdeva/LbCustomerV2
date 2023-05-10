package com.laundrybuoy.customer.model.home


import com.google.gson.annotations.SerializedName

data class HomeScreenModel(
    @SerializedName("data")
    val `data`: MutableList<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("action")
        val action: String?,
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("heading")
        val heading: String?,
        @SerializedName("type")
        val type: Int
    ) {
        data class Data(
            @SerializedName("date")
            val date: String?,
            @SerializedName("heading")
            val heading: String?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("ordNum")
            val ordNum: String?,
            @SerializedName("ordService")
            val ordService: String?,
            @SerializedName("ordServiceImage")
            val ordServiceImage: String?,
            @SerializedName("status")
            val status: Int?,
            @SerializedName("subHeading")
            val subHeading: String?,
            @SerializedName("title")
            val title: String?
        )
    }
}
package com.laundrybuoy.customer.model.refer


import com.google.gson.annotations.SerializedName

class FaqModel : ArrayList<FaqModel.FaqModelItem>(){
    data class FaqModelItem(
        @SerializedName("answer")
        val answer: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("isExpanded")
        var isExpanded: Boolean=false,
        @SerializedName("question")
        val question: String?
    )
}
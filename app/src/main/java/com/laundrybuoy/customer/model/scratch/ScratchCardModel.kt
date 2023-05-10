package com.laundrybuoy.customer.model.scratch


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class ScratchCardModel : ArrayList<ScratchCardModel.ScratchCardModelItem>(){
    @Parcelize
    data class ScratchCardModelItem(
        @SerializedName("coins")
        var coins: Int?,
        @SerializedName("date")
        var date: String?,
        @SerializedName("desc")
        var desc: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("isRevealed")
        var isRevealed: Boolean?
    ): Parcelable
}
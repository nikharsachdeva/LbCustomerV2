package com.laundrybuoy.customer.model.price


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class InventoryListSegregated : ArrayList<InventoryListSegregated.InventoryListSegregatedItem>(){
    @Parcelize
    data class InventoryListSegregatedItem(
        @SerializedName("catList")
        val catList: List<InventoryModel.Data.Item>,
        @SerializedName("catName")
        val catName: String?
    ): Parcelable
}
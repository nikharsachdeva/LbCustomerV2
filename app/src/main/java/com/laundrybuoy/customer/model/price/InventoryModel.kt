package com.laundrybuoy.customer.model.price


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class InventoryModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
):Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("items")
        val items: List<Item>,
        @SerializedName("serviceCost")
        val serviceCost: Double?
    ):Parcelable {
        @Parcelize
        data class Item(
            @SerializedName("category")
            val category: Category?=null,
            @SerializedName("eqCloths")
            val eqCloths: Double?=null,
            @SerializedName("_id")
            val id: String?=null,
            @SerializedName("itemName")
            val itemName: String?=null,
            @SerializedName("profile")
            val profile: String?=null,
            @SerializedName("__v")
            val v: Int?=null,
            var selectedQty: Int? = 0
        ):Parcelable {
            @Parcelize
            data class Category(
                @SerializedName("categoryName")
                val categoryName: String?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("profile")
                val profile: String?,
                @SerializedName("__v")
                val v: Int?
            ):Parcelable
        }
    }
}
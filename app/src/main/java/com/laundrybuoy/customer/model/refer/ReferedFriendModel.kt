package com.laundrybuoy.customer.model.refer


import com.google.gson.annotations.SerializedName

class ReferedFriendModel : ArrayList<ReferedFriendModel.ReferedFriendModelItem>(){
    data class ReferedFriendModelItem(
        @SerializedName("hasOrdered")
        val hasOrdered: Boolean?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?
    )
}
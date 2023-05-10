package com.laundrybuoy.customer.model.rating


import com.google.gson.annotations.SerializedName

class EmojiList : ArrayList<EmojiList.EmojiListItem>() {
    data class EmojiListItem(
        @SerializedName("emoji")
        val emoji: Int,
        @SerializedName("value")
        val value: Int,
        var isSelected: Boolean? = null,
    )
}
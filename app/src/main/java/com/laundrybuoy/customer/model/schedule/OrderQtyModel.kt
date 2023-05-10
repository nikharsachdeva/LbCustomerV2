package com.laundrybuoy.customer.model.schedule


import com.google.gson.annotations.SerializedName

class OrderQtyModel : ArrayList<OrderQtyModel.OrderQtyModelItem>(){
    data class OrderQtyModelItem(
        @SerializedName("qty")
        val qty: String?
    )
}
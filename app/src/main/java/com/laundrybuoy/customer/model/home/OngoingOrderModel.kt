package com.laundrybuoy.customer.model.home


import com.google.gson.annotations.SerializedName

class OngoingOrderModel : ArrayList<OngoingOrderModel.OngoingProgressModelItem>(){
    data class OngoingProgressModelItem(
        @SerializedName("progress")
        val progress: String?,
        @SerializedName("serviceDate")
        val serviceDate: String?,
        @SerializedName("serviceName")
        val serviceName: String?,
        @SerializedName("serviceStage")
        val serviceStage: Int?,
        @SerializedName("id")
        val id: Int?
    )
}
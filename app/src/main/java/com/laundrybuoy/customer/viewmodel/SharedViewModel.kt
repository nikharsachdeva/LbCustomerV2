package com.laundrybuoy.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laundrybuoy.customer.model.order.CreateOrderPayload
import com.laundrybuoy.customer.model.order.CreateOrderResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() :
    ViewModel() {

    val message = MutableLiveData<String?>()
    val orderPayload = MutableLiveData<CreateOrderPayload?>()
    val orderResponse = MutableLiveData<CreateOrderResponse?>()
    var priceFilterPair = MutableLiveData<Pair<String?,String?>>()

    fun sendMessage(text: String?) {
        message.value = text
    }

    fun setOrderPayload(payload: CreateOrderPayload?) {
        orderPayload.value = payload
    }

    fun setOrderResponse(response: CreateOrderResponse?) {
        orderResponse.value = response
    }

    fun setPriceFilter(pairOfFilter : Pair<String?,String?>){
        priceFilterPair.value = pairOfFilter
    }

}
package com.laundrybuoy.customer.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.JsonObject
import com.laundrybuoy.customer.model.GeneralModelResponse
import com.laundrybuoy.customer.model.address.AddAddressPayload
import com.laundrybuoy.customer.model.address.AddAddressResponse
import com.laundrybuoy.customer.model.address.DeleteAddressResponse
import com.laundrybuoy.customer.model.address.GetUserAddressResponse
import com.laundrybuoy.customer.model.auth.SendOtpResponse
import com.laundrybuoy.customer.model.order.CreateOrderPayload
import com.laundrybuoy.customer.model.order.CreateOrderResponse
import com.laundrybuoy.customer.model.order.CustomerOrdersModel
import com.laundrybuoy.customer.model.order.OrderDetailResponse
import com.laundrybuoy.customer.model.price.InventoryModel
import com.laundrybuoy.customer.model.rating.GiveRatingPayload
import com.laundrybuoy.customer.model.schedule.CouponModel
import com.laundrybuoy.customer.model.schedule.PickupDateResponse
import com.laundrybuoy.customer.model.schedule.PlaceOrderPayload
import com.laundrybuoy.customer.model.schedule.ServiceModel
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.repository.OrderRepository
import com.laundrybuoy.customer.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val application: Application,
) :
    ViewModel() {

    val _scheduleOrderPayloadLD = MutableLiveData<PlaceOrderPayload>()
    val scheduleOrderPayloadLD: LiveData<PlaceOrderPayload>
        get() = _scheduleOrderPayloadLD

    var _quickOrderPayloadLD = MutableLiveData<CreateOrderPayload>()
    val quickOrderPayloadLD: LiveData<CreateOrderPayload>
        get() = _quickOrderPayloadLD

    val servicesLiveData: LiveData<NetworkResult<ServiceModel>>
        get() = repository.serviceLiveResponse

    val validTimeSlotsLiveData: LiveData<NetworkResult<PickupDateResponse>>
        get() = repository.validDateLiveResponse

    val orderDetailLiveData: LiveData<NetworkResult<OrderDetailResponse>>
        get() = repository.orderDetailLiveResponse

    val addressLiveData: LiveData<NetworkResult<GetUserAddressResponse>>
        get() = repository.addressBookLiveResponse

    val addAddressLiveData: LiveData<NetworkResult<AddAddressResponse>>
        get() = repository.addAddressLiveResponse

    val deleteAddressLiveData: LiveData<NetworkResult<DeleteAddressResponse>>
        get() = repository.deleteAddressLiveResponse

    val defaultAddressLiveData: LiveData<NetworkResult<GeneralModelResponse>>
        get() = repository.defaultAddressLiveResponse

    val couponsLiveData: LiveData<NetworkResult<CouponModel>>
        get() = repository.couponsLiveResponse

    var tempList : MutableList<InventoryModel.Data.Item> = mutableListOf()
    private var _selectedItemsList = MutableLiveData<MutableList<InventoryModel.Data.Item>>()
    val selectedItemsList: LiveData<MutableList<InventoryModel.Data.Item>>
        get() = _selectedItemsList

    val priceListLiveData: LiveData<NetworkResult<InventoryModel>>
        get() = repository.priceListResponse

    val _customerOrderLiveData =
        MutableLiveData<PagingData<CustomerOrdersModel.Data.Partner>>()
    val customerOrderLiveData: LiveData<PagingData<CustomerOrdersModel.Data.Partner>>
        get() = _customerOrderLiveData

    val giveRatingLiveData: LiveData<NetworkResult<GeneralModelResponse>>
        get() = repository.giveRatingLiveResponse

    val createOrderLiveData: LiveData<NetworkResult<CreateOrderResponse>>
        get() = repository.createOrderLiveResponse

    fun pushSelectedBillingList(nonZeroSelectedItems: MutableList<InventoryModel.Data.Item>) {
        tempList.addAll(nonZeroSelectedItems)
        _selectedItemsList.value = tempList
    }

    fun fetchListItems(itemPayload: JsonObject) {
        viewModelScope.launch {
            repository.getPriceList(itemPayload)
        }
    }

    fun setOrderPayload(orderPayload: PlaceOrderPayload?) {
        _scheduleOrderPayloadLD.postValue(orderPayload)
    }

    fun getServices() {
        viewModelScope.launch {
            repository.getServices()
        }
    }

    fun getValidTimeSlots() {
        viewModelScope.launch {
            repository.getValidTimeSlots()
        }
    }

    fun getAddressBook() {
        viewModelScope.launch {
            repository.getAddressBook()
        }
    }

    fun addAddress(addressPayload: AddAddressPayload) {
        viewModelScope.launch {
            repository.addAddress(addressPayload)
        }
    }

    fun deleteAddress(deletePayload: JsonObject) {
        viewModelScope.launch {
            repository.deleteAddress(deletePayload)
        }
    }

    fun markAddressDefault(defaultPayload: JsonObject) {
        viewModelScope.launch {
            repository.markDefaultAddress(defaultPayload)
        }
    }

    suspend fun getCustomerOrders(): LiveData<PagingData<CustomerOrdersModel.Data.Partner>> {
        val response = repository.getCustomerOrder().cachedIn(viewModelScope)
        _customerOrderLiveData.value = response.value
        return response
    }

    fun giveRating(payload: GiveRatingPayload) {
        viewModelScope.launch {
            repository.giveRating(payload)
        }
    }

    fun getCoupons() {
        viewModelScope.launch {
            repository.getCoupons()
        }
    }

    fun createOrder(createPayload: CreateOrderPayload) {
        viewModelScope.launch {
            repository.createOrder(createPayload)
        }
    }

    fun getOrderDetails(orderId : String) {
        viewModelScope.launch {
            repository.getOrderDetails(orderId)
        }
    }

}
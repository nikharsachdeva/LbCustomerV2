package com.laundrybuoy.customer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.JsonObject
import com.laundrybuoy.customer.adapter.coins.CustomerCoinsPagingSource
import com.laundrybuoy.customer.adapter.order.CustomerOrderPagingSource
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
import com.laundrybuoy.customer.model.schedule.ServiceModel
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.retrofit.CustomerAPI
import com.laundrybuoy.customer.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class OrderRepository @Inject constructor(private val userAPI: CustomerAPI) {

    private val _serviceLiveResponse = MutableLiveData<NetworkResult<ServiceModel>>()
    val serviceLiveResponse: LiveData<NetworkResult<ServiceModel>>
        get() = _serviceLiveResponse

    private val _validDateLiveResponse = MutableLiveData<NetworkResult<PickupDateResponse>>()
    val validDateLiveResponse: LiveData<NetworkResult<PickupDateResponse>>
        get() = _validDateLiveResponse

    private val _couponsLiveResponse = MutableLiveData<NetworkResult<CouponModel>>()
    val couponsLiveResponse: LiveData<NetworkResult<CouponModel>>
        get() = _couponsLiveResponse

    private val _orderDetailLiveResponse = MutableLiveData<NetworkResult<OrderDetailResponse>>()
    val orderDetailLiveResponse: LiveData<NetworkResult<OrderDetailResponse>>
        get() = _orderDetailLiveResponse

    private val _addressBookLiveResponse = MutableLiveData<NetworkResult<GetUserAddressResponse>>()
    val addressBookLiveResponse: LiveData<NetworkResult<GetUserAddressResponse>>
        get() = _addressBookLiveResponse

    private val _addAddressLiveResponse = MutableLiveData<NetworkResult<AddAddressResponse>>()
    val addAddressLiveResponse: LiveData<NetworkResult<AddAddressResponse>>
        get() = _addAddressLiveResponse

    private val _deleteAddressLiveResponse = MutableLiveData<NetworkResult<DeleteAddressResponse>>()
    val deleteAddressLiveResponse: LiveData<NetworkResult<DeleteAddressResponse>>
        get() = _deleteAddressLiveResponse

    private val _defaultAddressLiveResponse = MutableLiveData<NetworkResult<GeneralModelResponse>>()
    val defaultAddressLiveResponse: LiveData<NetworkResult<GeneralModelResponse>>
        get() = _defaultAddressLiveResponse

    private val _priceListResponse = MutableLiveData<NetworkResult<InventoryModel>>()
    val priceListResponse: LiveData<NetworkResult<InventoryModel>>
        get() = _priceListResponse

    private val _giveRatingLiveResponse = MutableLiveData<NetworkResult<GeneralModelResponse>>()
    val giveRatingLiveResponse: LiveData<NetworkResult<GeneralModelResponse>>
        get() = _giveRatingLiveResponse

    private val _createOrderLiveResponse = MutableLiveData<NetworkResult<CreateOrderResponse>>()
    val createOrderLiveResponse: LiveData<NetworkResult<CreateOrderResponse>>
        get() = _createOrderLiveResponse

    suspend fun getPriceList(itemPayload: JsonObject) {
        _priceListResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getPriceList(itemPayload)
        if(response.isSuccessful && response.body()!=null){
            _priceListResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _priceListResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _priceListResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getServices() {
        _serviceLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getServices()
        if (response.isSuccessful && response.body() != null) {
            _serviceLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _serviceLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _serviceLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getAddressBook() {
        _addressBookLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getAddressBook()
        if (response.isSuccessful && response.body() != null) {
            _addressBookLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _addressBookLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _addressBookLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getValidTimeSlots() {
        _validDateLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getValidTimeSlots()
        if (response.isSuccessful && response.body() != null) {
            _validDateLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _validDateLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _validDateLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }


    suspend fun addAddress(addressPayload: AddAddressPayload) {
        _addAddressLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.addAddress(addressPayload)
        if (response.isSuccessful && response.body() != null) {
            _addAddressLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _addAddressLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _addAddressLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun deleteAddress(deletePayload: JsonObject) {
        _deleteAddressLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.deleteAddress(deletePayload)
        if (response.isSuccessful && response.body() != null) {
            _deleteAddressLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _deleteAddressLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _deleteAddressLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun markDefaultAddress(defaultPayload: JsonObject) {
        _defaultAddressLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.markAddressDefault(defaultPayload)
        if (response.isSuccessful && response.body() != null) {
            _defaultAddressLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _defaultAddressLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _defaultAddressLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getCustomerOrder(): LiveData<PagingData<CustomerOrdersModel.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CustomerOrderPagingSource(userAPI)
            }
        ).liveData

    }

    suspend fun giveRating(payload: GiveRatingPayload) {
        _giveRatingLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.giveRating(payload)
        if (response.isSuccessful && response.body() != null) {
            _giveRatingLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _giveRatingLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _giveRatingLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getCoupons() {
        _couponsLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getCoupons()
        if (response.isSuccessful && response.body() != null) {
            _couponsLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _couponsLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _couponsLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun createOrder(payload: CreateOrderPayload) {
        _createOrderLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.createOrder(payload)
        if (response.isSuccessful && response.body() != null) {
            _createOrderLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _createOrderLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _createOrderLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getOrderDetails(orderId : String) {
        _orderDetailLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getOrderDetails(orderId)
        if (response.isSuccessful && response.body() != null) {
            _orderDetailLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _orderDetailLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _orderDetailLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }
}
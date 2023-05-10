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
import com.laundrybuoy.customer.model.home.HomeScreenModel
import com.laundrybuoy.customer.model.order.CreateOrderPayload
import com.laundrybuoy.customer.model.order.CreateOrderResponse
import com.laundrybuoy.customer.model.order.CustomerOrdersModel
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

class HomeRepository @Inject constructor(private val userAPI: CustomerAPI) {

    private val _homeScreenLiveResponse = MutableLiveData<NetworkResult<HomeScreenModel>>()
    val homeScreenLiveResponse: LiveData<NetworkResult<HomeScreenModel>>
        get() = _homeScreenLiveResponse

    suspend fun getHomeScreen() {
        _homeScreenLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getHomeScreenData()
        if (response.isSuccessful && response.body() != null) {
            _homeScreenLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _homeScreenLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _homeScreenLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }
}
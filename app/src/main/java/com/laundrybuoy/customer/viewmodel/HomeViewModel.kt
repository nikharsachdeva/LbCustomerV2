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
import com.laundrybuoy.customer.model.home.HomeScreenModel
import com.laundrybuoy.customer.model.order.CreateOrderPayload
import com.laundrybuoy.customer.model.order.CreateOrderResponse
import com.laundrybuoy.customer.model.order.CustomerOrdersModel
import com.laundrybuoy.customer.model.price.InventoryModel
import com.laundrybuoy.customer.model.rating.GiveRatingPayload
import com.laundrybuoy.customer.model.schedule.CouponModel
import com.laundrybuoy.customer.model.schedule.PickupDateResponse
import com.laundrybuoy.customer.model.schedule.PlaceOrderPayload
import com.laundrybuoy.customer.model.schedule.ServiceModel
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.repository.HomeRepository
import com.laundrybuoy.customer.repository.OrderRepository
import com.laundrybuoy.customer.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val application: Application,
) :
    ViewModel() {

    val homeScreenLiveData: LiveData<NetworkResult<HomeScreenModel>>
        get() = repository.homeScreenLiveResponse

    fun getHomeScreenData() {
        viewModelScope.launch {
            repository.getHomeScreen()
        }
    }

}
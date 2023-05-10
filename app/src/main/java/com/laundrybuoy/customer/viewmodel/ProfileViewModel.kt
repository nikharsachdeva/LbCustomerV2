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
import com.laundrybuoy.customer.model.address.DeleteAddressResponse
import com.laundrybuoy.customer.model.plus.GetMembershipsResponse
import com.laundrybuoy.customer.model.schedule.PlaceOrderPayload
import com.laundrybuoy.customer.model.scratch.ScratchCardModel
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.repository.ProfileRepository
import com.laundrybuoy.customer.utils.LocationLiveData
import com.laundrybuoy.customer.utils.LocationModel
import com.laundrybuoy.customer.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository,private val application: Application ) :
    ViewModel() {

    val _selectedLatLngLiveData = MutableLiveData<LocationModel>()
    val selectedLatLngLiveData: LiveData<LocationModel>
        get() = _selectedLatLngLiveData

    val _selectedScratchCardLiveData = MutableLiveData<ScratchCardModel.ScratchCardModelItem?>()
    val selectedScratchCardLiveData: LiveData<ScratchCardModel.ScratchCardModelItem?>
        get() = _selectedScratchCardLiveData

    val _customerSubLiveData = MutableLiveData<PagingData<CustomerTransactionModel.Data.Partner>>()
    val customerSubLiveData: LiveData<PagingData<CustomerTransactionModel.Data.Partner>>
        get() = _customerSubLiveData

    val _customerCoinsLiveData =
        MutableLiveData<PagingData<CustomerTransactionModel.Data.Partner>>()
    val customerCoinsLiveData: LiveData<PagingData<CustomerTransactionModel.Data.Partner>>
        get() = _customerCoinsLiveData

    val _customerScratchLiveData =
        MutableLiveData<PagingData<CustomerTransactionModel.Data.Partner>>()
    val customerScratchLiveData: LiveData<PagingData<CustomerTransactionModel.Data.Partner>>
        get() = _customerScratchLiveData

    val availScratchLiveData: LiveData<NetworkResult<GeneralModelResponse>>
        get() = repository.availScratchLiveResponse

    val allMembershipsLiveData: LiveData<NetworkResult<GetMembershipsResponse>>
        get() = repository.allMembershipsLiveResponse

    val purchaseMembershipLiveData: LiveData<NetworkResult<GeneralModelResponse>>
        get() = repository.purchaseMembershipLiveResponse

    val uploadDocLiveData: LiveData<NetworkResult<GeneralModelResponse>>
        get() = repository.uploadDocResponse

    private val locationData = LocationLiveData(application)

    fun setSelectedLatLng(latlng: LocationModel) {
        _selectedLatLngLiveData.postValue(latlng)
    }

    fun setSelectedScratchCard(scratchCard: ScratchCardModel.ScratchCardModelItem?) {
        _selectedScratchCardLiveData.postValue(scratchCard)
    }

    fun getLiveLocationData() = locationData

    suspend fun getCustomerSubscription(): LiveData<PagingData<CustomerTransactionModel.Data.Partner>> {
        val response = repository.getCustomerSubscription().cachedIn(viewModelScope)
        _customerSubLiveData.value = response.value
        return response
    }

    suspend fun getCustomerCoins(): LiveData<PagingData<CustomerTransactionModel.Data.Partner>> {
        val response = repository.getCustomerCoins().cachedIn(viewModelScope)
        _customerCoinsLiveData.value = response.value
        return response
    }

    suspend fun getCustomerScratchCards(): LiveData<PagingData<CustomerTransactionModel.Data.Partner>> {
        val response = repository.getCustomerScratchCards().cachedIn(viewModelScope)
        _customerScratchLiveData.value = response.value
        return response
    }

    fun availScratchCard(payload: JsonObject) {
        viewModelScope.launch {
            repository.availScratchCard(payload)
        }
    }

    fun purchaseMembership(payload: JsonObject) {
        viewModelScope.launch {
            repository.purchaseMembership(payload)
        }
    }

    fun getAllMemberships() {
        viewModelScope.launch {
            repository.getAllMemberships()
        }
    }

    fun uploadDoc(body: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadDoc(body)
        }
    }
}
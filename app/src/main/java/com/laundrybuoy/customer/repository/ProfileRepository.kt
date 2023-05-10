package com.laundrybuoy.customer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.JsonObject
import com.laundrybuoy.customer.adapter.coins.CustomerCoinsPagingSource
import com.laundrybuoy.customer.adapter.scratch.CustomerScratchPagingSource
import com.laundrybuoy.customer.adapter.subscription.CustomerSubscriptionPagingSource
import com.laundrybuoy.customer.model.GeneralModelResponse
import com.laundrybuoy.customer.model.plus.GetMembershipsResponse
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.retrofit.CustomerAPI
import com.laundrybuoy.customer.utils.NetworkResult
import okhttp3.MultipartBody
import org.json.JSONObject
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val userAPI: CustomerAPI) {

    private val _availScratchLiveResponse = MutableLiveData<NetworkResult<GeneralModelResponse>>()
    val availScratchLiveResponse: LiveData<NetworkResult<GeneralModelResponse>>
        get() = _availScratchLiveResponse

    private val _purchaseMembershipLiveResponse = MutableLiveData<NetworkResult<GeneralModelResponse>>()
    val purchaseMembershipLiveResponse: LiveData<NetworkResult<GeneralModelResponse>>
        get() = _purchaseMembershipLiveResponse

    private val _allMembershipsLiveResponse = MutableLiveData<NetworkResult<GetMembershipsResponse>>()
    val allMembershipsLiveResponse: LiveData<NetworkResult<GetMembershipsResponse>>
        get() = _allMembershipsLiveResponse

    private val _uploadDocResponse = MutableLiveData<NetworkResult<GeneralModelResponse>>()
    val uploadDocResponse: LiveData<NetworkResult<GeneralModelResponse>>
        get() = _uploadDocResponse

    suspend fun uploadDoc(body: MultipartBody.Part) {
        _uploadDocResponse.postValue(NetworkResult.Loading())
        val response = userAPI.uploadDoc(body)
        if (response.isSuccessful && response.body() != null) {
            _uploadDocResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _uploadDocResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        } else {
            _uploadDocResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getCustomerSubscription(): LiveData<PagingData<CustomerTransactionModel.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CustomerSubscriptionPagingSource(userAPI)
            }
        ).liveData

    }

    suspend fun getCustomerCoins(): LiveData<PagingData<CustomerTransactionModel.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CustomerCoinsPagingSource(userAPI)
            }
        ).liveData

    }

    suspend fun getCustomerScratchCards(): LiveData<PagingData<CustomerTransactionModel.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CustomerScratchPagingSource(userAPI)
            }
        ).liveData

    }

    suspend fun availScratchCard(payload: JsonObject) {
        _availScratchLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.availScratchCard(payload)
        if (response.isSuccessful && response.body() != null) {
            _availScratchLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _availScratchLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _availScratchLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }


    suspend fun getAllMemberships() {
        _allMembershipsLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getAllMemberships()
        if (response.isSuccessful && response.body() != null) {
            _allMembershipsLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _allMembershipsLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _allMembershipsLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun purchaseMembership(payload: JsonObject) {
        _purchaseMembershipLiveResponse.postValue(NetworkResult.Loading())
        val response = userAPI.buyMembership(payload)
        if (response.isSuccessful && response.body() != null) {
            _purchaseMembershipLiveResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _purchaseMembershipLiveResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _purchaseMembershipLiveResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }
}
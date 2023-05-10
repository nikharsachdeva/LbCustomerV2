package com.laundrybuoy.customer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.laundrybuoy.customer.db.UserDAO
import com.laundrybuoy.customer.db.UserStructure
import com.laundrybuoy.customer.model.auth.GetProfileResponse
import com.laundrybuoy.customer.model.auth.SendOtpResponse
import com.laundrybuoy.customer.model.auth.UpdateProfileResponse
import com.laundrybuoy.customer.model.auth.VerifyOtpResponse
import com.laundrybuoy.customer.retrofit.CustomerAPI
import com.laundrybuoy.customer.utils.NetworkResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userAPI: CustomerAPI,
    private val userDAO: UserDAO,
) {

    private val _sendOtpResponse = MutableLiveData<NetworkResult<SendOtpResponse>>()
    val sendOtpResponse: LiveData<NetworkResult<SendOtpResponse>>
        get() = _sendOtpResponse

    private val _verifyOtpResponse = MutableLiveData<NetworkResult<VerifyOtpResponse>>()
    val verifyOtpResponse: LiveData<NetworkResult<VerifyOtpResponse>>
        get() = _verifyOtpResponse

    val loggedInUserLive: LiveData<UserStructure> = userDAO.getLoggedInUserLive()

    private val _updateUserResponse = MutableLiveData<NetworkResult<UpdateProfileResponse>>()
    val updateUserResponse: LiveData<NetworkResult<UpdateProfileResponse>>
        get() = _updateUserResponse

    private val _userProfileResponse = MutableLiveData<NetworkResult<GetProfileResponse>>()
    val userProfileResponse: LiveData<NetworkResult<GetProfileResponse>>
        get() = _userProfileResponse

    suspend fun logOut(){
        userDAO.signOut()
    }

    suspend fun getUserProfile() {
        _userProfileResponse.postValue(NetworkResult.Loading())
        val response = userAPI.getProfile()
        if (response.isSuccessful && response.body() != null) {
            _userProfileResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _userProfileResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _userProfileResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }


    suspend fun sendOtp(payload: JsonObject) {
        _sendOtpResponse.postValue(NetworkResult.Loading())
        val response = userAPI.sendOTP(payload)
        if (response.isSuccessful && response.body() != null) {
            _sendOtpResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _sendOtpResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _sendOtpResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun verifyOtp(payload: JsonObject) {
        _verifyOtpResponse.postValue(NetworkResult.Loading())
        val response = userAPI.verifyOTP(payload)
        if (response.isSuccessful && response.body() != null) {
            _verifyOtpResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _verifyOtpResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _verifyOtpResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    fun saveUserToDb(user : UserStructure){
        userDAO.signIn(user)
    }

    suspend fun updateUser(payload: JsonObject) {
        _updateUserResponse.postValue(NetworkResult.Loading())
        val response = userAPI.updateUser(payload)
        if (response.isSuccessful && response.body() != null) {
            _updateUserResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _updateUserResponse.postValue((NetworkResult.Error(response.code().toString())))
        } else {
            _updateUserResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

}
package com.laundrybuoy.customer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.laundrybuoy.customer.db.UserStructure
import com.laundrybuoy.customer.model.auth.GetProfileResponse
import com.laundrybuoy.customer.model.auth.SendOtpResponse
import com.laundrybuoy.customer.model.auth.UpdateProfileResponse
import com.laundrybuoy.customer.model.auth.VerifyOtpResponse
import com.laundrybuoy.customer.repository.AuthRepository
import com.laundrybuoy.customer.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) :
    ViewModel() {

    var loggedInUserLive: LiveData<UserStructure>? = null

    init {
        loggedInUserLive = repository.loggedInUserLive
    }

    val sendOtpLiveData: LiveData<NetworkResult<SendOtpResponse>>
        get() = repository.sendOtpResponse

    val verifyOtpLiveData: LiveData<NetworkResult<VerifyOtpResponse>>
        get() = repository.verifyOtpResponse

    val updateUserLiveData: LiveData<NetworkResult<UpdateProfileResponse>>
        get() = repository.updateUserResponse

    val userProfileLiveData: LiveData<NetworkResult<GetProfileResponse>>
        get() = repository.userProfileResponse

    fun logOut(){

        CoroutineScope(Dispatchers.IO).launch {
            repository.logOut()
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            repository.getUserProfile()
        }
    }

    fun sendOtp(payload: JsonObject) {
        viewModelScope.launch {
            repository.sendOtp(payload)
        }
    }

    fun verifyOtp(payload: JsonObject) {
        viewModelScope.launch {
            repository.verifyOtp(payload)
        }
    }

    fun saveUserToDb(user: UserStructure) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveUserToDb(user)
        }
    }

    fun updateUser(payload: JsonObject) {
        viewModelScope.launch {
            repository.updateUser(payload)
        }
    }

}
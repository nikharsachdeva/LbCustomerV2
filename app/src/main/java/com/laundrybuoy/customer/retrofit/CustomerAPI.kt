package com.laundrybuoy.customer.retrofit

import com.google.gson.JsonObject
import com.laundrybuoy.customer.model.GeneralModelResponse
import com.laundrybuoy.customer.model.address.AddAddressPayload
import com.laundrybuoy.customer.model.address.AddAddressResponse
import com.laundrybuoy.customer.model.address.DeleteAddressResponse
import com.laundrybuoy.customer.model.address.GetUserAddressResponse
import com.laundrybuoy.customer.model.auth.GetProfileResponse
import com.laundrybuoy.customer.model.auth.SendOtpResponse
import com.laundrybuoy.customer.model.auth.UpdateProfileResponse
import com.laundrybuoy.customer.model.auth.VerifyOtpResponse
import com.laundrybuoy.customer.model.home.HomeScreenModel
import com.laundrybuoy.customer.model.order.CreateOrderPayload
import com.laundrybuoy.customer.model.order.CreateOrderResponse
import com.laundrybuoy.customer.model.order.CustomerOrdersModel
import com.laundrybuoy.customer.model.order.OrderDetailResponse
import com.laundrybuoy.customer.model.plus.GetMembershipsResponse
import com.laundrybuoy.customer.model.price.InventoryModel
import com.laundrybuoy.customer.model.rating.GiveRatingPayload
import com.laundrybuoy.customer.model.schedule.CouponModel
import com.laundrybuoy.customer.model.schedule.PickupDateResponse
import com.laundrybuoy.customer.model.schedule.ServiceModel
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface CustomerAPI {

    @GET("users/getUserSubHistory")
    suspend fun getCustomerSubscription(
        @Query("page") page: Int,
    ): Response<CustomerTransactionModel>

    @GET("users/getUserCoinHistory")
    suspend fun getCustomerCoins(
        @Query("page") page: Int,
    ): Response<CustomerTransactionModel>

    @GET("users/getUserOrders")
    suspend fun getCustomerOrders(
        @Query("page") page: Int,
    ): Response<CustomerOrdersModel>

    @GET("users/getScratchCards")
    suspend fun getCustomerScratchCard(
        @Query("page") page: Int,
    ): Response<CustomerTransactionModel>

    @GET("users/getPackages")
    suspend fun getAllMemberships(): Response<GetMembershipsResponse>

    @POST("auth/getMobile")
    suspend fun sendOTP(
        @Body payload: JsonObject,
    ): Response<SendOtpResponse>

    @POST("auth/verify")
    suspend fun verifyOTP(
        @Body payload: JsonObject,
    ): Response<VerifyOtpResponse>

    @PUT("users/addUserData")
    suspend fun updateUser(
        @Body payload: JsonObject,
    ): Response<UpdateProfileResponse>


    @Multipart
    @POST("users/uploadPhotos")
    suspend fun uploadDoc(@Part image: MultipartBody.Part): Response<GeneralModelResponse>

    @GET("users/getProfile")
    suspend fun getProfile(): Response<GetProfileResponse>

    @GET("users/getServices")
    suspend fun getServices(): Response<ServiceModel>

    @GET("users/getUserAddresses")
    suspend fun getAddressBook(): Response<GetUserAddressResponse>

    @GET("users/getTimeSlot")
    suspend fun getValidTimeSlots(): Response<PickupDateResponse>

    @POST("users/addAddress")
    suspend fun addAddress(
        @Body payload: AddAddressPayload,
    ): Response<AddAddressResponse>

    @POST("users/deleteUserAddress")
    suspend fun deleteAddress(
        @Body payload: JsonObject,
    ): Response<DeleteAddressResponse>

    @POST("users/setPrimaryAddress")
    suspend fun markAddressDefault(
        @Body payload: JsonObject,
    ): Response<GeneralModelResponse>

    @POST("users/addCoinFromScratch")
    suspend fun availScratchCard(
        @Body payload: JsonObject,
    ): Response<GeneralModelResponse>

    @POST("users/buyPackage")
    suspend fun buyMembership(
        @Body payload: JsonObject,
    ): Response<GeneralModelResponse>

    @POST("users/getItem")
    suspend fun getPriceList(
        @Body itemPayload: JsonObject,
    ): Response<InventoryModel>

    @POST("users/giveRatings")
    suspend fun giveRating(
        @Body payload: GiveRatingPayload,
    ): Response<GeneralModelResponse>

    @GET("orders/getCoupons")
    suspend fun getCoupons(): Response<CouponModel>

    @POST("orders/createOrder")
    suspend fun createOrder(
        @Body payload: CreateOrderPayload,
    ): Response<CreateOrderResponse>

    @GET("users/getHomeScreen")
    suspend fun getHomeScreenData(): Response<HomeScreenModel>

    @GET("users/orderDetails/{orderId}")
    suspend fun getOrderDetails(@Path("orderId") orderId: String): Response<OrderDetailResponse>

}
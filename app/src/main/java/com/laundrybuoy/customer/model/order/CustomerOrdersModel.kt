package com.laundrybuoy.customer.model.order


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CustomerOrdersModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("endIndex")
        val endIndex: Int?,
        @SerializedName("len")
        val len: Int?,
        @SerializedName("nextPage")
        val nextPage: Int?,
        @SerializedName("partners")
        val partners: List<Partner>,
        @SerializedName("prevPage")
        val prevPage: Int?,
        @SerializedName("startIndex")
        val startIndex: Int?,
        @SerializedName("totalItems")
        val totalItems: Int?,
        @SerializedName("totalPage")
        val totalPage: Int?
    ) {
        data class Partner(
            @SerializedName("orderDetails")
            val orderDetails: OrderDetails,
            @SerializedName("ratings")
            val ratings: List<Rating>
        ) {
            data class OrderDetails(
                @SerializedName("bagIds")
                val bagIds: List<String?>?,
                @SerializedName("createdAt")
                val createdAt: String?,
                @SerializedName("deliveryAddress")
                val deliveryAddress: DeliveryAddress?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isDelivered")
                val isDelivered: Boolean?,
                @SerializedName("isPrime")
                val isPrime: Boolean?,
                @SerializedName("items")
                val items: List<Item?>?,
                @SerializedName("nextAction")
                val nextAction: String?,
                @SerializedName("ordNum")
                val ordNum: String?,
                @SerializedName("orderDate")
                val orderDate: String?,
                @SerializedName("orderStatus")
                val orderStatus: Int?,
                @SerializedName("package")
                val packageX: Boolean?,
                @SerializedName("partnerId")
                val partnerId: PartnerId?,
                @SerializedName("pickupDate")
                val pickupDate: String?,
                @SerializedName("riderId")
                val riderId: RiderId?,
                @SerializedName("role")
                val role: String?,
                @SerializedName("serviceId")
                val serviceId: ServiceId?,
                @SerializedName("tagIds")
                val tagIds: List<Int?>?,
                @SerializedName("updatedAt")
                val updatedAt: String?,
                @SerializedName("userId")
                val userId: UserId?,
                @SerializedName("userName")
                val userName: String?
            ) {
                data class DeliveryAddress(
                    @SerializedName("city")
                    val city: String?,
                    @SerializedName("landmark")
                    val landmark: String?,
                    @SerializedName("latitude")
                    val latitude: String?,
                    @SerializedName("line1")
                    val line1: String?,
                    @SerializedName("longitude")
                    val longitude: String?,
                    @SerializedName("pin")
                    val pin: String?,
                    @SerializedName("state")
                    val state: String?
                )

                data class Item(
                    @SerializedName("eqCloths")
                    val eqCloths: Float?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("itemId")
                    val itemId: String?,
                    @SerializedName("itemName")
                    val itemName: String?,
                    @SerializedName("quantity")
                    val quantity: Int?
                )

                data class PartnerId(
                    @SerializedName("address")
                    val address: Address?,
                    @SerializedName("altMobile")
                    val altMobile: String?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("mobile")
                    val mobile: String?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("profile")
                    val profile: String?,
                    @SerializedName("workAddress")
                    val workAddress: WorkAddress?
                ) {
                    data class Address(
                        @SerializedName("city")
                        val city: String?,
                        @SerializedName("landmark")
                        val landmark: String?,
                        @SerializedName("latitude")
                        val latitude: String?,
                        @SerializedName("line1")
                        val line1: String?,
                        @SerializedName("longitude")
                        val longitude: String?,
                        @SerializedName("pin")
                        val pin: String?,
                        @SerializedName("state")
                        val state: String?
                    )

                    data class WorkAddress(
                        @SerializedName("city")
                        val city: String?,
                        @SerializedName("landmark")
                        val landmark: String?,
                        @SerializedName("lattitude")
                        val lattitude: String?,
                        @SerializedName("line1")
                        val line1: String?,
                        @SerializedName("longitude")
                        val longitude: String?,
                        @SerializedName("pin")
                        val pin: String?,
                        @SerializedName("state")
                        val state: String?
                    )
                }

                data class RiderId(
                    @SerializedName("address")
                    val address: Address?,
                    @SerializedName("altMobile")
                    val altMobile: String?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("mobile")
                    val mobile: String?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("profile")
                    val profile: String?
                ) {
                    data class Address(
                        @SerializedName("city")
                        val city: String?,
                        @SerializedName("landmark")
                        val landmark: String?,
                        @SerializedName("latitude")
                        val latitude: String?,
                        @SerializedName("line1")
                        val line1: String?,
                        @SerializedName("longitude")
                        val longitude: String?,
                        @SerializedName("pin")
                        val pin: String?,
                        @SerializedName("state")
                        val state: String?
                    )
                }

                data class ServiceId(
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("serviceImage")
                    val serviceImage: String?,
                    @SerializedName("serviceName")
                    val serviceName: String?
                )

                data class UserId(
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("mobile")
                    val mobile: String?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("profile")
                    val profile: String?
                )
            }

            @Parcelize
            data class Rating(
                @SerializedName("badges")
                val badges: MutableList<Badge>,
                @SerializedName("createdAt")
                val createdAt: String?,
                @SerializedName("desc")
                val desc: String?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isRated")
                val isRated: Boolean?,
                @SerializedName("orderId")
                val orderId: String?,
                @SerializedName("partnerId")
                val partnerId: String?,
                @SerializedName("rating")
                val rating: Int?,
                @SerializedName("rating_for")
                val ratingFor: String?,
                @SerializedName("riderId")
                val riderId: String?,
                @SerializedName("updatedAt")
                val updatedAt: String?,
                @SerializedName("__v")
                val v: Int?
            ):Parcelable {
                @Parcelize
                data class Badge(
                    @SerializedName("badgeId")
                    val badgeId: BadgeId?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("isAwarded")
                    var isAwarded: Boolean?
                ):Parcelable {
                    @Parcelize
                    data class BadgeId(
                        @SerializedName("badgeFor")
                        val badgeFor: String?,
                        @SerializedName("createdAt")
                        val createdAt: String?,
                        @SerializedName("_id")
                        val id: String?,
                        @SerializedName("image")
                        val image: String?,
                        @SerializedName("name")
                        val name: String?,
                        @SerializedName("updatedAt")
                        val updatedAt: String?,
                        @SerializedName("__v")
                        val v: Int?
                    ):Parcelable
                }
            }
        }
    }
}
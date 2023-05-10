package com.laundrybuoy.customer.model.order


import com.google.gson.annotations.SerializedName

data class OrderDetailResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("bill")
        val bill: Bill?,
        @SerializedName("keyPOC")
        val keyPOC: List<KeyPOC?>?,
        @SerializedName("orderDetails")
        val orderDetails: OrderDetails?,
        @SerializedName("orderHistory")
        val orderHistory: List<OrderHistory?>?,
        @SerializedName("ratings")
        val ratings: List<Rating?>?
    ) {
        data class Bill(
            @SerializedName("actualDeliveryCost")
            val actualDeliveryCost: Int?,
            @SerializedName("billedCloths")
            val billedCloths: Int?,
            @SerializedName("billedWeight")
            val billedWeight: Any?,
            @SerializedName("coinDiscount")
            val coinDiscount: Int?,
            @SerializedName("coinUsed")
            val coinUsed: Int?,
            @SerializedName("couponDiscount")
            val couponDiscount: Int?,
            @SerializedName("couponStatus")
            val couponStatus: CouponStatus?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("deliveryCost")
            val deliveryCost: Int?,
            @SerializedName("grossTotal")
            val grossTotal: Int?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("items")
            val items: List<CustomerOrdersModel.Data.Partner.OrderDetails.Item?>?,
            @SerializedName("netTotal")
            val netTotal: Int?,
            @SerializedName("orderId")
            val orderId: String?,
            @SerializedName("otp")
            val otp: Int?,
            @SerializedName("paymentType")
            val paymentType: String?,
            @SerializedName("remainingCoins")
            val remainingCoins: Int?,
            @SerializedName("serviceCost")
            val serviceCost: Int?,
            @SerializedName("subscriptionBalance")
            val subscriptionBalance: Any?,
            @SerializedName("subscriptionStatus")
            val subscriptionStatus: SubscriptionStatus?,
            @SerializedName("totalCloths")
            val totalCloths: Int?,
            @SerializedName("totalWeight")
            val totalWeight: Any?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("usedSubscriptionBalance")
            val usedSubscriptionBalance: Int?,
            @SerializedName("__v")
            val v: Int?
        ) {
            data class CouponStatus(
                @SerializedName("id")
                val id: Any?,
                @SerializedName("message")
                val message: String?,
                @SerializedName("name")
                val name: Any?
            )

            data class SubscriptionStatus(
                @SerializedName("id")
                val id: Any?,
                @SerializedName("message")
                val message: String?,
                @SerializedName("name")
                val name: Any?,
                @SerializedName("subBalance")
                val subBalance: Any?,
                @SerializedName("subType")
                val subType: Any?,
                @SerializedName("usrSub")
                val usrSub: Any?
            )
        }

        data class KeyPOC(
            @SerializedName("details")
            val details: Details?,
            @SerializedName("name")
            val name: String?
        ) {
            data class Details(
                @SerializedName("action")
                val action: String?,
                @SerializedName("actualTime")
                val actualTime: String?,
                @SerializedName("createdAt")
                val createdAt: String?,
                @SerializedName("expectedTime")
                val expectedTime: String?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isLate")
                val isLate: Boolean?,
                @SerializedName("ordNum")
                val ordNum: String?,
                @SerializedName("orderId")
                val orderId: String?,
                @SerializedName("orderStatus")
                val orderStatus: Int?,
                @SerializedName("partnerId")
                val partnerId: PartnerId?,
                @SerializedName("riderId")
                val riderId: RiderId?,
                @SerializedName("role")
                val role: String?,
                @SerializedName("updatedAt")
                val updatedAt: String?,
                @SerializedName("userId")
                val userId: UserId?,
                @SerializedName("__v")
                val v: Int?
            ) {
                data class PartnerId(
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
                )

                data class RiderId(
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
                )

                data class UserId(
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("mobile")
                    val mobile: String?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("profile")
                    val profile: String?,
                    @SerializedName("tagId")
                    val tagId: Int?
                )
            }
        }

        data class OrderDetails(
            @SerializedName("adminCreated")
            val adminCreated: Boolean?,
            @SerializedName("approxCloths")
            val approxCloths: String?,
            @SerializedName("bagIds")
            val bagIds: List<String?>?,
            @SerializedName("billId")
            val billId: String?,
            @SerializedName("couponId")
            val couponId: Any?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("deliveredOn")
            val deliveredOn: String?,
            @SerializedName("deliveryAddress")
            val deliveryAddress: DeliveryAddress?,
            @SerializedName("deliveryDate")
            val deliveryDate: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isDelivered")
            val isDelivered: Boolean?,
            @SerializedName("isPrime")
            val isPrime: Boolean?,
            @SerializedName("items")
            val items: List<CustomerOrdersModel.Data.Partner.OrderDetails.Item?>?,
            @SerializedName("nextAction")
            val nextAction: String?,
            @SerializedName("ordNum")
            val ordNum: String?,
            @SerializedName("orderDate")
            val orderDate: String?,
            @SerializedName("orderStatus")
            val orderStatus: Int?,
            @SerializedName("orderType")
            val orderType: String?,
            @SerializedName("otp")
            val otp: Any?,
            @SerializedName("package")
            val packageX: Boolean?,
            @SerializedName("partnerId")
            val partnerId: PartnerId?,
            @SerializedName("pickupDate")
            val pickupDate: String?,
            @SerializedName("ratings")
            val ratings: List<String?>?,
            @SerializedName("riderId")
            val riderId: RiderId?,
            @SerializedName("role")
            val role: String?,
            @SerializedName("serviceId")
            val serviceId: ServiceId?,
            @SerializedName("subscriptionId")
            val subscriptionId: Any?,
            @SerializedName("tagIds")
            val tagIds: List<Int?>?,
            @SerializedName("timeSlot")
            val timeSlot: String?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("userId")
            val userId: UserId?,
            @SerializedName("userName")
            val userName: String?,
            @SerializedName("__v")
            val v: Int?
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

            data class PartnerId(
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
            )

            data class RiderId(
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
            )

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

        data class OrderHistory(
            @SerializedName("action")
            val action: String?,
            @SerializedName("actualTime")
            val actualTime: String?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("expectedTime")
            val expectedTime: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isLate")
            val isLate: Boolean?,
            @SerializedName("ordNum")
            val ordNum: String?,
            @SerializedName("orderId")
            val orderId: String?,
            @SerializedName("orderStatus")
            val orderStatus: Int?,
            @SerializedName("partnerId")
            val partnerId: PartnerId?,
            @SerializedName("riderId")
            val riderId: RiderId?,
            @SerializedName("role")
            val role: String?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("userId")
            val userId: UserId?,
            @SerializedName("__v")
            val v: Int?
        ) {
            data class PartnerId(
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
            )

            data class RiderId(
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
            )

            data class UserId(
                @SerializedName("_id")
                val id: String?,
                @SerializedName("mobile")
                val mobile: String?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("profile")
                val profile: String?,
                @SerializedName("tagId")
                val tagId: Int?
            )
        }

        data class Rating(
            @SerializedName("badges")
            val badges: List<Badge?>?,
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
        ) {
            data class Badge(
                @SerializedName("badgeId")
                val badgeId: BadgeId?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isAwarded")
                val isAwarded: Boolean?
            ) {
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
                )
            }
        }
    }
}
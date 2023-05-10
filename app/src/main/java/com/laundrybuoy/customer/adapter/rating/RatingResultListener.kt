package com.laundrybuoy.customer.adapter.rating

interface RatingResultListener {
    fun onRatingSuccess()
    fun onRatingError()
    fun onRatingClosed()
}
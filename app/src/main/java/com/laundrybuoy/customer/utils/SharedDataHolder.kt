package com.laundrybuoy.customer.utils

import com.laundrybuoy.customer.model.order.CustomerOrdersModel

object SharedDataHolder {
    var ratingsList: List<CustomerOrdersModel.Data.Partner.Rating>? = null
}
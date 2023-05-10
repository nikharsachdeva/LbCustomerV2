package com.laundrybuoy.customer.model.address

data class AddressTypeModel(
    var type: String,
    var image: Int,
    var isSelected: Boolean? = false,
)

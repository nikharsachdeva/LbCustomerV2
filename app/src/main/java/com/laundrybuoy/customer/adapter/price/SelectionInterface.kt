package com.laundrybuoy.customer.adapter.price

import com.laundrybuoy.customer.model.price.InventoryModel


interface SelectionInterface {
    fun newListIsHere(mutableList: MutableList<InventoryModel.Data.Item>)
}
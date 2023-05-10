package com.laundrybuoy.customer.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemOrderItemsBinding
import com.laundrybuoy.customer.model.order.CustomerOrdersModel.Data.Partner.OrderDetails.Item

class CustomerOrderItemsAdapter() :
    ListAdapter<Item, CustomerOrderItemsAdapter.ItemsViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val binding =
            RowItemOrderItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class ItemsViewHolder(private val binding: RowItemOrderItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, position: Int) {
            binding.orderItemQtyTv.text=item.quantity.toString()+" x "
            binding.orderItemNameTv.text=item.itemName.toString()
        }
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
package com.laundrybuoy.customer.adapter.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemSelectQtyBinding
import com.laundrybuoy.customer.model.schedule.OrderQtyModel

class SelectQtyAdapter() :
    ListAdapter<OrderQtyModel.OrderQtyModelItem, SelectQtyAdapter.AddressViewHolder>(
        ComparatorDiffUtil()
    ) {
    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            RowItemSelectQtyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class AddressViewHolder(private val binding: RowItemSelectQtyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(qty: OrderQtyModel.OrderQtyModelItem, position: Int) {

            if (checkedPosition == -1) {
                binding.isSelected = false
            } else {
                binding.isSelected = checkedPosition == adapterPosition
            }

            binding.qtyNameTv.text = qty.qty+" clothes"

            binding.qtyRootRl.setOnClickListener {
                binding.isSelected = true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
            }

        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<OrderQtyModel.OrderQtyModelItem>() {
        override fun areItemsTheSame(
            oldItem: OrderQtyModel.OrderQtyModelItem,
            newItem: OrderQtyModel.OrderQtyModelItem,
        ): Boolean {
            return oldItem.qty == newItem.qty
        }

        override fun areContentsTheSame(
            oldItem: OrderQtyModel.OrderQtyModelItem,
            newItem: OrderQtyModel.OrderQtyModelItem,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
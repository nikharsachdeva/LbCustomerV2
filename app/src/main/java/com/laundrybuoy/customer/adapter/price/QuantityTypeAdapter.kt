package com.laundrybuoy.customer.adapter.price

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemSelectServiceBinding
import com.laundrybuoy.customer.model.price.QuantityTypeModel
import com.laundrybuoy.customer.model.schedule.ServiceModel
import com.laundrybuoy.customer.utils.loadImageWithGlide

class QuantityTypeAdapter(val listener: OnQtySelect) :
    ListAdapter<QuantityTypeModel.Data, QuantityTypeAdapter.AddressViewHolder>(
        ComparatorDiffUtil()
    ) {

    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            RowItemSelectServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class AddressViewHolder(private val binding: RowItemSelectServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(qtyItem: QuantityTypeModel.Data, position: Int) {

            if (checkedPosition == -1) {
                binding.isSelected = false
            } else {
                binding.isSelected = checkedPosition == adapterPosition
            }

            binding.serviceIconIv.setImageResource(qtyItem.qtyImage!!)
            binding.serviceNameTv.text = qtyItem.qtyType ?: ""

            binding.serviceRootRl.setOnClickListener {
                binding.isSelected = true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                listener.typeSelected(qtyItem)
            }

        }
    }

    interface OnQtySelect {
        fun typeSelected(qtyItem: QuantityTypeModel.Data)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<QuantityTypeModel.Data>() {
        override fun areItemsTheSame(
            oldItem: QuantityTypeModel.Data,
            newItem: QuantityTypeModel.Data,
        ): Boolean {
            return oldItem.id== newItem.id
        }

        override fun areContentsTheSame(
            oldItem: QuantityTypeModel.Data,
            newItem: QuantityTypeModel.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
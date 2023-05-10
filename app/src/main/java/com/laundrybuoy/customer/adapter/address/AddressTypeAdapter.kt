package com.laundrybuoy.customer.adapter.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemAddressTypeBinding
import com.laundrybuoy.customer.model.address.AddressTypeModel

class AddressTypeAdapter(val clickListener: TypeClickListener) :
    ListAdapter<AddressTypeModel, AddressTypeAdapter.AddressViewHolder>(
        ComparatorDiffUtil()
    ) {

    private var checkedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            RowItemAddressTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val type = getItem(position)
        type?.let {
            holder.bind(it, position)
        }

    }

    inner class AddressViewHolder(private val binding: RowItemAddressTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(type: AddressTypeModel, position: Int) {

            if (checkedPosition == -1) {
                binding.isSelected = false
                type.isSelected = false
            } else {
                binding.isSelected = checkedPosition == adapterPosition
                type.isSelected = checkedPosition == adapterPosition
            }

            binding.addressTypeTitleIv.text=type.type
            binding.addressTypeIconIv.setImageResource(type.image)

            binding.addressTypeRootRl.setOnClickListener {
                binding.isSelected = true
                type.isSelected=true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                clickListener.onTypeSelected(type)
            }

        }
    }

    interface TypeClickListener{
        fun onTypeSelected(type: AddressTypeModel)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<AddressTypeModel>() {
        override fun areItemsTheSame(
            oldItem: AddressTypeModel,
            newItem: AddressTypeModel,
        ): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(
            oldItem: AddressTypeModel,
            newItem: AddressTypeModel,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
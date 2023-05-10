package com.laundrybuoy.customer.adapter.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemUserAddressBinding
import com.laundrybuoy.customer.model.address.GetUserAddressResponse

class UserAddressAdapter(val listener : AddressClickListener) :
    ListAdapter<GetUserAddressResponse.Data, UserAddressAdapter.AddressViewHolder>(
        ComparatorDiffUtil()
    ) {

    private var checkedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            RowItemUserAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class AddressViewHolder(private val binding: RowItemUserAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: GetUserAddressResponse.Data, position: Int) {

            if (checkedPosition == -1) {
                binding.isSelected = false
            } else {
                binding.isSelected = checkedPosition == adapterPosition
            }

            val addressString = address.address?.line1 + " " +
                    address.address?.landmark + " " +
                    address.address?.city + " " +
                    address.address?.pin

            binding.itemAddressPhoneTv.text = "${address.altMobile}"
            binding.itemAddressTypeTv.text = "${address.addressType?.capitalize()}"
            binding.itemAddressTv.text = addressString

            binding.addressRoot.setOnClickListener {
                binding.isSelected = true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                listener.onAddressSelected(address)
            }

        }
    }

    interface AddressClickListener{
        fun onAddressSelected(address: GetUserAddressResponse.Data)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<GetUserAddressResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: GetUserAddressResponse.Data,
            newItem: GetUserAddressResponse.Data,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetUserAddressResponse.Data,
            newItem: GetUserAddressResponse.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
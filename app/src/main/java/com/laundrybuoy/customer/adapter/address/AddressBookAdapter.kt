package com.laundrybuoy.customer.adapter.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemAddressBookBinding
import com.laundrybuoy.customer.model.address.GetUserAddressResponse
import com.laundrybuoy.customer.utils.getAddressIcon
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible

class AddressBookAdapter(val clickListeners: AddressClickListeners) :
    ListAdapter<GetUserAddressResponse.Data, AddressBookAdapter.AddressViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            RowItemAddressBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class AddressViewHolder(private val binding: RowItemAddressBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: GetUserAddressResponse.Data, position: Int) {

            binding.itemAddressTypeIcon.setImageResource(address.addressType.toLowerCase().getAddressIcon())
            binding.itemAddressTypeTv.text=address?.addressType?.capitalize()

            if(address.isDefault==true){
                binding.defaultRootRl.makeViewVisible()
                binding.itemAddressDefaultTv.makeViewGone()
            }else{
                binding.defaultRootRl.makeViewGone()
                binding.itemAddressDefaultTv.makeViewVisible()
            }

            val addressString = address.address?.line1 + " " +
                    address.address?.landmark + " " +
                    address.address?.city + " " + address.address?.state + " " +
                    address.address?.pin

            binding.itemFullAddressTv.text = addressString
            binding.itemAddressPhoneTv.text = "Phone Number : ${address.altMobile}"

            binding.itemAddressEditTv.setOnClickListener {
                clickListeners.onAddressEdit(address)
            }

            binding.itemAddressDeleteTv.setOnClickListener {
                clickListeners.onAddressDelete(address)
            }

            binding.itemAddressDefaultTv.setOnClickListener {
                clickListeners.onAddressDefault(address)
            }

        }
    }

    interface AddressClickListeners{
        fun onAddressEdit(address: GetUserAddressResponse.Data)
        fun onAddressDelete(address: GetUserAddressResponse.Data)
        fun onAddressDefault(address: GetUserAddressResponse.Data)
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
package com.laundrybuoy.customer.adapter.refer

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemReferFriendsBinding
import com.laundrybuoy.customer.model.refer.ReferedFriendModel
import com.laundrybuoy.customer.utils.RandomColors

class ReferFriendsAdapter() :
    ListAdapter<ReferedFriendModel.ReferedFriendModelItem, ReferFriendsAdapter.AddressViewHolder>(
        ComparatorDiffUtil()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            RowItemReferFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class AddressViewHolder(private val binding: RowItemReferFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friend: ReferedFriendModel.ReferedFriendModelItem, position: Int) {

            binding.itemReferName.text = friend.name

            val initials = friend.name?.trim()?.splitToSequence(" ", limit = 2)
                ?.map { println(it); it.first() }
                ?.joinToString("")?.toUpperCase()

            binding.itemReferInitialsTv.text = initials
            binding.itemReferInitialsTv.backgroundTintList = ColorStateList.valueOf(RandomColors().getColor())

            if(friend.hasOrdered==true){
                binding.itemReferStatus.text = "Ordered"
            }else{
                binding.itemReferStatus.text = "Joined"
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<ReferedFriendModel.ReferedFriendModelItem>() {
        override fun areItemsTheSame(
            oldItem: ReferedFriendModel.ReferedFriendModelItem,
            newItem: ReferedFriendModel.ReferedFriendModelItem,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ReferedFriendModel.ReferedFriendModelItem,
            newItem: ReferedFriendModel.ReferedFriendModelItem,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
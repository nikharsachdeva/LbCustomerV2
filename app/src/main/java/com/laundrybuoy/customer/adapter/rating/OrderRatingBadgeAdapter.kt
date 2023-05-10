package com.laundrybuoy.customer.adapter.rating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemBadgeBinding
import com.laundrybuoy.customer.model.order.CustomerOrdersModel.Data.Partner.Rating.Badge
import com.laundrybuoy.customer.model.rating.BadgeList.BadgeItem
import com.laundrybuoy.customer.utils.loadImageWithGlide

class OrderRatingBadgeAdapter :
    ListAdapter<Badge, OrderRatingBadgeAdapter.BadgesViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgesViewHolder {
        val binding =
            RowItemBadgeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return BadgesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BadgesViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class BadgesViewHolder(val binding: RowItemBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(badge: Badge, position: Int) {
            binding.isSelected = badge.isAwarded==true
            binding.itemBadgeRootRl.setOnClickListener {
                badge.isAwarded = !badge.isAwarded!!
                binding.isSelected = badge.isAwarded
                notifyDataSetChanged()
            }

            binding.itemBadgeTitleTv.text = badge.badgeId?.name?:""
            binding.itemBadgeIconIv.loadImageWithGlide(badge.badgeId?.image?:"")
        }
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<Badge>() {
        override fun areItemsTheSame(
            oldItem: Badge,
            newItem: Badge
        ): Boolean {
            return oldItem?.badgeId?.id == newItem.badgeId?.id
        }

        override fun areContentsTheSame(
            oldItem: Badge,
            newItem: Badge
        ): Boolean {
            return oldItem == newItem
        }

    }
}
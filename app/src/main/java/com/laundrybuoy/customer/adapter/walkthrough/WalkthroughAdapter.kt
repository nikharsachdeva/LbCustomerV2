package com.laundrybuoy.customer.adapter.walkthrough

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemWalkthroughBinding
import com.laundrybuoy.customer.model.walkthrough.WalkthroughResponse
import com.laundrybuoy.customer.utils.loadImageWithGlide

class WalkthroughAdapter() :
    ListAdapter<WalkthroughResponse.Data, WalkthroughAdapter.WalkthroughViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkthroughViewHolder {
        val binding =
            RowItemWalkthroughBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalkthroughViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalkthroughViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }
    }

    inner class WalkthroughViewHolder(private val binding: RowItemWalkthroughBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(walkthrough: WalkthroughResponse.Data, position: Int) {
            binding.itemWalkthroughIv.loadImageWithGlide(walkthrough.image ?: "")

            binding.itemWalkthroughHeading.text = walkthrough.heading
            binding.itemWalkthroughSubheading.text = walkthrough.description
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<WalkthroughResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: WalkthroughResponse.Data,
            newItem: WalkthroughResponse.Data,
        ): Boolean {
            return oldItem.heading == newItem.heading
        }

        override fun areContentsTheSame(
            oldItem: WalkthroughResponse.Data,
            newItem: WalkthroughResponse.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
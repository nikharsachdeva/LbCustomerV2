package com.laundrybuoy.customer.adapter.plus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemPlusSubPointBinding
import com.laundrybuoy.customer.utils.colorTransition2

class PlusPointerAdapter(var colorHeading: Int?=null) :
    ListAdapter<String, PlusPointerAdapter.CouponsViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponsViewHolder {
        val binding =
            RowItemPlusSubPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponsViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class CouponsViewHolder(private val binding: RowItemPlusSubPointBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(point: String, position: Int) {
            binding.pointerTv.text = point ?: ""
            binding.pointerTv.colorTransition2(colorHeading!!)
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
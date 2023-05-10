package com.laundrybuoy.customer.adapter.plus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemPlusIntroBinding
import com.laundrybuoy.customer.model.plus.PlusIntroPointsModel

class PlusIntroPointsAdapter() :
    ListAdapter<PlusIntroPointsModel.Data, PlusIntroPointsAdapter.PlusIntroViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlusIntroViewHolder {
        val binding =
            RowItemPlusIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlusIntroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlusIntroViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }
    }

    inner class PlusIntroViewHolder(private val binding: RowItemPlusIntroBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plusPoints: PlusIntroPointsModel.Data, position: Int) {
            binding.plusIntroHeading.text = plusPoints.heading
            binding.plusIntroSubheading.text = plusPoints.subHeading
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<PlusIntroPointsModel.Data>() {
        override fun areItemsTheSame(
            oldItem: PlusIntroPointsModel.Data,
            newItem: PlusIntroPointsModel.Data,
        ): Boolean {
            return oldItem.heading == newItem.heading
        }

        override fun areContentsTheSame(
            oldItem: PlusIntroPointsModel.Data,
            newItem: PlusIntroPointsModel.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
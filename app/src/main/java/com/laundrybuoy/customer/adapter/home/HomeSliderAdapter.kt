package com.laundrybuoy.customer.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemSliderBinding
import com.laundrybuoy.customer.model.home.HomeScreenModel
import com.laundrybuoy.customer.utils.loadImageWithGlide

class HomeSliderAdapter() :
    ListAdapter<HomeScreenModel.Data.Data, HomeSliderAdapter.HomeSliderViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSliderViewHolder {
        val binding =
            RowItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeSliderViewHolder, position: Int) {
        val sliderItem = getItem(position)
        sliderItem?.let {
            holder.bind(it, position)
        }
    }

    inner class HomeSliderViewHolder(private val binding: RowItemSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sliderItem: HomeScreenModel.Data.Data, position: Int) {
            binding.rowItemSliderIv.loadImageWithGlide(sliderItem.image.toString())
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<HomeScreenModel.Data.Data>() {
        override fun areItemsTheSame(
            oldItem: HomeScreenModel.Data.Data,
            newItem: HomeScreenModel.Data.Data,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: HomeScreenModel.Data.Data,
            newItem: HomeScreenModel.Data.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
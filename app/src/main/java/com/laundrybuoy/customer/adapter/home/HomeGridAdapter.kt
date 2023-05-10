package com.laundrybuoy.customer.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemHomeGridBinding
import com.laundrybuoy.customer.model.home.HomeScreenModel.Data.Data
import com.laundrybuoy.customer.utils.loadImageWithGlide

class HomeGridAdapter() :
    ListAdapter<Data, HomeGridAdapter.GridViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding =
            RowItemHomeGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val gridItem = getItem(position)
        gridItem?.let {
            holder.bind(it, position)
        }

    }

    inner class GridViewHolder(private val binding: RowItemHomeGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gridItem: Data, position: Int) {
            binding.itemGridIv.loadImageWithGlide(gridItem.image.toString())
            binding.itemGridTv.text=gridItem.title
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(
            oldItem: Data,
            newItem: Data,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Data,
            newItem: Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
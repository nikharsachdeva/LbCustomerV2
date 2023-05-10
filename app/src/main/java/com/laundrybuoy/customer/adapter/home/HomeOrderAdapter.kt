package com.laundrybuoy.customer.adapter.home

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.databinding.RowItemOngoingServiceBinding
import com.laundrybuoy.customer.model.home.HomeScreenModel.Data.Data
import com.laundrybuoy.customer.utils.getFormattedDateWithTime

class HomeOrderAdapter() :
    ListAdapter<Data, HomeOrderAdapter.OrderViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            RowItemOngoingServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class OrderViewHolder(private val binding: RowItemOngoingServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Data, position: Int) {

            binding.itemOngoingNameTv.text = service.ordService
            binding.itemOngoingDateTv.text = service.date?.getFormattedDateWithTime()
            var progress = 0
            if (service.status!! >= 0) {
                progress = ((service.status / 10) * 100).toInt()
            }
            binding.itemOngoingProgressPb.progress = progress

            when (service.status) {

                0, 1 -> {
                    binding.itemOngoingStatusTv.text = "Waiting"
                    binding.idOneTv.setTextColor(binding.root.context.resources.getColor(R.color.white))
                    binding.stageOneTv.setTextColor(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                    binding.idOneTv.backgroundTintList =
                        ColorStateList.valueOf(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                }

                2, 3 -> {
                    binding.itemOngoingStatusTv.text = "Ongoing"
                    binding.idOneTv.setTextColor(binding.root.context.resources.getColor(R.color.white))
                    binding.stageOneTv.setTextColor(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                    binding.idOneTv.backgroundTintList =
                        ColorStateList.valueOf(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                }

                4 -> {
                    binding.itemOngoingStatusTv.text = "Done"
                    binding.idOneTv.setTextColor(binding.root.context.resources.getColor(R.color.white))
                    binding.stageOneTv.setTextColor(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                    binding.idOneTv.backgroundTintList =
                        ColorStateList.valueOf(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                }

                5 -> {
                    binding.itemOngoingStatusTv.text = "Waiting"
                    binding.idTwoTv.setTextColor(binding.root.context.resources.getColor(R.color.white))
                    binding.stageTwoTv.setTextColor(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                    binding.idTwoTv.backgroundTintList =
                        ColorStateList.valueOf(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))

                }

                6 -> {
                    binding.itemOngoingStatusTv.text = "Ongoing"
                    binding.idTwoTv.setTextColor(binding.root.context.resources.getColor(R.color.white))
                    binding.stageTwoTv.setTextColor(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                    binding.idTwoTv.backgroundTintList =
                        ColorStateList.valueOf(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))

                }

                7 -> {
                    binding.itemOngoingStatusTv.text = "Done"
                    binding.idThreeTv.setTextColor(binding.root.context.resources.getColor(R.color.white))
                    binding.stageThreeTv.setTextColor(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                    binding.idThreeTv.backgroundTintList =
                        ColorStateList.valueOf(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                }

                8, 9 -> {
                    binding.itemOngoingStatusTv.text = "Ongoing"
                    binding.idFourTv.setTextColor(binding.root.context.resources.getColor(R.color.white))
                    binding.stageFourTv.setTextColor(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                    binding.idFourTv.backgroundTintList =
                        ColorStateList.valueOf(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                }

                10 -> {
                    binding.itemOngoingStatusTv.text = "Done"
                    binding.idFourTv.setTextColor(binding.root.context.resources.getColor(R.color.white))
                    binding.stageFourTv.setTextColor(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                    binding.idFourTv.backgroundTintList =
                        ColorStateList.valueOf(binding.root.context.resources.getColor(R.color.colorOngoingHighlight))
                }

            }

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
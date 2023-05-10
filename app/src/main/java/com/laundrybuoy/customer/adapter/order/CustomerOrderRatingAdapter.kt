package com.laundrybuoy.customer.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemOrderRatingBinding
import com.laundrybuoy.customer.model.order.CustomerOrdersModel.Data.Partner.Rating
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible

class CustomerOrderRatingAdapter(val clickListener: RatingClickListener) :
    ListAdapter<Rating, CustomerOrderRatingAdapter.ItemsViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val binding =
            RowItemOrderRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class ItemsViewHolder(private val binding: RowItemOrderRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Rating, position: Int) {

            var ratedFor = when (item.ratingFor) {
                "pickupRider" -> "Pickup"
                "deliveryRider" -> "Delivery"
                "partner" -> "Laundry"
                else -> ""
            }

            if (item.isRated == false) {
                binding.orderRatingUnavailableTv.makeViewVisible()
                binding.orderRatingUnavailableTv.text = "You haven't rated $ratedFor"
                binding.orderRatingAvailableRl.makeViewGone()
            } else {
                binding.orderRatingUnavailableTv.makeViewGone()
                binding.orderRatingUnavailableTv.text = ""
                binding.orderRatingAvailableRl.makeViewVisible()
                binding.orderRatingForTv.text = "$ratedFor Rating :"
                binding.orderRatingValueTv.text = "${item.rating}"
            }

            binding.orderRatingRootRl.setOnClickListener {
                clickListener.onRatingClicked()
            }

        }
    }

    interface RatingClickListener{
        fun onRatingClicked()
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Rating>() {
        override fun areItemsTheSame(
            oldItem: Rating,
            newItem: Rating,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Rating,
            newItem: Rating,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
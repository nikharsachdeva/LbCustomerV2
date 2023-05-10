package com.laundrybuoy.customer.adapter.schedule

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemQuickCouponBinding
import com.laundrybuoy.customer.model.schedule.CouponModel
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible

class QuickCouponAdapter(val listener: OnCouponSelect) :
    ListAdapter<CouponModel.CouponModelItem, QuickCouponAdapter.AddressViewHolder>(
        ComparatorDiffUtil()
    ) {

    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            RowItemQuickCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }
    }

    inner class AddressViewHolder(private val binding: RowItemQuickCouponBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coupon: CouponModel.CouponModelItem, position: Int) {

            if (checkedPosition == -1) {
                binding.isSelected = false
            } else {
                binding.isSelected = checkedPosition == adapterPosition
            }

            if (coupon.isExpanded==true) {
                binding.itemFaqArrowDown.makeViewGone()
                binding.itemFaqArrowUp.makeViewVisible()
                binding.rowItemCouponDescTv.makeViewVisible()
            } else {
                binding.itemFaqArrowDown.makeViewVisible()
                binding.itemFaqArrowUp.makeViewGone()
                binding.rowItemCouponDescTv.makeViewGone()
            }

            binding.rowItemCouponTv.text = "TOBEUPDATE"
            binding.rowItemCouponNameTv.text = coupon.name ?: ""
            binding.rowItemCouponDescTv.text = coupon.description ?: ""

            binding.copyCouponLl.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#50" + coupon.hex))
            binding.copyFrameFl.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#" + coupon.hex))

            binding.couponRootRl.setOnClickListener {
                binding.isSelected = true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                listener.couponSelected(coupon)
            }

            /*
            binding.rowItemCouponDescTv.setOnClickListener {
                binding.isSelected = true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                listener.couponSelected(coupon)
            }

             */


            binding.copyFrameFl.setOnClickListener {
                listener.couponCopied(coupon)
            }

            binding.itemArrowFrame.setOnClickListener {
                coupon.isExpanded = !coupon.isExpanded!!
                notifyItemChanged(position)
            }

        }
    }

    interface OnCouponSelect {
        fun couponSelected(coupon: CouponModel.CouponModelItem)
        fun couponCopied(coupon: CouponModel.CouponModelItem)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<CouponModel.CouponModelItem>() {
        override fun areItemsTheSame(
            oldItem: CouponModel.CouponModelItem,
            newItem: CouponModel.CouponModelItem,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CouponModel.CouponModelItem,
            newItem: CouponModel.CouponModelItem,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
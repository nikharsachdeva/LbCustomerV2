package com.laundrybuoy.customer.adapter.subscription

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemSubscriptionBoughtBinding
import com.laundrybuoy.customer.databinding.RowItemSubscriptionUsedBinding
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel.Data.Partner
import com.laundrybuoy.customer.utils.getFormattedDateWithTime

class CustomerSubscriptionPagingAdapter(private val onClickListener: OnClickInterface) :
    PagingDataAdapter<Partner, RecyclerView.ViewHolder>(
        COMPARATOR
    ) {

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {

        when (holder) {
            is SubBoughtViewHolder -> {
                val boughtItem = getItem(position) as Partner
                holder.bind(boughtItem)
            }
            is SubUsedViewHolder -> {
                val usedItem = getItem(position) as Partner
                holder.bind(usedItem)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_BOUGHT -> SubBoughtViewHolder.from(parent)
            TYPE_USED -> SubUsedViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    class SubBoughtViewHolder private constructor(val binding: RowItemSubscriptionBoughtBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Partner) {
            val subType = if (item.subscriptionId?.quantityType == "weight") "kg" else "cloth"
            binding.subNameTv.text = item.subscriptionId?.name
            binding.subDescTv.text = item.subscriptionId?.description
            binding.subPriceTv.text = "₹" + item.subscriptionId?.actualPrice?.toString()
            binding.subValidityTv.text = "/" + item.subscriptionId?.validity?.toString() + "days"
            binding.startDateTv.text = item.transactionDate?.getFormattedDateWithTime()
            binding.endDateTv.text = item.transactionDate?.getFormattedDateWithTime()
            binding.subTranClosingTv.text = "Closing balance : " + item.balanceCurrent + " $subType"
            binding.subTranAmt.text = "+${item.subscriptionId?.quantity} $subType"
        }

        companion object {
            fun from(parent: ViewGroup): SubBoughtViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    RowItemSubscriptionBoughtBinding.inflate(layoutInflater, parent, false)
                return SubBoughtViewHolder(binding)
            }
        }
    }


    class SubUsedViewHolder private constructor(val binding: RowItemSubscriptionUsedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Partner) {
            val subType = if (item.subscriptionId?.quantityType == "weight") "kg" else "cloth"
            binding.subTranHeadingTv.text = item.action ?: ""
            binding.subTranSubheadingTv.text = item.transactionDate?.getFormattedDateWithTime()
            binding.subTranAmt.text = "-${item.balanceUsed} $subType"
            binding.subTranClosingTv.text = "Closing balance : " + item.balanceCurrent + " $subType"
        }

        companion object {
            fun from(parent: ViewGroup): SubUsedViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemSubscriptionUsedBinding.inflate(layoutInflater, parent, false)
                return SubUsedViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (getItem(position)?.orderId == null) {
            TYPE_BOUGHT
        } else if (getItem(position)?.orderId?.id != null) {
            TYPE_USED
        } else {
            -1
        }
    }

    interface OnClickInterface {
        fun onSelected(order: Partner, position: Int)
    }

    companion object {
        private val COMPARATOR =
            object : DiffUtil.ItemCallback<Partner>() {
                override fun areItemsTheSame(
                    oldItem: Partner,
                    newItem: Partner,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Partner,
                    newItem: Partner,
                ): Boolean {
                    return oldItem == newItem
                }
            }

        private const val TYPE_BOUGHT = 0
        private const val TYPE_USED = 1

    }
}






















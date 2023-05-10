package com.laundrybuoy.customer.adapter.refer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemFaqBinding
import com.laundrybuoy.customer.model.refer.FaqModel
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible

class ReferFaqAdapter() :
    ListAdapter<FaqModel.FaqModelItem, ReferFaqAdapter.FaqViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding =
            RowItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class FaqViewHolder(private val binding: RowItemFaqBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(faq: FaqModel.FaqModelItem, position: Int) {

            binding.rowFaqQuestion.text = faq.question
            binding.rowFaqAnswer.text = faq.answer

            if (faq.isExpanded == true) {
                binding.itemFaqArrowDown.makeViewGone()
                binding.itemFaqArrowUp.makeViewVisible()
                binding.itemFaqDivider.makeViewVisible()
                binding.rowFaqAnswer.makeViewVisible()
            } else {
                binding.itemFaqArrowDown.makeViewVisible()
                binding.itemFaqArrowUp.makeViewGone()
                binding.itemFaqDivider.makeViewGone()
                binding.rowFaqAnswer.makeViewGone()
            }

            binding.rowFaqChildRl.setOnClickListener {
                faq.isExpanded=!faq.isExpanded
                notifyItemChanged(position)

            }

        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<FaqModel.FaqModelItem>() {
        override fun areItemsTheSame(
            oldItem: FaqModel.FaqModelItem,
            newItem: FaqModel.FaqModelItem,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FaqModel.FaqModelItem,
            newItem: FaqModel.FaqModelItem,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
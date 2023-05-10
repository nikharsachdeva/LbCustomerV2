package com.laundrybuoy.customer.adapter.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.databinding.RowItemSelectDateBinding
import com.laundrybuoy.customer.model.schedule.PickupDateResponse
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewInVisible
import com.laundrybuoy.customer.utils.makeViewVisible

class QuickDateAdapter(val clickListener: DateSelectListener) :
    ListAdapter<PickupDateResponse.Data, QuickDateAdapter.PickupDateViewHolder>(
        ComparatorDiffUtil()
    ) {

    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickupDateViewHolder {
        val binding =
            RowItemSelectDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickupDateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PickupDateViewHolder, position: Int) {
        val date = getItem(position)
        date?.let {
            holder.bind(it, position)
        }

    }

    inner class PickupDateViewHolder(private val binding: RowItemSelectDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: PickupDateResponse.Data, position: Int) {

            if (date.isValid == true) {
                binding.dateClosedReasonTv.makeViewInVisible()
                binding.dateClosedReasonTv.text=""
                if (checkedPosition == -1) {
                    binding.isSelected = false
                } else {
                    binding.isSelected = checkedPosition == adapterPosition
                }

                binding.serviceRootRl.setOnClickListener {
                    binding.isSelected = true
                    if (checkedPosition != adapterPosition) {
                        notifyItemChanged(checkedPosition)
                        checkedPosition = adapterPosition
                    }

                    clickListener.onDateSelected(date)
                }

            } else {
                binding.dateClosedReasonTv.makeViewVisible()
                binding.dateClosedReasonTv.text=binding.root.context.getString(R.string.same_day_pickup)+" "+date.offset
                binding.dateSlotChildRl.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.colorPlaceholder)
            }

            binding.dateOfWeekTv.text = date.dateOfSlot
            binding.dayOfWeekTv.text = date.day?.subSequence(0, 3)
            binding.monthOfWeekTv.text = date.monthOfSlot
        }
    }

    interface DateSelectListener {
        fun onDateSelected(date: PickupDateResponse.Data)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<PickupDateResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: PickupDateResponse.Data,
            newItem: PickupDateResponse.Data,
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: PickupDateResponse.Data,
            newItem: PickupDateResponse.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
package com.laundrybuoy.customer.adapter.scratch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel.Data.Partner
import com.laundrybuoy.customer.utils.getFormattedDateWithTime
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerScratchPagingAdapter(private val onClickListener: OnClickInterface) :
    PagingDataAdapter<Partner, CustomerScratchPagingAdapter.ScratchCardViewHolder>(
        COMPARATOR
    ) {

    class ScratchCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scratchRevealedRl: RelativeLayout = itemView.findViewById(R.id.scratchRevealed_rl)
        val scratchUnRevealedRl: RelativeLayout = itemView.findViewById(R.id.scratchUnRevealed_rl)
        val itemScratchCoinsTv: TextView = itemView.findViewById(R.id.item_scratch_coins_tv)
        val itemScratchDateTv: TextView = itemView.findViewById(R.id.item_scratch_date_tv)
        val itemScratchDescTv: TextView = itemView.findViewById(R.id.item_scratch_desc_tv)
        val itemScratchRootCv: CardView = itemView.findViewById(R.id.itemScratchRoot_cv)
    }

    override fun onBindViewHolder(holder: ScratchCardViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            if (item.isOpened == true) {
                holder.scratchRevealedRl.makeViewVisible()
                holder.scratchUnRevealedRl.makeViewGone()
                holder.itemScratchCoinsTv.text = item.coins.toString()
                holder.itemScratchDateTv.text = item.createdAt?.getFormattedDateWithTime()
                holder.itemScratchDescTv.text = item.desc
            } else {
                holder.scratchRevealedRl.makeViewGone()
                holder.scratchUnRevealedRl.makeViewVisible()
            }

            holder.itemScratchRootCv.transitionName = "transition_name_$position"

            holder.itemScratchRootCv.setOnClickListener {
                onClickListener.onScratchClicked(item, holder.itemScratchRootCv, holder.absoluteAdapterPosition)
            }

        }

    }

    fun getItemAtPosition(position: Int): Partner? {
        return getItem(position)
    }

    fun submitListToAdapter(list: MutableList<Partner>) {
        CoroutineScope(Dispatchers.IO).launch {
            submitData(PagingData.from(list))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScratchCardViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_scratch_card, parent, false)

        return ScratchCardViewHolder(view)
    }

    interface OnClickInterface {
        fun onScratchClicked(
            scratchCard: Partner,
            itemScratchRootCv: CardView,
            pos: Int,
        )
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
    }
}






















package com.laundrybuoy.customer.adapter.rating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemEmojiBinding
import com.laundrybuoy.customer.model.rating.EmojiList.EmojiListItem

class OrderEmojiAdapter(val clickListener: EmojiClickListener) :
    ListAdapter<EmojiListItem, OrderEmojiAdapter.AddressViewHolder>(
        ComparatorDiffUtil()
    ) {

    private var checkedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            RowItemEmojiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class AddressViewHolder(private val binding: RowItemEmojiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(emoji: EmojiListItem, position: Int) {

            if (checkedPosition == -1) {
                binding.isSelected = false
                emoji.isSelected = false
            } else {
                binding.isSelected = checkedPosition == adapterPosition
                emoji.isSelected = checkedPosition == adapterPosition
            }

            binding.emojiIconIv.setImageResource(emoji.emoji)

            binding.emojiRootRl.setOnClickListener {
                binding.isSelected = true
                emoji.isSelected=true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                clickListener.onEmojiClicked(emoji)
            }

        }
    }

    interface EmojiClickListener {
        fun onEmojiClicked(emoji: EmojiListItem)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<EmojiListItem>() {
        override fun areItemsTheSame(
            oldItem: EmojiListItem,
            newItem: EmojiListItem,
        ): Boolean {
            return oldItem.value == newItem.value
        }

        override fun areContentsTheSame(
            oldItem: EmojiListItem,
            newItem: EmojiListItem,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
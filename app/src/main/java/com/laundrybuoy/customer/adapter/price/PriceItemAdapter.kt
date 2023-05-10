package com.laundrybuoy.customer.adapter.price

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.databinding.RowItemInventoryBinding
import com.laundrybuoy.customer.model.price.InventoryModel

class PriceItemAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<InventoryModel.Data.Item, PriceItemAdapter.InventoryListViewHolder>(
        ComparatorDiffUtil()
    ),Filterable {

    private var list = mutableListOf<InventoryModel.Data.Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryListViewHolder {
        val binding =
            RowItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryListViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class InventoryListViewHolder(private val binding: RowItemInventoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: InventoryModel.Data.Item, position: Int) {
            binding.itemInventoryName.text = doc.itemName
            binding.itemInventoryQty.setText(doc.selectedQty.toString())

            binding.itemInventoryPlus.setOnClickListener {
                onClickListener.onPlusClicked(doc, position)
            }

            binding.itemInventoryMinus.setOnClickListener {
                onClickListener.onMinusClicked(doc, position)
            }
        }
    }

    interface OnClickInterface {
        fun onPlusClicked(doc: InventoryModel.Data.Item, position: Int)
        fun onMinusClicked(doc: InventoryModel.Data.Item, position: Int)
    }

    fun updateQty(position: Int, currentQty: Int?): MutableList<InventoryModel.Data.Item> {
        currentList[position].selectedQty = currentQty
        notifyItemChanged(position)
        return currentList
    }

    fun setData(list: MutableList<InventoryModel.Data.Item>){
        this.list = list
        submitList(list)
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<InventoryModel.Data.Item>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(list)
            } else {
                for (item in list) {
                    if (item.itemName?.toLowerCase()!!.startsWith(constraint.toString().toLowerCase())) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<InventoryModel.Data.Item>)
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<InventoryModel.Data.Item>() {
        override fun areItemsTheSame(
            oldItem: InventoryModel.Data.Item,
            newItem: InventoryModel.Data.Item
        ): Boolean {
            return oldItem.itemName == newItem.itemName
        }

        override fun areContentsTheSame(
            oldItem: InventoryModel.Data.Item,
            newItem: InventoryModel.Data.Item
        ): Boolean {
            return oldItem == newItem
        }

    }
}
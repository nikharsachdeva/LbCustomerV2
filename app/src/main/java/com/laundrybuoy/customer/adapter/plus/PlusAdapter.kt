package com.laundrybuoy.customer.adapter.plus

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.databinding.RowItemPlusSubBinding
import com.laundrybuoy.customer.model.plus.GetMembershipsResponse
import com.laundrybuoy.customer.utils.colorTransition
import com.laundrybuoy.customer.utils.colorTransition2
import com.laundrybuoy.customer.utils.makeViewInVisible
import com.laundrybuoy.customer.utils.makeViewVisible
import kotlin.math.roundToInt

class PlusAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<GetMembershipsResponse.Data, PlusAdapter.PlusViewHolder>(
        ComparatorDiffUtil()
    ) {

    var selectedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlusViewHolder {
        val binding =
            RowItemPlusSubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlusViewHolder(binding)
    }

    fun setCurrentSelectedPos(current : Int){
        selectedPos = current
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PlusViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }
    }

    inner class PlusViewHolder(private val binding: RowItemPlusSubBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(packages: GetMembershipsResponse.Data, position: Int) {
            var pointerAdapter = PlusPointerAdapter()
            pointerAdapter = if(position==selectedPos){
                binding.plusSubRlRoot.colorTransition(R.color.colorDarkerBlue)
                binding.subNameTv.colorTransition2(R.color.white)
                binding.subPriceTv.colorTransition2(R.color.white)
                PlusPointerAdapter(R.color.white)
            }else{
                binding.plusSubRlRoot.colorTransition(R.color.colorLightBeige)
                binding.subNameTv.colorTransition2(R.color.colorHeading)
                binding.subPriceTv.colorTransition2(R.color.colorHeading)
                PlusPointerAdapter(R.color.colorHeading)
            }

            binding.subIconIv.imageTintList = ColorStateList.valueOf(Color.parseColor("#"+packages.hexCode))
            binding.subNameTv.text = packages.name ?: ""
            binding.subDescTv.text = packages.description ?: ""
            binding.subPriceTv.text="₹${(packages.price!!).roundToInt()}"
            binding.subValidityTv.text="${packages.validity} days"
            binding.subPriceOldTv.text="₹${packages.grossPrice?.roundToInt()}"

            if(packages?.price==packages?.grossPrice){
                binding.subPriceOldTv.text=""
                binding.subPriceOldTv.makeViewInVisible()
                binding.subPriceOldV.makeViewInVisible()
            }else{
                binding.subPriceOldTv.makeViewVisible()
                binding.subPriceOldV.makeViewVisible()
            }

            binding.subPointsRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            binding.subPointsRv.adapter = pointerAdapter
            pointerAdapter.submitList(packages.details)

            binding.chooseSubBtn.setOnClickListener {
                onClickListener.onPlusClicked(packages = packages)
            }
        }
    }

    interface OnClickInterface {
        fun onPlusClicked(packages: GetMembershipsResponse.Data)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<GetMembershipsResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: GetMembershipsResponse.Data,
            newItem: GetMembershipsResponse.Data,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetMembershipsResponse.Data,
            newItem: GetMembershipsResponse.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}
package com.laundrybuoy.customer.adapter.order

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.model.order.CustomerOrdersModel.Data.Partner
import com.laundrybuoy.customer.utils.*

class CustomerOrderPagingAdapter(private val clickListener: OnClickInterface) :
    PagingDataAdapter<Partner, CustomerOrderPagingAdapter.PartnerOrderViewHolder>(
        COMPARATOR
    ){

    class PartnerOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val serviceIcon: ImageView = itemView.findViewById(R.id.orderServiceIcon_iv)
        val serviceName: TextView = itemView.findViewById(R.id.orderServiceName_tv)
        val orderAddress: TextView = itemView.findViewById(R.id.orderServiceAddress_tv)
        val orderStatus: TextView = itemView.findViewById(R.id.orderStatus_tv)
        val orderItemsRv: RecyclerView = itemView.findViewById(R.id.orderItems_rv)
        val orderDateTime: TextView = itemView.findViewById(R.id.orderDateTime_tv)
        val orderNumber: TextView = itemView.findViewById(R.id.orderNumber_tv)
        val orderItemUnav: TextView = itemView.findViewById(R.id.orderItemsUnavailable_tv)
        val orderRatingRv: RecyclerView = itemView.findViewById(R.id.orderRating_rv)
        val orderInfo1Rl: RelativeLayout = itemView.findViewById(R.id.orderInfo1_rl)
        val orderInfo2Rl: RelativeLayout = itemView.findViewById(R.id.orderInfo2_rl)
        val orderInfo3Rl: RelativeLayout = itemView.findViewById(R.id.orderInfo3_rl)
        val orderInfo4Rl: RelativeLayout = itemView.findViewById(R.id.orderInfo4_rl)
        val orderDivider2: View = itemView.findViewById(R.id.orderInfoDotted_v2)

    }

    override fun onBindViewHolder(holder: PartnerOrderViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            holder.serviceIcon.loadImageWithGlide(item.orderDetails.serviceId?.serviceImage ?: "")
            holder.serviceName.text = item.orderDetails.serviceId?.serviceName ?: ""
            holder.orderAddress.text = item.orderDetails.deliveryAddress?.line1 ?: ""

            val orderStatus = item.orderDetails.orderStatus?.getOrderStatus()
            holder.orderStatus.text = orderStatus

            when (orderStatus) {
                "Delivered" -> {
                    holder.orderStatus.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#30b856"
                            )
                        )
                    )
                    holder.orderStatus.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#1030b856"))
                }

                "Cancelled" -> {
                    holder.orderStatus.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#fc2254"
                            )
                        )
                    )
                    holder.orderStatus.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#10fc2254"))
                }

                else -> {
                    holder.orderStatus.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#e6bb4f"
                            )
                        )
                    )
                    holder.orderStatus.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#10e6bb4f"))
                }
            }

            if (!item.orderDetails.items.isNullOrEmpty()) {
                val itemsAdapter = CustomerOrderItemsAdapter()
                holder.orderItemsRv.layoutManager =
                    LinearLayoutManager(
                        holder.itemView.context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                holder.orderItemsRv.adapter = itemsAdapter
                itemsAdapter.submitList(item.orderDetails.items)
                holder.orderItemsRv.makeViewVisible()
                holder.orderItemUnav.makeViewGone()
            } else {
                holder.orderItemsRv.makeViewGone()
                holder.orderItemUnav.makeViewVisible()
            }

            holder.orderDateTime.text = item.orderDetails.orderDate?.getFormattedDateWithTime()
            holder.orderNumber.text = "#" + item.orderDetails.ordNum

            if (!item.ratings.isNullOrEmpty()) {
                holder.orderInfo4Rl.makeViewVisible()
                holder.orderDivider2.makeViewVisible()
                val ratingAdapter = CustomerOrderRatingAdapter(object : CustomerOrderRatingAdapter.RatingClickListener{
                    override fun onRatingClicked() {
                        clickListener.onRatingSelected(item)
                    }

                })
                holder.orderRatingRv.layoutManager =
                    LinearLayoutManager(
                        holder.itemView.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                holder.orderRatingRv.adapter = ratingAdapter
                ratingAdapter.submitList(item.ratings)
            } else {
                holder.orderInfo4Rl.makeViewGone()
                holder.orderDivider2.makeViewGone()
            }

            holder.orderInfo1Rl.setOnClickListener {
                clickListener.onSelected(item, position)
            }
            holder.orderInfo2Rl.setOnClickListener {
                clickListener.onSelected(item, position)
            }
            holder.orderInfo3Rl.setOnClickListener {
                clickListener.onSelected(item, position)
            }
            /*
            holder.orderInfo4Rl.setOnClickListener {
                clickListener.onRatingSelected(item)
            }
             */

            /*
            holder.coinTranRoot.setOnClickListener {
                onClickListener.onSelected(item, position)
            }
             */
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerOrderViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_customer_orders, parent, false)

        return PartnerOrderViewHolder(view)
    }

    interface OnClickInterface {
        fun onSelected(order: Partner, position: Int)
        fun onRatingSelected(item: Partner)
    }

    companion object {
        private val COMPARATOR =
            object : DiffUtil.ItemCallback<Partner>() {
                override fun areItemsTheSame(
                    oldItem: Partner,
                    newItem: Partner,
                ): Boolean {
                    return oldItem.orderDetails.id == newItem.orderDetails.id
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






















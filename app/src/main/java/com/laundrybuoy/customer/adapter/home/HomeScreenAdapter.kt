package com.laundrybuoy.customer.adapter.home

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.model.home.HomeScreenModel.Data
import com.laundrybuoy.customer.utils.loadImageWithGlide

class HomeScreenAdapter(private val context: Context, var list: MutableList<Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SLIDER_S = 1
        const val VIEW_TYPE_GRID = 2
        const val VIEW_TYPE_SMALL_B = 3
        const val VIEW_TYPE_BIG_B = 4
        const val VIEW_TYPE_FOOTER = 5
        const val VIEW_TYPE_ORD_HIST = 6
        const val VIEW_TYPE_REFER = 7
        const val VIEW_TYPE_ADVERTISEMENT = 8
        const val VIEW_TYPE_SLIDER_B = 9
        const val VIEW_TYPE_NULL = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_SLIDER_S -> SmallSliderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_small_slider, parent, false)
            )

            VIEW_TYPE_SLIDER_B -> BigSliderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_big_slider, parent, false)
            )

            VIEW_TYPE_GRID -> GridViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_grid, parent, false)
            )

            VIEW_TYPE_SMALL_B -> SmallBannerViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_small_banner, parent, false)
            )

            VIEW_TYPE_BIG_B -> BigBannerViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_big_banner, parent, false)
            )

            VIEW_TYPE_ORD_HIST -> CurrentOrderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_orders, parent, false)
            )

            VIEW_TYPE_REFER -> ReferViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_refer, parent, false)
            )

            VIEW_TYPE_ADVERTISEMENT -> AdvertisementViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_advertisement, parent, false)
            )

            VIEW_TYPE_FOOTER -> FooterViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_home_footer, parent, false)
            )

            else -> NullViewHolder(View(context))
        }

    }

    private inner class NullViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {}
    }

    private inner class ReferViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var referIv: ImageView = itemView.findViewById(R.id.row_item_refer_iv)
        var referRl: RelativeLayout = itemView.findViewById(R.id.referRl)

        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            referIv.loadImageWithGlide(recyclerViewModel.data[0].image.toString())
        }
    }

    private inner class AdvertisementViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var bannerIv: ImageView = itemView.findViewById(R.id.row_item_adv_iv)

        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            bannerIv.loadImageWithGlide(recyclerViewModel.data[0].image.toString())
        }
    }

    private inner class SmallBannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var bannerIv: ImageView = itemView.findViewById(R.id.row_item_s_banner_iv)

        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            bannerIv.loadImageWithGlide(recyclerViewModel.data[0].image.toString())
        }
    }

    private inner class BigBannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var bannerIv: ImageView = itemView.findViewById(R.id.row_item_b_banner_iv)

        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            bannerIv.loadImageWithGlide(recyclerViewModel.data[0].image.toString())
        }
    }

    private inner class FooterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var footerHeading: TextView = itemView.findViewById(R.id.footerHeading_tv)
        var footerSubHeading: TextView = itemView.findViewById(R.id.footerSubHeading_tv)

        fun bind(position: Int) {
            val recyclerViewModel = list[position]

            footerHeading.text = recyclerViewModel.data[0].heading.toString()
            footerSubHeading.text = recyclerViewModel.data[0].subHeading.toString()

        }
    }

    private inner class CurrentOrderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var orderRv: RecyclerView = itemView.findViewById(R.id.row_item_orders_rv)
        private lateinit var homeOrderAdapter: HomeOrderAdapter

        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            homeOrderAdapter = HomeOrderAdapter()
            orderRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            orderRv.adapter = homeOrderAdapter
            homeOrderAdapter.submitList(recyclerViewModel.data)

        }
    }

    private inner class GridViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var gridRv: RecyclerView = itemView.findViewById(R.id.row_item_grid_rv)
        var gridHeading: TextView = itemView.findViewById(R.id.row_item_grid_heading_tv)
        private lateinit var gridAdapter: HomeGridAdapter

        fun bind(position: Int) {
            val recyclerViewModel = list[position]

            gridHeading.text = recyclerViewModel.heading.toString()
            gridAdapter = HomeGridAdapter()
            gridRv.layoutManager =
                GridLayoutManager(context, 2)
            gridRv.adapter = gridAdapter
            gridAdapter.submitList(recyclerViewModel.data)

        }
    }


    private inner class SmallSliderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var viewPager: ViewPager2 = itemView.findViewById(R.id.row_item_small_slider_vp)
        var indicatorLl: LinearLayout = itemView.findViewById(R.id.row_item_small_indicator_ll)
        val adapter = HomeSliderAdapter()
        val interval = 3000L // 3 seconds
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val adapter = viewPager.adapter
                if (adapter != null && currentItem < adapter.itemCount - 1) {
                    viewPager.currentItem = currentItem + 1
                } else {
                    viewPager.currentItem = 0
                }
                handler.postDelayed(this, interval)
            }
        }

        fun bind(position: Int) {

            val dataList = list[position]

            viewPager.adapter = adapter
            viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            })
            (viewPager.getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER

            adapter.submitList(dataList.data)
            setupIndicator()

            setCurrentIndicator(0)
            setupAutoScroll()
        }

        private fun setupAutoScroll() {
            handler.postDelayed(runnable, interval)
        }

        private fun setCurrentIndicator(position: Int) {
            val childCount = indicatorLl.childCount
            for (i in 0 until childCount) {
                val imgView = indicatorLl.getChildAt(i) as ImageView
                if (i == position) {
                    imgView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.active_indicator
                        )
                    )
                } else {
                    imgView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.in_active_indicator
                        )
                    )
                }
            }
        }

        private fun setupIndicator() {
            val indicators = arrayOfNulls<ImageView>(adapter.itemCount)
            val layoutparams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutparams.setMargins(8, 0, 8, 0)
            for (i in indicators.indices) {
                indicators[i] = ImageView(context)
                indicators[i].let {
                    it?.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.active_indicator
                        )
                    )
                    it?.layoutParams = layoutparams
                    indicatorLl.addView(it)
                }
            }
        }

    }


    private inner class BigSliderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var viewPager: ViewPager2 = itemView.findViewById(R.id.row_item_big_slider_vp)
        var indicatorLl: LinearLayout = itemView.findViewById(R.id.row_item_big_indicator_ll)
        val adapter = HomeSliderAdapter()
        val interval = 3000L // 3 seconds
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val adapter = viewPager.adapter
                if (adapter != null && currentItem < adapter.itemCount - 1) {
                    viewPager.currentItem = currentItem + 1
                } else {
                    viewPager.currentItem = 0
                }
                handler.postDelayed(this, interval)
            }
        }

        fun bind(position: Int) {

            val dataList = list[position]

            viewPager.adapter = adapter
            viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            })
            (viewPager.getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER

            adapter.submitList(dataList.data)
            setupIndicator()

            setCurrentIndicator(0)
            setupAutoScroll()
        }

        private fun setupAutoScroll() {
            handler.postDelayed(runnable, interval)
        }

        private fun setCurrentIndicator(position: Int) {
            val childCount = indicatorLl.childCount
            for (i in 0 until childCount) {
                val imgView = indicatorLl.getChildAt(i) as ImageView
                if (i == position) {
                    imgView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.active_indicator
                        )
                    )
                } else {
                    imgView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.in_active_indicator
                        )
                    )
                }
            }
        }

        private fun setupIndicator() {
            val indicators = arrayOfNulls<ImageView>(adapter.itemCount)
            val layoutparams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutparams.setMargins(8, 0, 8, 0)
            for (i in indicators.indices) {
                indicators[i] = ImageView(context)
                indicators[i].let {
                    it?.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.active_indicator
                        )
                    )
                    it?.layoutParams = layoutparams
                    indicatorLl.addView(it)
                }
            }
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_SLIDER_S -> {
                val smallSliderHolder = holder as SmallSliderViewHolder
                smallSliderHolder.bind(position)
            }

            VIEW_TYPE_GRID -> {
                val gridHolder = holder as GridViewHolder
                gridHolder.bind(position)
            }

            VIEW_TYPE_SMALL_B -> {
                val sBannerHolder = holder as SmallBannerViewHolder
                sBannerHolder.bind(position)
            }

            VIEW_TYPE_BIG_B -> {
                val bBannerHolder = holder as BigBannerViewHolder
                bBannerHolder.bind(position)
            }

            VIEW_TYPE_ORD_HIST -> {
                val ordersHolder = holder as CurrentOrderViewHolder
                ordersHolder.bind(position)
            }

            VIEW_TYPE_FOOTER -> {
                val footerHolder = holder as FooterViewHolder
                footerHolder.bind(position)
            }

            VIEW_TYPE_REFER -> {
                val referHolder = holder as ReferViewHolder
                referHolder.bind(position)
            }

            VIEW_TYPE_ADVERTISEMENT -> {
                val adHolder = holder as AdvertisementViewHolder
                adHolder.bind(position)
            }

            VIEW_TYPE_SLIDER_B -> {
                val bigSliderHolder = holder as BigSliderViewHolder
                bigSliderHolder.bind(position)
            }

            VIEW_TYPE_NULL -> {
                val nullHolder = holder as NullViewHolder
                nullHolder.bind(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
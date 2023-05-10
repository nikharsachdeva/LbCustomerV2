package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.plus.PlusIntroPointsAdapter
import com.laundrybuoy.customer.adapter.plus.PlusPointerAdapter
import com.laundrybuoy.customer.adapter.walkthrough.WalkthroughAdapter
import com.laundrybuoy.customer.databinding.FragmentPlusBinding
import com.laundrybuoy.customer.databinding.FragmentPlusIntroBinding
import com.laundrybuoy.customer.model.plus.PlusIntroPointsModel
import com.laundrybuoy.customer.model.walkthrough.WalkthroughResponse
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewInVisible
import com.laundrybuoy.customer.utils.makeViewVisible
import java.util.*

class PlusIntroFragment : BaseFragment() {

    private var _binding: FragmentPlusIntroBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlusIntroPointsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNav()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPointersVP()
        fetchIntroPoints()
        onClick()
    }

    private fun onClick() {
        binding.explorePlansBtn.setOnClickListener {
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                PlusFragment()
            )
        }
    }

    private fun initPointersVP() {
        adapter = PlusIntroPointsAdapter()
        binding.plusIntroViewPager.adapter = adapter
        binding.plusIntroViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (binding.plusIntroViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
    }

    private fun fetchIntroPoints() {
        val listOfItems: MutableList<PlusIntroPointsModel.Data> = arrayListOf()

        listOfItems.add(
            PlusIntroPointsModel.Data(
                heading = "Free Delivery",
                subHeading = "No more worrying about delivery fees, our premium plan offers free delivery on all your orders."
            )
        )

        listOfItems.add(
            PlusIntroPointsModel.Data(
                heading = "Priority Service",
                subHeading = "Our premium plan members receive priority service, so your laundry is always at the top of our to-do list."
            )
        )

        listOfItems.add(
            PlusIntroPointsModel.Data(
                heading = "Exclusive Discounts",
                subHeading = "Enjoy exclusive discounts and offers on all our services, only available to our premium plan members."
            )
        )

        listOfItems.add(
            PlusIntroPointsModel.Data(
                heading = "Personalized Care",
                subHeading = "As a premium plan member, you'll receive personalized care from our team, ensuring all your laundry needs are met."
            )
        )

        listOfItems.add(
            PlusIntroPointsModel.Data(
                heading = "Flexible Scheduling",
                subHeading = "Need laundry done at a specific time? Our premium plan members have access to flexible scheduling options, so you can choose the best time for you."
            )
        )

        listOfItems.add(
            PlusIntroPointsModel.Data(
                heading = "Easy Cancellation",
                subHeading = "If your plans change, don't worry! Our premium plan members can easily cancel or reschedule their orders without any fees."
            )
        )

        adapter.submitList(listOfItems)
        setupIndicator()
        setCurrentIndicator(0)
        setupAutoScroll()
    }

    val interval = 3000L // 3 seconds
    val handler = Handler(Looper.getMainLooper())
    val runnable = object : Runnable {
        override fun run() {
            val currentItem = binding.plusIntroViewPager.currentItem
            val adapter = binding.plusIntroViewPager.adapter
            if (adapter != null && currentItem < adapter.itemCount - 1) {
                binding.plusIntroViewPager.currentItem = currentItem + 1
            } else {
                binding.plusIntroViewPager.currentItem = 0
            }
            handler.postDelayed(this, interval)
        }
    }

    private fun setupAutoScroll() {
        handler.postDelayed(runnable, interval)
    }

    private fun setupIndicator() {
        val indicators = arrayOfNulls<ImageView>(adapter.itemCount)
        val layoutparams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutparams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i].let {
                it?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.active_indicator
                    )
                )
                it?.layoutParams = layoutparams
                binding.indicatorIntroLl.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = binding.indicatorIntroLl.childCount
        for (i in 0 until childCount) {
            val imgView = binding.indicatorIntroLl.getChildAt(i) as ImageView
            if (i == position) {
                imgView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.active_indicator
                    )
                )
            } else {
                imgView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.in_active_indicator
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlusIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }


}
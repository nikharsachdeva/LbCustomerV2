package com.laundrybuoy.customer.ui.quick

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.adapter.rating.RatingResultListener
import com.laundrybuoy.customer.adapter.schedule.QuickPagerAdapter
import com.laundrybuoy.customer.databinding.FragmentQuickOrderRootBinding
import com.laundrybuoy.customer.ui.FeedbackDynamicFragment
import com.laundrybuoy.customer.ui.GeneralInfoFragment
import com.laundrybuoy.customer.utils.Constants.BACKWARD
import com.laundrybuoy.customer.utils.Constants.FORWARD
import com.laundrybuoy.customer.utils.Constants.HIDE_CLOSE
import com.laundrybuoy.customer.utils.Constants.QUICK_HOME
import com.laundrybuoy.customer.utils.makeViewInVisible
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickOrderRootFragment : BaseBottomSheet() {

    private var _binding: FragmentQuickOrderRootBinding? = null
    private val binding get() = _binding!!
    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()
        onClicks()
        initViewModel()
    }

    private fun initViewModel() {
        val model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        model.sendMessage(null)
        model.message.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                redirectToPage(it)
            }
        })

    }

    companion object {
        private const val SOURCE = "SOURCE"

        fun newInstance(
            source: String,
        ): QuickOrderRootFragment {
            val quickFragment = QuickOrderRootFragment()
            val args = Bundle()
            args.putString(SOURCE, source)
            quickFragment.arguments = args
            return quickFragment
        }
    }

    private fun redirectToPage(destination: String) {
        
        when (destination) {

            FORWARD -> {
                if (binding.quickOrderVp.currentItem + 1 < 8) {
                    binding.quickOrderVp.currentItem += 1
                }
            }

            BACKWARD -> {
                if (binding.quickOrderVp.currentItem != 0) {
                    binding.quickOrderVp.currentItem -= 1
                }
            }

            HIDE_CLOSE ->{
                binding.closeQuickOrderIv.makeViewInVisible()
            }

            QUICK_HOME -> {
                if(::callback.isInitialized){
                    dialog?.dismiss()
                    callback.invoke()
                }
            }

        }
    }

    private fun initTabLayout() {
        val quickTabLayout = binding.quickOrderTl
        val quickViewPager = binding.quickOrderVp

        quickTabLayout.addTab(quickTabLayout.newTab().setText("Welcome"))
        quickTabLayout.addTab(quickTabLayout.newTab().setText("Service"))
        quickTabLayout.addTab(quickTabLayout.newTab().setText("Address"))
        quickTabLayout.addTab(quickTabLayout.newTab().setText("Date"))
        quickTabLayout.addTab(quickTabLayout.newTab().setText("Clothes"))
        quickTabLayout.addTab(quickTabLayout.newTab().setText("Coupon"))
        quickTabLayout.addTab(quickTabLayout.newTab().setText("Review"))
        quickTabLayout.addTab(quickTabLayout.newTab().setText("Success"))
        quickTabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = QuickPagerAdapter(
            requireContext(), childFragmentManager,
            quickTabLayout.tabCount
        )
        quickViewPager.adapter = adapter
        quickViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                quickTabLayout
            )
        )
        quickViewPager.offscreenPageLimit = 8
        quickTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                quickViewPager.currentItem = tab.position

                if(quickViewPager.currentItem==7){
                    redirectToPage(HIDE_CLOSE)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        quickViewPager.setSwipePagingEnabled(false)
    }


    private fun onClicks() {
        binding.closeQuickOrderIv.setOnClickListener {
            dialog?.dismiss()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickOrderRootBinding.inflate(inflater, container, false)
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
        _binding = null
    }


}
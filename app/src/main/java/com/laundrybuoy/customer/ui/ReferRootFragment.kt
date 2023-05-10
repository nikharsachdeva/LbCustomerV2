package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.adapter.refer.ReferPagerAdapter
import com.laundrybuoy.customer.databinding.FragmentReferRootBinding


class ReferRootFragment : BaseFragment() {

    private var _binding: FragmentReferRootBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initTabLayout()
        onClick()

    }

    private fun onClick() {
        binding.backFromReferIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    private fun initTabLayout() {
        val vendorTabLayout = binding.referRootTabLayout
        val vendorViewPager = binding.referRootViewPager

        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Invite"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("FAQs"))
        vendorTabLayout.tabGravity = TabLayout.GRAVITY_CENTER
        val adapter = ReferPagerAdapter(
            requireContext(), childFragmentManager,
            vendorTabLayout.tabCount
        )
        vendorViewPager.adapter = adapter
        vendorViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                vendorTabLayout
            )
        )
        vendorViewPager.offscreenPageLimit = 2
        vendorTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vendorViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentReferRootBinding.inflate(inflater, container, false)
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
package com.laundrybuoy.customer.adapter.schedule

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.customer.ui.SplashFragment
import com.laundrybuoy.customer.ui.quick.*

class QuickPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> {
                QuickWelcomeFragment()
            }
            1 -> {
                QuickServiceFragment()
            }
            2 -> {
                QuickAddressFragment()
            }
            3 -> {
                QuickDateFragment()
            }
            4 -> {
                QuickClothesFragment()
            }
            5 -> {
                QuickCouponFragment()
            }
            6 -> {
                QuickReviewFragment()
            }
            7 -> {
                QuickOrderResultFragment()
            }

            else -> SplashFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
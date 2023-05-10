package com.laundrybuoy.customer.adapter.refer

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.customer.ui.ReferFaqFragment
import com.laundrybuoy.customer.ui.ReferInviteFragment
import com.laundrybuoy.customer.ui.SplashFragment

class ReferPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> {
                ReferInviteFragment()
            }
            1 -> {
                ReferFaqFragment()
            }

            else -> SplashFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
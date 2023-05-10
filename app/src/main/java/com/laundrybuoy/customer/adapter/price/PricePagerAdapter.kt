package com.laundrybuoy.customer.adapter.price

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.laundrybuoy.customer.model.price.InventoryListSegregated
import com.laundrybuoy.customer.ui.DynamicItemFragment
import com.laundrybuoy.customer.ui.SplashFragment
import com.laundrybuoy.customer.utils.DynamicViewPager

class PricePagerAdapter(
    fm: FragmentManager,
    val categoriesList: MutableList<InventoryListSegregated.InventoryListSegregatedItem>,
    val type: Int,
    val selectionInterface: SelectionInterface
) :
    FragmentStatePagerAdapter(fm) {
    private var mCurrentPosition = -1

    companion object {
        const val TYPE_INVENTORY_FRAGMENT = 0
    }

    override fun getItem(position: Int): Fragment {

        return when (type) {
            TYPE_INVENTORY_FRAGMENT -> {
                DynamicItemFragment.newInstance(
                    categoriesList[position],
                    selectionInterface
                )
            }
            else -> {
                SplashFragment()
            }
        }

    }

    override fun getCount(): Int {
        return if (categoriesList.isNotEmpty())
            categoriesList.size
        else
            1
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != mCurrentPosition) {
            val fragment = `object` as Fragment
            mCurrentPosition = position
            if (container is DynamicViewPager) {
                val pager = container
                if (fragment.view != null) {
                    fragment.view?.let { pager.measureCurrentView(it) }
                }
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (categoriesList.isNotEmpty())
            categoriesList[position].catName
        else ""

    }
}
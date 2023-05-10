package com.laundrybuoy.customer.adapter.rating

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.laundrybuoy.customer.model.order.CustomerOrdersModel
import com.laundrybuoy.customer.ui.FeedbackDynamicFragment
import com.laundrybuoy.customer.ui.FeedbackRootFragment
import com.laundrybuoy.customer.ui.SplashFragment
import com.laundrybuoy.customer.utils.DynamicViewPager

class FeedbackPagerAdapter(
    fm: FragmentManager,
    val feedbackListType: MutableList<CustomerOrdersModel.Data.Partner.Rating>,
    val type: Int,
    val resultListener : RatingResultListener
) :
    FragmentStatePagerAdapter(fm) {
    private var mCurrentPosition = -1

    companion object {
        const val TYPE_FEEDBACK_FRAGMENT = 0
    }

    override fun getItem(position: Int): Fragment {

        return when (type) {
            TYPE_FEEDBACK_FRAGMENT -> {
                val bundle = Bundle().apply {
                    putParcelable("ratingsObj", feedbackListType[position])
                }
                val feedbackDynFrag = FeedbackDynamicFragment.newInstance(resultListener)
                feedbackDynFrag.arguments = bundle
                return feedbackDynFrag
            }
            else -> {
                SplashFragment()
            }
        }

    }

    override fun getCount(): Int {
        return if (feedbackListType.isNotEmpty())
            feedbackListType.size
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
        return if (feedbackListType.isNotEmpty()) {
            val ratedFor = when (feedbackListType[position].ratingFor.toString()) {
                "pickupRider" -> "Pickup"
                "deliveryRider" -> "Delivery"
                "partner" -> "Laundry"
                else -> ""
            }
            return ratedFor
        } else {
            ""
        }
    }
}
package com.laundrybuoy.customer.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.adapter.rating.FeedbackPagerAdapter
import com.laundrybuoy.customer.adapter.rating.FeedbackPagerAdapter.Companion.TYPE_FEEDBACK_FRAGMENT
import com.laundrybuoy.customer.adapter.rating.RatingResultListener
import com.laundrybuoy.customer.databinding.FragmentFeedbackRootBinding
import com.laundrybuoy.customer.model.order.CustomerOrdersModel.Data.Partner.Rating

class FeedbackRootFragment : BaseBottomSheet() {

    private var _binding: FragmentFeedbackRootBinding? = null
    private val binding get() = _binding!!
    private lateinit var ratingListReceived: ArrayList<Rating>

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        getBundleData()

    }

    private fun getBundleData() {
        ratingListReceived = arguments?.getParcelableArrayList<Rating>("ratingsList")!!
        if (!ratingListReceived.isNullOrEmpty()) {
            setDataToVp(ratingListReceived)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun setDataToVp(ratingListReceived: ArrayList<Rating>) {

        binding.ratingRootViewPager.adapter =
            FeedbackPagerAdapter(
                childFragmentManager,
                ratingListReceived,
                TYPE_FEEDBACK_FRAGMENT,
                object : RatingResultListener {
                    override fun onRatingSuccess() {
                        if (binding.ratingRootViewPager.currentItem + 1 < ratingListReceived.size) {
                            binding.ratingRootViewPager.currentItem += 1
                        }else{
                            dialog?.dismiss()
                        }
                    }

                    override fun onRatingError() {

                    }

                    override fun onRatingClosed() {
                        dialog?.dismiss()
                    }

                }
            )
        binding.ratingRootViewPager.offscreenPageLimit = ratingListReceived.size
        binding.ratingRootViewPager.setSwipePagingEnabled(false)


    }

    fun initViewPager() {

        binding.ratingRootTabLayout.setupWithViewPager(binding.ratingRootViewPager)
        binding.ratingRootTabLayout.tabRippleColor = null
        binding.ratingRootTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFeedbackRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(true)
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
        if(::callback.isInitialized){
            callback.invoke()
        }
    }


}
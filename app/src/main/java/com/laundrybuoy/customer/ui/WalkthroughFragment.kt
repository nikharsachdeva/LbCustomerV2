package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.util.Log
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
import com.laundrybuoy.customer.adapter.walkthrough.WalkthroughAdapter
import com.laundrybuoy.customer.databinding.FragmentWalkthroughBinding
import com.laundrybuoy.customer.model.walkthrough.WalkthroughResponse
import com.laundrybuoy.customer.utils.SharedPreferenceManager
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewInVisible
import com.laundrybuoy.customer.utils.makeViewVisible

class WalkthroughFragment : BaseFragment() {

    private var _binding: FragmentWalkthroughBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WalkthroughAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWalkthroughVP()
        onClick()

        fetchWalkthrough()

    }

    private fun fetchWalkthrough() {
        val listOfItems: MutableList<WalkthroughResponse.Data> = arrayListOf()

        listOfItems.add(
            WalkthroughResponse.Data(
                description = "Get your laundry done with just a few taps",
                heading = "Welcome to Laundry Buoy!",
                image = "https://imgtr.ee/images/2023/03/07/ovRbR.jpg"
            )
        )

        listOfItems.add(
            WalkthroughResponse.Data(
                description = "Tell us when and where to pick up your laundry",
                heading = "Schedule your pickup",
                image = "https://imgtr.ee/images/2023/03/07/ovGsB.jpg"
            )
        )

        listOfItems.add(
            WalkthroughResponse.Data(
                description = "We'll wash, dry, and fold your laundry, and deliver it back to you",
                heading = "Relax and let us do the rest",
                image = "https://imgtr.ee/images/2023/03/07/ovOOQ.jpg"
            )
        )

        adapter.submitList(listOfItems)
        setupIndicator()
        setCurrentIndicator(0)
    }

    private fun onClick() {

        binding.nextWalkBtn.setOnClickListener {
            if (binding.walkthroughPagerVp.currentItem + 1 < adapter.itemCount) {
                binding.walkthroughPagerVp.currentItem += 1
            }
        }

        binding.prevWalkBtn.setOnClickListener {
            Log.d("xoxo-->1", "onClick: ${binding.walkthroughPagerVp.currentItem}")
            if (binding.walkthroughPagerVp.currentItem != 0) {
                Log.d("xoxo-->2", "onClick: ${binding.walkthroughPagerVp.currentItem}")
                binding.walkthroughPagerVp.currentItem -= 1
            }
        }

        binding.finishWalkBtn.setOnClickListener {
            SharedPreferenceManager.setFirstTimeStatus(false)
            navigateToLogin()
        }

        /*
        binding.skipIntroBtn.setOnClickListener {
            binding.introFeaturePagerVp.currentItem = adapter.itemCount - 1
        }
         */

    }

    private fun navigateToLogin() {
        getMainActivity()?.addFragment(
            false,
            getMainActivity()?.getVisibleFrame()!!,
            LoginFragment()
        )
    }

    private fun initWalkthroughVP() {
        adapter = WalkthroughAdapter()
        binding.walkthroughPagerVp.adapter = adapter
        binding.walkthroughPagerVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if (position == adapter.itemCount - 1) {
//                    binding.skipIntroBtn.beGone()
                    binding.nextWalkBtn.makeViewGone()
                    binding.finishWalkBtn.makeViewVisible()
                } else {
//                    binding.skipIntroBtn.beVisible()
                    binding.nextWalkBtn.makeViewVisible()
                    binding.finishWalkBtn.makeViewGone()
                }

                if (position == 0) {
                    binding.prevWalkBtn.makeViewInVisible()
                } else {
                    binding.prevWalkBtn.makeViewVisible()

                }
            }
        })
        (binding.walkthroughPagerVp.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
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
        _binding = FragmentWalkthroughBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
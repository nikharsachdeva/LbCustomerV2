package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.JsonObject
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.plus.PlusAdapter
import com.laundrybuoy.customer.databinding.FragmentPlusBinding
import com.laundrybuoy.customer.model.plus.GetMembershipsResponse
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class PlusFragment : BaseFragment() {

    private var _binding: FragmentPlusBinding? = null
    private val binding get() = _binding!!
    private lateinit var plusAdapter: PlusAdapter
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val profileViewModel by viewModels<ProfileViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showShimmer() {
        binding.plusSubShimmer.makeViewVisible()
        binding.plusSubViewPager.makeViewGone()
        binding.mainHeadingLl.makeViewGone()
        binding.plusSubShimmer.startShimmer()
    }

    fun hideShimmer() {
        binding.mainHeadingLl.makeViewVisible()
        binding.plusSubShimmer.makeViewGone()
        binding.plusSubViewPager.makeViewVisible()
        binding.plusSubShimmer.stopShimmer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomNav()
        initPlusVp()
        onClick()
        initObservers()
        fetchAllMemberships()
    }

    private fun initObservers() {
        profileViewModel.allMembershipsLiveData.observe(viewLifecycleOwner, Observer {
            showShimmer()
            when (it) {
                is NetworkResult.Loading -> {
                    showShimmer()
                }
                is NetworkResult.Success -> {
                    hideShimmer()
                    if (it.data?.success == true && it.data?.message == "All Packages") {
                        setMembershipsData(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        profileViewModel.purchaseMembershipLiveData.observe(viewLifecycleOwner, Observer {
            showShimmer()
            when (it) {
                is NetworkResult.Loading -> {
                    showShimmer()
                }
                is NetworkResult.Success -> {
                    hideShimmer()
                    Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setMembershipsData(memberships: List<GetMembershipsResponse.Data>) {

        if (!memberships.isNullOrEmpty()) {
            binding.membershipUnAvailableLl.makeViewGone()
            binding.plusSubViewPager.makeViewVisible()

            plusAdapter.submitList(memberships)
            binding.plusSubViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    plusAdapter.setCurrentSelectedPos(position)
                }
            })

        } else {

            binding.membershipUnAvailableLl.makeViewVisible()
            binding.plusSubViewPager.makeViewGone()

        }


    }

    private fun fetchAllMemberships() {
        profileViewModel.getAllMemberships()
    }

    private fun onClick() {
        binding.backFromPlusIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    private fun initPlusVp() {
        plusAdapter = PlusAdapter(object : PlusAdapter.OnClickInterface {
            override fun onPlusClicked(packages: GetMembershipsResponse.Data) {
                redirectToInfo(
                    getString(R.string.confirm_purchase),
                    getString(R.string.confirm_purchase_2),
                    getString(R.string.confirm_purchase_3),
                    packages
                )
            }

        })
        binding.plusSubViewPager.adapter = plusAdapter
        // You need to retain one page on each side so that the next and previous items are visible
        binding.plusSubViewPager.offscreenPageLimit = 1

        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * Math.abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        binding.plusSubViewPager.setPageTransformer(pageTransformer)

        // The ItemDecoration gives the current (centered) item horizontal margin so that
        // it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.plusSubViewPager.addItemDecoration(itemDecoration)

    }

    private fun redirectToInfo(
        heading: String,
        subheading: String,
        buttonText: String,
        packages: GetMembershipsResponse.Data
    ) {
        val infoFrag =
            GeneralInfoFragment.newInstance(
                heading,
                subheading,
                buttonText
            )
        infoFrag.setCallback {
            val payload = JsonObject()
            payload.addProperty("packageId",packages.id)
            profileViewModel.purchaseMembership(payload)
        }
        infoFrag.isCancelable = false
        infoFrag.show(childFragmentManager, "purchase_frag")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlusBinding.inflate(inflater, container, false)
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
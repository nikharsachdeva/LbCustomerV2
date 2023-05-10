package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.adapter.home.HomeScreenAdapter
import com.laundrybuoy.customer.adapter.home.HomeOrderAdapter
import com.laundrybuoy.customer.databinding.FragmentHomeBinding
import com.laundrybuoy.customer.db.UserStructure
import com.laundrybuoy.customer.model.auth.GetProfileResponse
import com.laundrybuoy.customer.model.home.HomeScreenModel
import com.laundrybuoy.customer.model.home.OngoingOrderModel
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.utils.Constants.SCREEN_TYPE_HOME
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import com.laundrybuoy.customer.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val authViewModel by viewModels<AuthViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        onClicks()
        fetchHomeScreenData()
    }

    fun fetchHomeScreenData() {
        homeViewModel.getHomeScreenData()
    }

    private fun initObserver() {

        authViewModel.userProfileLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                    getMainActivity()?.showBottomNavShimmer()
                }
                is NetworkResult.Success -> {
                    getMainActivity()?.hideBottomNavShimmer()
                    if (it.data?.success == true && it.data?.message == "Profile Data") {
                        saveUserToDb(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.hideBottomNavShimmer()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        authViewModel.loggedInUserLive?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setUserUI(it)
            }
        })

        homeViewModel.homeScreenLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data.message == "Home Data") {
                        if (!it.data.data.isNullOrEmpty()) {
                            setHomeUI(it.data.data)
                        }
                    } else {
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun saveUserToDb(data: GetProfileResponse.Data?) {
        val userStructure = UserStructure(
            id = data?.profile?.id ?: "",
            coins = data?.profile?.coins,
            createdAt = data?.profile?.createdAt,
            email = data?.profile?.email,
            emailVerified = data?.profile?.emailVerified,
            isActive = data?.profile?.isActive,
            isBlocked = data?.profile?.isBlocked,
            mobile = data?.profile?.mobile,
            altMobile = data?.profile?.altMobile,
            name = data?.profile?.name,
            profile = data?.profile?.profile,
            role = data?.profile?.role,
            tagId = data?.profile?.tagId,
            updatedAt = data?.profile?.updatedAt,
            v = data?.profile?.v,
            jwtToken = data?.jwtToken,
            isPremium = data?.isPremium
        )
        authViewModel.saveUserToDb(userStructure)
    }

    private fun setHomeUI(dataList: MutableList<HomeScreenModel.Data>) {

        val adapter = HomeScreenAdapter(requireContext(), dataList)
        binding.homeScreenMainRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.homeScreenMainRv.adapter = adapter
    }

    private fun setUserUI(profile: UserStructure) {
        if (profile.name.isNullOrEmpty()) {
            binding.greetingTv.text = "Hi"
            binding.greetingUnavailableRl.makeViewVisible()
        } else {
            binding.greetingTv.text = "Hi ${profile.name},"
            binding.greetingUnavailableRl.makeViewGone()
        }

        binding.greetingTypeTv.text = "Welcome back \uD83D\uDC4B"

        if (profile.isPremium == true) {
            getMainActivity()?.showPlusBottomNav(false)
            getMainActivity()?.showScheduleBottomNav(true)
        } else {
            getMainActivity()?.showPlusBottomNav(true)
            getMainActivity()?.showScheduleBottomNav(false)
        }

        binding.coinHomeTv.text = profile.coins.toString()

    }

    private fun onClicks() {

        binding.coinHomeRootRl.setOnClickListener {
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                MyCoinsFragment()
            )
        }

        binding.greetingUnavailableRl.setOnClickListener {
            val updateBtmSht = UpdateProfileFragment.newInstance(Constants.UPDATE_NAME, null)
            updateBtmSht.setCallback {
                fetchUserProfile()
            }
            updateBtmSht.isCancelable = false
            updateBtmSht.show(childFragmentManager, "update_profile_name")
        }
    }

    private fun fetchUserProfile() {
        authViewModel.getUserProfile()
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
    }


}
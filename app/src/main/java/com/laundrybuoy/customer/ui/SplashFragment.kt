package com.laundrybuoy.customer.ui

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.databinding.FragmentSplashBinding
import com.laundrybuoy.customer.db.UserStructure
import com.laundrybuoy.customer.model.auth.GetProfileResponse
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.SharedPreferenceManager
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageLottieAnimation()
        initObserver()
    }

    private fun initObserver() {
        authViewModel.userProfileLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data?.message == "Profile Data") {
                        saveUserToDb(it.data.data)
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

        goToHome()

    }

    private fun manageLottieAnimation() {

        binding.splashLottieLv.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(2300)
                    binding.splashLogoIv.makeViewVisible()
                }
            }

            override fun onAnimationEnd(p0: Animator) {
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(300)
                    redirectUser()
                }
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

        })
    }

    private fun redirectUser() {
        if (!SharedPreferenceManager.getBearerToken().isNullOrEmpty()) {
            fetchUserProfile()
        } else {
            if (SharedPreferenceManager.getFirstTimeStatus() == true) {
                goToWalkThrough()
            } else {
                goToLogin()
            }
        }
    }

    private fun fetchUserProfile() {
        authViewModel.getUserProfile()
    }

    private fun goToHome() {
        getMainActivity()!!.addFragment(
            false,
            1,
            HomeFragment()
        )
    }

    private fun goToLogin() {
        getMainActivity()!!.addFragment(
            false,
            1,
            LoginFragment()
        )
    }

    private fun goToWalkThrough() {
        getMainActivity()!!.addFragment(
            false,
            1,
            WalkthroughFragment()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
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
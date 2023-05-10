package com.laundrybuoy.customer.ui.quick

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.databinding.FragmentQuickWelcomeBinding
import com.laundrybuoy.customer.model.order.CreateOrderPayload
import com.laundrybuoy.customer.utils.Constants.FORWARD
import com.laundrybuoy.customer.utils.makeViewInVisible
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuickWelcomeFragment : BaseBottomSheet() {

    private var _binding: FragmentQuickWelcomeBinding? = null
    private val binding get() = _binding!!
    lateinit var model: SharedViewModel
    private val authVideModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lottieListener()
        clickListener()
        initSharedVm()
        initObserver()
    }

    private fun initObserver() {
        authVideModel.loggedInUserLive?.observe(viewLifecycleOwner, Observer {
            val currentUser = it
            val id = currentUser.id
            val payload = CreateOrderPayload()
            payload.userId = id

            model.setOrderPayload(payload)
        })
    }

    private fun initSharedVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

    }

    private fun clickListener() {
        binding.quickOrderBtn.setOnClickListener {
            model.sendMessage(FORWARD)
        }
    }

    private fun lottieListener() {

        binding.lightningLottieSmall.makeViewInVisible()
        binding.lightningLottieBig.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.lightningLottieSmall.makeViewVisible()
                    binding.lightningLottieSmall.playAnimation()
                }
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickWelcomeBinding.inflate(inflater, container, false)
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
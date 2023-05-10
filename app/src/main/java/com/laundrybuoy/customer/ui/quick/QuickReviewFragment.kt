package com.laundrybuoy.customer.ui.quick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.databinding.FragmentQuickReviewBinding
import com.laundrybuoy.customer.model.order.CreateOrderPayload
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.makeButtonDisabled
import com.laundrybuoy.customer.utils.makeButtonEnabled
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickReviewFragment : BaseBottomSheet() {

    lateinit var model: SharedViewModel
    private var _binding: FragmentQuickReviewBinding? = null
    private val binding get() = _binding!!
    private val orderVideModel by viewModels<OrderViewModel>()
    private lateinit var createPayload: CreateOrderPayload

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVm()
        getDataFromVm()
        onClick()
        initObserver()
    }

    private fun initObserver() {
        orderVideModel.createOrderLiveData.observe(viewLifecycleOwner, Observer {
            binding.reviewConfirmBtn.makeButtonDisabled()
            when (it) {
                is NetworkResult.Loading -> {
                    binding.reviewConfirmBtn.makeButtonDisabled()
                }

                is NetworkResult.Success -> {
                    binding.reviewConfirmBtn.makeButtonEnabled()
                    model.setOrderResponse(it.data)
                    model.sendMessage(Constants.FORWARD)
                }

                is NetworkResult.Error -> {
                    binding.reviewConfirmBtn.makeButtonEnabled()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getDataFromVm() {
        model.orderPayload.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                createPayload = it
                setReviewUI(createPayload)
            }
        })
    }

    private fun setReviewUI(createOrderPayload: CreateOrderPayload) {
        binding.reviewServiceNameTv.text = createOrderPayload.serviceName
        binding.reviewApproxQtyTv.text = createOrderPayload.approxCloths + " clothes"
        binding.reviewPickupDateTv.text =
            createOrderPayload.pickupDate + " " + createOrderPayload.timeSlot

        val addressString = createOrderPayload.deliveryAddress?.line1 + " " +
                createOrderPayload.deliveryAddress?.landmark + " " +
                createOrderPayload.deliveryAddress?.city + " " +
                createOrderPayload.deliveryAddress?.pin

        binding.reviewPickupAddressTv.text = addressString
        if (createOrderPayload.coupon.isNullOrEmpty()) {
            binding.reviewCouponTv.text = "-"
        } else {
            binding.reviewCouponTv.text = createOrderPayload.coupon
        }

    }

    private fun initVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private fun onClick() {
        binding.backFromQReviewIv.setOnClickListener {
            model.sendMessage(Constants.BACKWARD)
        }

        binding.reviewConfirmBtn.setOnClickListener {
            orderVideModel.createOrder(createPayload)
        }

    }

//


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickReviewBinding.inflate(inflater, container, false)
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
package com.laundrybuoy.customer.ui.quick

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.gson.Gson
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.adapter.schedule.QuickCouponAdapter
import com.laundrybuoy.customer.databinding.FragmentQuickCouponBinding
import com.laundrybuoy.customer.model.schedule.CouponModel
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.copyToClipboard
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable


@AndroidEntryPoint
class QuickCouponFragment : BaseBottomSheet() {

    private var _binding: FragmentQuickCouponBinding? = null
    private val binding get() = _binding!!
    lateinit var model: SharedViewModel
    val compositeDisposable = CompositeDisposable()
    private lateinit var couponAdapter: QuickCouponAdapter
    private val orderVideModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        initVm()
        initRv()
        fetchCoupons()
//        setDataToRv()
        onClick()
    }

    private fun fetchCoupons() {
        orderVideModel.getCoupons()
    }

    private fun initObserver() {

        orderVideModel.couponsLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        setDataToRv(it.data.data)
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

    private fun setDataToRv(data: List<CouponModel.CouponModelItem>) {
        if (data.isNullOrEmpty()) {
            binding.quickCouponRv.makeViewGone()
            binding.noCouponsTv.makeViewVisible()
        } else {
            binding.quickCouponRv.makeViewVisible()
            binding.noCouponsTv.makeViewGone()
            couponAdapter.submitList(data)

            binding.couponSelectedBtn.makeViewVisible()
            binding.quickCouponNestedSv.post(Runnable {
                binding.quickCouponNestedSv.fullScroll(
                    View.FOCUS_DOWN
                )
            })

        }
    }

    private fun init() {
        compositeDisposable.add(
            RxTextView.textChanges(binding.couponSearchEt)
                .subscribe {
                    if (!it.trim().isNullOrEmpty()) {
                        binding.applyCouponRl.makeViewVisible()
                    } else {
                        binding.applyCouponRl.makeViewGone()
                    }
                })
    }

    private fun onClick() {
        binding.backFromQCouponIv.setOnClickListener {
            model.sendMessage(Constants.BACKWARD)
        }

        binding.couponSelectedBtn.setOnClickListener {
            model.sendMessage(Constants.FORWARD)
        }
    }

    private fun initVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        model.orderPayload.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("yxyx-->", "initObserver: "+ Gson().toJson(it))
            }
        })

    }

    private fun initRv() {
        couponAdapter = QuickCouponAdapter(object : QuickCouponAdapter.OnCouponSelect {
            override fun couponSelected(coupon: CouponModel.CouponModelItem) {
                val currentPayload = model.orderPayload.value
                currentPayload?.coupon = coupon.name
                model.setOrderPayload(currentPayload)
            }

            override fun couponCopied(coupon: CouponModel.CouponModelItem) {
                requireContext().copyToClipboard(coupon.name ?: "")
            }


        })
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.quickCouponRv)
        binding.quickCouponRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.quickCouponRv.adapter = couponAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickCouponBinding.inflate(inflater, container, false)
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
        compositeDisposable.dispose()
    }


}
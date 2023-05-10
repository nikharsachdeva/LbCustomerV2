package com.laundrybuoy.customer.ui.quick

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.databinding.FragmentQuickOrderResultBinding
import com.laundrybuoy.customer.model.order.CreateOrderResponse
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickOrderResultFragment : BaseBottomSheet() {

    private var _binding: FragmentQuickOrderResultBinding? = null
    private val binding get() = _binding!!
    lateinit var model: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun initVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVm()
        onClick()
        initObserver()
    }

    private fun initObserver() {

        model.orderResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setUI(it)
            }
        })

    }

    private fun setUI(it: CreateOrderResponse) {
        if (it.sucess == true) {
            if (it.message == "Order Created") {
                binding.orderPlacedHeadingTv.text = "Thanks for the order!"
                binding.orderPlacedSubHeadingTv.text = "Your laundry needs are in good hands."
            } else {
                binding.orderPlacedHeadingTv.text = "Something went wrong!"
                binding.orderPlacedSubHeadingTv.text = it.message
            }
        } else {
            binding.orderPlacedHeadingTv.text = "Something went wrong!"
            binding.orderPlacedSubHeadingTv.text = it.message
        }
    }

    private fun onClick() {
        binding.goToHomeBtn.setOnClickListener {
            model.sendMessage(Constants.QUICK_HOME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickOrderResultBinding.inflate(inflater, container, false)
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
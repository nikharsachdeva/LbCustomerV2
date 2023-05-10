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
import com.google.gson.Gson
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.adapter.address.QuickAddressAdapter
import com.laundrybuoy.customer.databinding.FragmentQuickAddressBinding
import com.laundrybuoy.customer.model.address.GetUserAddressResponse
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuickAddressFragment : BaseBottomSheet() {

    private var _binding: FragmentQuickAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var addressAdapter: QuickAddressAdapter
    lateinit var model: SharedViewModel
    private val orderVideModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSharedVm()
        initRv()
        initObserver()
        getAddresses()
        onClick()

    }

    private fun getAddresses() {
        orderVideModel.getAddressBook()

    }

    private fun initObserver() {
        orderVideModel.addressLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        setData(it.data.data)
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


        model.orderPayload.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("xxyyzz-->", "initObserver: "+Gson().toJson(it))
            }
        })

    }

    private fun initSharedVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }


    private fun onClick() {
        binding.backFromQAddressIv.setOnClickListener {
            model.sendMessage(Constants.BACKWARD)
        }
    }


    private fun setData(listOfAddress: List<GetUserAddressResponse.Data>) {
        if (listOfAddress.isNullOrEmpty()) {
            binding.addressAvailableRv.makeViewGone()
            binding.addressUnAvailableLl.makeViewVisible()
        } else {
            binding.addressAvailableRv.makeViewVisible()
            binding.addressUnAvailableLl.makeViewGone()
            addressAdapter.submitList(listOfAddress)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRv() {
        addressAdapter = QuickAddressAdapter(object : QuickAddressAdapter.AddressClickListener {
            override fun onAddressSelected(address: GetUserAddressResponse.Data) {

                var currentPayload = model.orderPayload.value
                currentPayload?.deliveryAddress = address.address
                model.setOrderPayload(currentPayload)
                model.sendMessage(Constants.FORWARD)
            }

        })
        binding.addressAvailableRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.addressAvailableRv.adapter = addressAdapter
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
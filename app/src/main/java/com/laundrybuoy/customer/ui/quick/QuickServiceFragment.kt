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
import com.laundrybuoy.customer.adapter.schedule.QuickServiceAdapter
import com.laundrybuoy.customer.databinding.FragmentQuickServiceBinding
import com.laundrybuoy.customer.model.order.CreateOrderPayload
import com.laundrybuoy.customer.model.schedule.ServiceModel
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuickServiceFragment : BaseBottomSheet() {

    private var _binding: FragmentQuickServiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var serviceAdapter: QuickServiceAdapter
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
        getServices()
        onClick()
    }

    private fun initObserver() {

        orderVideModel.servicesLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        if (!it.data.data.isNullOrEmpty()) {
                            setData(it.data.data)
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

    private fun getServices() {
        orderVideModel.getServices()
    }

    private fun onClick() {
        binding.backFromQServiceIv.setOnClickListener {
            model.sendMessage(Constants.BACKWARD)
        }
    }

    private fun initSharedVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }


    private fun initRv() {

        serviceAdapter = QuickServiceAdapter(object : QuickServiceAdapter.OnServiceSelect {
            override fun serviceSelected(service: ServiceModel.Data) {
                var currentPayload = model.orderPayload.value
                currentPayload?.serviceId = service.id
                currentPayload?.serviceName = service.serviceName
                model.setOrderPayload(currentPayload)
                model.sendMessage(Constants.FORWARD)
            }

        })
        binding.scheduleServiceRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.scheduleServiceRv.adapter = serviceAdapter

    }

    private fun setData(data: List<ServiceModel.Data?>?) {

        /*
        val listOfService: MutableList<ServiceModel.Data> = arrayListOf()
        listOfService.add(
            ServiceModel.Data(
                costPerKg = 1.1,
                costPerCloth = 1.1,
                createdAt = "",
                deliveryKg = 1.1,
                deliveryCloth = 1.1,
                id = "1",
                isActive = true,
                updatedAt = "",
                v = 1,
                serviceImage = "",
                serviceName = "Wash"
            )
        )

        listOfService.add(
            ServiceModel.Data(
                costPerKg = 1.1,
                costPerCloth = 1.1,
                createdAt = "",
                deliveryKg = 1.1,
                deliveryCloth = 1.1,
                id = "2",
                isActive = true,
                updatedAt = "",
                v = 2,
                serviceImage = "",
                serviceName = "Iron"
            )
        )

        listOfService.add(
            ServiceModel.Data(
                costPerKg = 1.1,
                costPerCloth = 1.1,
                createdAt = "",
                deliveryKg = 1.1,
                deliveryCloth = 1.1,
                id = "3",
                isActive = true,
                updatedAt = "",
                v = 3,
                serviceImage = "",
                serviceName = "Dry Clean"
            )
        )

        listOfService.add(
            ServiceModel.Data(
                costPerKg = 1.1,
                costPerCloth = 1.1,
                createdAt = "",
                deliveryKg = 1.1,
                deliveryCloth = 1.1,
                id = "4",
                isActive = true,
                updatedAt = "",
                v = 4,
                serviceImage = "",
                serviceName = "Laundry"
            )
        )
         */

        serviceAdapter.submitList(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickServiceBinding.inflate(inflater, container, false)
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
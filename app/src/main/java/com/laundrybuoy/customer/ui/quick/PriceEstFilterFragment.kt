package com.laundrybuoy.customer.ui.quick

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.price.QuantityTypeAdapter
import com.laundrybuoy.customer.adapter.schedule.QuickServiceAdapter
import com.laundrybuoy.customer.databinding.FragmentPriceEstFilterBinding
import com.laundrybuoy.customer.databinding.FragmentQuickAddressBinding
import com.laundrybuoy.customer.databinding.FragmentUpdateProfileBinding
import com.laundrybuoy.customer.model.price.QuantityTypeModel
import com.laundrybuoy.customer.model.schedule.ServiceModel
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.makeButtonDisabled
import com.laundrybuoy.customer.utils.makeButtonEnabled
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PriceEstFilterFragment : BaseBottomSheet() {

    private var _binding: FragmentPriceEstFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var serviceAdapter: QuickServiceAdapter
    private lateinit var quantityAdapter: QuantityTypeAdapter
    private val orderVideModel by viewModels<OrderViewModel>()
    lateinit var model: SharedViewModel

    var typeSelected: String? = null
    var serviceSelected: String? = null

    private lateinit var callback: (String,String) -> Unit
    fun setCallback(callback: (String,String) -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVm()
        onClick()
        initServiceRv()
        initQtyRv()
        initObserver()
        fetchServices()
        setQuantityData()
        binding.applyPriceFilterBtn.makeButtonDisabled()
//        model.setPriceFilter(Pair(model.priceFilterPair.value?.first, model.priceFilterPair.value?.second))
    }

    private fun initVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private fun setQuantityData() {
        val listOfQuantity: MutableList<QuantityTypeModel.Data> = arrayListOf()
        listOfQuantity.add(
            QuantityTypeModel.Data(
                id = "cloth",
                qtyImage = R.drawable.shirt,
                qtyType = "By Piece"
            )
        )

        listOfQuantity.add(
            QuantityTypeModel.Data(
                id = "weight",
                qtyImage = R.drawable.kilogram,
                qtyType = "By Weight"
            )
        )

        quantityAdapter.submitList(listOfQuantity)
    }

    private fun initQtyRv() {
        quantityAdapter = QuantityTypeAdapter(object : QuantityTypeAdapter.OnQtySelect {
            override fun typeSelected(qtyItem: QuantityTypeModel.Data) {
                typeSelected = qtyItem.id
                if (typeSelected.isNullOrEmpty() || serviceSelected.isNullOrEmpty()) {
                    binding.applyPriceFilterBtn.makeButtonDisabled()
                } else {
                    binding.applyPriceFilterBtn.makeButtonEnabled()
                }
//                model.priceFilterPair.value = Pair(model.priceFilterPair.value?.first, qtyItem.id)
            }


        })
        binding.filterQuantityRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filterQuantityRv.adapter = quantityAdapter
    }

    private fun onClick() {
        binding.closeFilterIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.applyPriceFilterBtn.setOnClickListener {
            if(::callback.isInitialized){
                dialog?.dismiss()
                callback.invoke(serviceSelected!!,typeSelected!!)
            }
        }
    }

    private fun fetchServices() {
        orderVideModel.getServices()
    }

    private fun initObserver() {

        /*
        model.priceFilterPair.observe(viewLifecycleOwner, Observer { pairReceived ->
            if (pairReceived.first.isNullOrEmpty() || pairReceived.second.isNullOrEmpty()) {
                binding.applyPriceFilterBtn.makeButtonDisabled()
            } else {
                binding.applyPriceFilterBtn.makeButtonEnabled()
            }

        })

         */

        orderVideModel.servicesLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        if (!it.data.data.isNullOrEmpty()) {
                            setServiceData(it.data.data)
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

    private fun setServiceData(data: List<ServiceModel.Data?>) {
        serviceAdapter.submitList(data)
    }


    private fun initServiceRv() {

        serviceAdapter = QuickServiceAdapter(object : QuickServiceAdapter.OnServiceSelect {
            override fun serviceSelected(service: ServiceModel.Data) {
                serviceSelected = service.id
                if (typeSelected.isNullOrEmpty() || serviceSelected.isNullOrEmpty()) {
                    binding.applyPriceFilterBtn.makeButtonDisabled()
                } else {
                    binding.applyPriceFilterBtn.makeButtonEnabled()
                }
//                model.priceFilterPair.value = Pair(service.id, model.priceFilterPair.value?.second)
            }

        })
        binding.filterServiceRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filterServiceRv.adapter = serviceAdapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPriceEstFilterBinding.inflate(inflater, container, false)
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
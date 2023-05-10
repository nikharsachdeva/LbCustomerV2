package com.laundrybuoy.customer.ui.quick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.adapter.schedule.QuickQtyAdapter
import com.laundrybuoy.customer.databinding.FragmentQuickClothesBinding
import com.laundrybuoy.customer.model.schedule.OrderQtyModel
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickClothesFragment : BaseBottomSheet() {


    private var _binding: FragmentQuickClothesBinding? = null
    private val binding get() = _binding!!
    private lateinit var qtyAdapter: QuickQtyAdapter
    lateinit var model: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSharedVm()
        onClick()
        initQuantityRv()
        setData()
    }

    private fun onClick() {
        binding.backFromQclothIv.setOnClickListener {
            model.sendMessage(Constants.BACKWARD)
        }
    }

    private fun initSharedVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private fun setData() {
        val listOfQty: MutableList<OrderQtyModel.OrderQtyModelItem> = arrayListOf()
        listOfQty.add(OrderQtyModel.OrderQtyModelItem(
            qty = "10-15"
        ))
        listOfQty.add(OrderQtyModel.OrderQtyModelItem(
            qty = "15-20"
        ))
        listOfQty.add(OrderQtyModel.OrderQtyModelItem(
            qty = "20-25"
        ))
        listOfQty.add(OrderQtyModel.OrderQtyModelItem(
            qty = "More than 25"
        ))

        qtyAdapter.submitList(listOfQty)

    }

    private fun initQuantityRv() {
        qtyAdapter = QuickQtyAdapter(object : QuickQtyAdapter.QuantitySelectListener{
            override fun onQuantitySelected(qty: OrderQtyModel.OrderQtyModelItem) {
                val currentPayload = model.orderPayload.value
                currentPayload?.approxCloths = qty.qty
                model.setOrderPayload(currentPayload)
                model.sendMessage(Constants.FORWARD)
            }

        })
        binding.scheduleQtyRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.scheduleQtyRv.adapter = qtyAdapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickClothesBinding.inflate(inflater, container, false)
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
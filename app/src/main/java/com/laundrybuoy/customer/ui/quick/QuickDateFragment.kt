package com.laundrybuoy.customer.ui.quick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.adapter.schedule.QuickDateAdapter
import com.laundrybuoy.customer.adapter.schedule.QuickQtyAdapter
import com.laundrybuoy.customer.databinding.FragmentQuickDateBinding
import com.laundrybuoy.customer.model.schedule.OrderQtyModel
import com.laundrybuoy.customer.model.schedule.PickupDateResponse
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickDateFragment : BaseBottomSheet() {

    private var _binding: FragmentQuickDateBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateAdapter: QuickDateAdapter
    lateinit var model: SharedViewModel
    private val orderVideModel by viewModels<OrderViewModel>()
    private lateinit var timeSlotAdapter: QuickQtyAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initViewModel()
        initDateRv()

        initTimeRv()
        setTimeSlots()

        fetchValidDates()

        onClick()
    }

    private fun setTimeSlots() {
        val listOfQty: MutableList<OrderQtyModel.OrderQtyModelItem> = arrayListOf()
        listOfQty.add(OrderQtyModel.OrderQtyModelItem(
            qty = "9AM-12PM"
        ))
        listOfQty.add(OrderQtyModel.OrderQtyModelItem(
            qty = "12PM-3PM"
        ))
        listOfQty.add(OrderQtyModel.OrderQtyModelItem(
            qty = "3PM-6PM"
        ))
        timeSlotAdapter.submitList(listOfQty)
    }

    private fun initTimeRv() {
        timeSlotAdapter = QuickQtyAdapter(object : QuickQtyAdapter.QuantitySelectListener{
            override fun onQuantitySelected(qty: OrderQtyModel.OrderQtyModelItem) {
                val currentPayload = model.orderPayload.value
                currentPayload?.timeSlot = qty.qty
                currentPayload?.isPrime = false
                model.setOrderPayload(currentPayload)
                model.sendMessage(Constants.FORWARD)
            }

        })
        binding.quickTimeSlotRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.quickTimeSlotRv.adapter = timeSlotAdapter

    }

    private fun initObserver() {
        orderVideModel.validTimeSlotsLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {

                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true) {
                            setDatesData(it.data.data)
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

    private fun setDatesData(data: List<PickupDateResponse.Data?>?) {

        if (!data.isNullOrEmpty()) {
            data.forEach {
                it?.dateOfSlot = it?.date?.getOnlyDate()
                it?.monthOfSlot = it?.date?.getOnlyMonth()
            }
            binding.slotsAvailableRl.makeViewVisible()
            binding.slotsUnavailableTv.makeViewGone()
            binding.dateInfoIv.makeViewVisible()
            binding.quickTimeSlotRv.makeViewVisible()

            dateAdapter.submitList(data)

            val offSet = data[0]?.offset
            binding.dateInfoIv.setOnClickListener {
                Toast.makeText(requireContext(), "Same day pickup slots are available till $offSet", Toast.LENGTH_SHORT).show()
//                viewLifecycleOwner.showBalloonText("Same day pickup slots are available till $offSet", requireContext())
            }

        } else {
            binding.quickTimeSlotRv.makeViewGone()
            binding.dateInfoIv.makeViewGone()
            binding.slotsAvailableRl.makeViewGone()
            binding.slotsUnavailableTv.makeViewVisible()
        }
    }

    private fun fetchValidDates() {
        orderVideModel.getValidTimeSlots()
    }

    private fun onClick() {
        binding.backFromQSDateIv.setOnClickListener {
            model.sendMessage(Constants.BACKWARD)
        }
    }

    private fun initViewModel() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private fun initDateRv() {
        dateAdapter = QuickDateAdapter(object : QuickDateAdapter.DateSelectListener {
            override fun onDateSelected(date: PickupDateResponse.Data) {
                binding.quickTimeSlotRv.makeViewVisible()
                val currentPayload = model.orderPayload.value
                currentPayload?.pickupDate = date.date?.getISOToNormal()
                model.setOrderPayload(currentPayload)
                binding.quickDateNestedSv.post(Runnable { binding.quickDateNestedSv.fullScroll(View.FOCUS_DOWN) })
            }

        })
        binding.pickupDateRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.pickupDateRv.adapter = dateAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuickDateBinding.inflate(inflater, container, false)
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
package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.adapter.schedule.SelectQtyAdapter
import com.laundrybuoy.customer.adapter.schedule.SelectServiceAdapter
import com.laundrybuoy.customer.databinding.FragmentScheduleBinding
import com.laundrybuoy.customer.model.address.GetUserAddressResponse
import com.laundrybuoy.customer.model.schedule.OrderQtyModel
import com.laundrybuoy.customer.model.schedule.ServiceModel
import com.laundrybuoy.customer.utils.Constants
import com.laundrybuoy.customer.utils.makeButtonDisabled
import com.laundrybuoy.customer.utils.makeButtonEnabled
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : BaseFragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var serviceAdapter: SelectServiceAdapter

    //    private lateinit var dateAdapter: SelectDateAdapter
    private lateinit var qtyAdapter: SelectQtyAdapter
    private val orderViewModel by viewModels<OrderViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNav()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        setData()
//        setDates()
        onClick()
        initObserver()

        orderViewModel.setOrderPayload(null)
    }

    private fun initObserver() {
        orderViewModel.scheduleOrderPayloadLD.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { payload ->
                if (payload == null) {
                    binding.proceedScheduleBtn.makeButtonDisabled()
                } else {
                    binding.proceedScheduleBtn.makeButtonEnabled()
                }
            })
    }

    private fun onClick() {

        binding.backFromScheduleIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.selectAddressRl.setOnClickListener {
            val addressBtmSheet =
                AddressSelectFragment.newInstance(Constants.SCREEN_TYPE_SCHEDULE)
            addressBtmSheet.setCallback {
                setSelectedAddress(it)
            }
            addressBtmSheet.isCancelable = true
            addressBtmSheet.show(childFragmentManager, "address_select")
        }
    }

    private fun setSelectedAddress(address: GetUserAddressResponse.Data) {
        val addressAsString =
            address.address?.line1 + "," + address?.address?.pin + "," + address?.address?.state
        binding.scheduleAddressTv.text = addressAsString
    }

    /*
    fun setDates() {
        val sdfDate = SimpleDateFormat("dd")
        val sdfDay = SimpleDateFormat("EEEE")
        val sdfMonth = SimpleDateFormat("MMM")

        val listOf7Dates: MutableList<PickupDateModel.PickupDateModelItem> = arrayListOf()

        for (i in 0..6) {
            val calendar: Calendar = GregorianCalendar()
            calendar.add(Calendar.DATE, i)
            listOf7Dates.add(
                PickupDateModel.PickupDateModelItem(
                    date = sdfDate.format(calendar.time),
                    month = (sdfMonth.format(calendar.time)).substring(0, 3),
                    day = (sdfDay.format(calendar.time)).substring(0, 3)
                )
            )
        }

        dateAdapter.submitList(listOf7Dates)

    }

     */

    private fun setData() {
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

        val listOfQty: MutableList<OrderQtyModel.OrderQtyModelItem> = arrayListOf()
        listOfQty.add(
            OrderQtyModel.OrderQtyModelItem(
                qty = "10-15"
            )
        )
        listOfQty.add(
            OrderQtyModel.OrderQtyModelItem(
                qty = "15-20"
            )
        )
        listOfQty.add(
            OrderQtyModel.OrderQtyModelItem(
                qty = "20-25"
            )
        )
        listOfQty.add(
            OrderQtyModel.OrderQtyModelItem(
                qty = "More than 25"
            )
        )


        serviceAdapter.submitList(listOfService)
        qtyAdapter.submitList(listOfQty)
    }

    private fun initRv() {

        serviceAdapter = SelectServiceAdapter()
        binding.scheduleServiceRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.scheduleServiceRv.adapter = serviceAdapter


        /*
        dateAdapter = SelectDateAdapter()
        binding.pickupDateRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.pickupDateRv.adapter = dateAdapter
                 */

        qtyAdapter = SelectQtyAdapter()
        binding.scheduleQtyRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.scheduleQtyRv.adapter = qtyAdapter


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
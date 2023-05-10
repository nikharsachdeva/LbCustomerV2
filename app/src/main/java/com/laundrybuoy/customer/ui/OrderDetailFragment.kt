package com.laundrybuoy.customer.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.order.CustomerOrderItemsAdapter
import com.laundrybuoy.customer.databinding.FragmentOrderDetailBinding
import com.laundrybuoy.customer.databinding.FragmentOrdersBinding
import com.laundrybuoy.customer.model.order.OrderDetailResponse
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.getFormattedDateWithTime
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment : BaseBottomSheet() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val orderViewModel by viewModels<OrderViewModel>()
    var orderIdReceived: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        onClick()
        if (isAdded) {
            orderIdReceived = requireArguments().getString(ORDER_ID)
            orderIdReceived?.let { getOrderDetails(it) }
        }
    }

    private fun onClick() {
        binding.backFromOrderDetailIv.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun initObserver() {
        orderViewModel.orderDetailLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data.message == "Order data found") {
                        setUI(it.data.data)
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

    private fun setUI(data: OrderDetailResponse.Data?) {
        binding.orderNumberTv.text = "#${data?.orderDetails?.ordNum}"
        binding.orderDateTimeTv.text = data?.orderDetails?.orderDate?.getFormattedDateWithTime()

        val addressString = data?.orderDetails?.deliveryAddress?.line1 + " " +
                data?.orderDetails?.deliveryAddress?.landmark + " " +
                data?.orderDetails?.deliveryAddress?.city + " " + data?.orderDetails?.deliveryAddress?.state + " " +
                data?.orderDetails?.deliveryAddress?.pin
        binding.orderAddressTv.text = addressString

        binding.orderServiceTv.text = data?.orderDetails?.serviceId?.serviceName

        if (!data?.bill?.items.isNullOrEmpty()) {
            binding.billDetailAvailableRl.makeViewVisible()
            binding.billDetailUnAvailableLl.makeViewGone()
            val itemsAdapter = CustomerOrderItemsAdapter()
            binding.billItemsRv.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            binding.billItemsRv.adapter = itemsAdapter
            itemsAdapter.submitList(data?.bill?.items)

            var totalItems = 0
            data?.bill?.items?.map {
                totalItems += it?.quantity!!
            }
            binding.billItemHeadingTv.text="Bill Items ($totalItems)"
        } else {
            binding.billDetailAvailableRl.makeViewGone()
            binding.billDetailUnAvailableLl.makeViewVisible()
        }
    }

    private fun getOrderDetails(orderIdReceived: String) {
        orderViewModel.getOrderDetails(orderIdReceived)
    }

    companion object {
        private const val ORDER_ID = "ORDER_ID"

        fun newInstance(
            orderId: String?,
        ): OrderDetailFragment {
            val orderDetailFragment = OrderDetailFragment()
            val args = Bundle()
            args.putString(ORDER_ID, orderId)
            orderDetailFragment.arguments = args
            return orderDetailFragment
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(true)
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
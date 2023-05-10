package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.adapter.PagingLoaderAdapter
import com.laundrybuoy.customer.adapter.order.CustomerOrderPagingAdapter
import com.laundrybuoy.customer.databinding.FragmentOrdersBinding
import com.laundrybuoy.customer.model.order.CustomerOrdersModel
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val orderViewModel by viewModels<OrderViewModel>()
    lateinit var customerOrderPagingAdapter: CustomerOrderPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOrderRv()
        fetchUserOrders()

    }

    private fun initOrderRv() {
        customerOrderPagingAdapter =
            CustomerOrderPagingAdapter(object : CustomerOrderPagingAdapter.OnClickInterface {
                override fun onSelected(order: CustomerOrdersModel.Data.Partner, position: Int) {
                    val orderDetailBtmSheet =
                        OrderDetailFragment.newInstance(orderId = order.orderDetails.id ?: null)
                    orderDetailBtmSheet
                    orderDetailBtmSheet.isCancelable = false
                    orderDetailBtmSheet.show(childFragmentManager, "address_add")
                }

                override fun onRatingSelected(item: CustomerOrdersModel.Data.Partner) {
                    checkRatingAndProceed(item)
                }
            })

        binding.customerOrderRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.customerOrderRv.setHasFixedSize(true)
        binding.customerOrderRv.adapter = customerOrderPagingAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoaderAdapter(),
            footer = PagingLoaderAdapter()
        )
        customerOrderPagingAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.Loading) {
                showShimmer()
            } else if (loadState.source.refresh is LoadState.NotLoading) {
                hideShimmer()
                if (loadState.append.endOfPaginationReached && customerOrderPagingAdapter.itemCount < 1) {
                    binding.ordersUnAvailableLl.makeViewVisible()
                    binding.customerOrderRv.makeViewGone()
                } else {
                    binding.ordersUnAvailableLl.makeViewGone()
                    binding.customerOrderRv.makeViewVisible()
                }
            }
        }

    }

    private fun checkRatingAndProceed(order: CustomerOrdersModel.Data.Partner) {
        val listOfUnReviewed = order.ratings.filter {
            it.isRated == false
        }
        if (!listOfUnReviewed.isNullOrEmpty()) {
            val bundle = Bundle().apply {
                putParcelableArrayList("ratingsList", ArrayList(listOfUnReviewed))
            }
            val feedbackRootFrag = FeedbackRootFragment()
            feedbackRootFrag.arguments = bundle
            feedbackRootFrag.setCallback {
                fetchUserOrders()
            }
            feedbackRootFrag.isCancelable = true
            feedbackRootFrag.show(childFragmentManager, "feedback_root")

        } else {
            getMainActivity()?.showSnackBar("You have already rated!")
        }
    }

    fun showShimmer() {
        binding.myOrderShimmerSv.pendingOrderShimmerSl.makeViewVisible()
        binding.myOrderShimmerSv.pendingOrderShimmerSl.startShimmer()
        binding.customerOrderRv.makeViewGone()
        binding.ordersUnAvailableLl.makeViewGone()
        binding.myOrderHeadingTv.makeViewGone()
    }

    fun hideShimmer() {
        binding.myOrderShimmerSv.pendingOrderShimmerSl.makeViewGone()
        binding.myOrderShimmerSv.pendingOrderShimmerSl.stopShimmer()
        binding.customerOrderRv.makeViewVisible()
        binding.ordersUnAvailableLl.makeViewVisible()
        binding.myOrderHeadingTv.makeViewVisible()
    }

    fun fetchUserOrders() {
        viewLifecycleOwner.lifecycleScope.launch {
            orderViewModel.getCustomerOrders().observe(viewLifecycleOwner) {
                customerOrderPagingAdapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
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
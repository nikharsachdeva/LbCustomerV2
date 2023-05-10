package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.adapter.PagingLoaderAdapter
import com.laundrybuoy.customer.adapter.subscription.CustomerSubscriptionPagingAdapter
import com.laundrybuoy.customer.databinding.FragmentMySubscriptionBinding
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MySubscriptionFragment : BaseFragment() {

    private var _binding: FragmentMySubscriptionBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    lateinit var customerSubListAdapter: CustomerSubscriptionPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSubRv()
        initObserver()
        onClick()
        fetchMySubscriptionHistory()

    }

    private fun onClick() {
        binding.backFromSubsIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    private fun initObserver() {

    }

    private fun initSubRv() {
        customerSubListAdapter =
            CustomerSubscriptionPagingAdapter(object :
                CustomerSubscriptionPagingAdapter.OnClickInterface {
                override fun onSelected(
                    order: CustomerTransactionModel.Data.Partner,
                    position: Int,
                ) {

                }

            })

        binding.subListingRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.subListingRv.setHasFixedSize(true)
        binding.subListingRv.adapter = customerSubListAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoaderAdapter(),
            footer = PagingLoaderAdapter()
        )
        customerSubListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.Loading) {
                showShimmer()
            } else if (loadState.source.refresh is LoadState.NotLoading) {
                hideShimmer()
                if (loadState.append.endOfPaginationReached && customerSubListAdapter.itemCount < 1) {
                    binding.subUnAvailableLl.makeViewVisible()
                    binding.subAvailableLl.makeViewGone()
                } else {
                    binding.subUnAvailableLl.makeViewGone()
                    binding.subAvailableLl.makeViewVisible()
                    setUI()
                }
            }
        }

    }

    fun showShimmer() {
        binding.mySubShimmerSv.pendingOrderShimmerSl.makeViewVisible()
        binding.mySubShimmerSv.pendingOrderShimmerSl.startShimmer()
        binding.subUnAvailableLl.makeViewGone()
        binding.subAvailableLl.makeViewGone()
    }

    fun hideShimmer() {
        binding.mySubShimmerSv.pendingOrderShimmerSl.makeViewGone()
        binding.mySubShimmerSv.pendingOrderShimmerSl.stopShimmer()
        binding.subUnAvailableLl.makeViewVisible()
        binding.subAvailableLl.makeViewVisible()
    }

    private fun setUI() {
        val transactions = customerSubListAdapter.snapshot().items
        if (!transactions.isNullOrEmpty()) {
            try {
                val subType =
                    if (transactions[0].subscriptionId?.quantityType == "weight") "kg" else "cloth"
                binding.currentBalTv.text = "${transactions[0].balanceCurrent} $subType"
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.currentBalTv.text = "0"
        }
    }

    private fun fetchMySubscriptionHistory() {
        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.getCustomerSubscription()
                .observe(viewLifecycleOwner) {
                    customerSubListAdapter.submitData(lifecycle, it)
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMySubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        fun newInstance(): MySubscriptionFragment {
            val viewFragment = MySubscriptionFragment()
            val args = Bundle()
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(true)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
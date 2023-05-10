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
import com.laundrybuoy.customer.adapter.coins.CustomerCoinsPagingAdapter
import com.laundrybuoy.customer.databinding.FragmentMyCoinsBinding
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class MyCoinsFragment : BaseFragment() {

    private var _binding: FragmentMyCoinsBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    lateinit var customerCoinsListAdapter: CustomerCoinsPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCoinsRv()
        fetchUserCoins()
        onClick()

    }

    private fun onClick() {
        binding.backFromMyCoinsIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    private fun initCoinsRv() {
        customerCoinsListAdapter =
            CustomerCoinsPagingAdapter(object : CustomerCoinsPagingAdapter.OnClickInterface {
                override fun onSelected(
                    order: CustomerTransactionModel.Data.Partner,
                    position: Int,
                ) {

                }

            })

        binding.customerCoinsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.customerCoinsRv.setHasFixedSize(true)
        binding.customerCoinsRv.adapter = customerCoinsListAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoaderAdapter(),
            footer = PagingLoaderAdapter()
        )
        customerCoinsListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.Loading) {
                showShimmer()
            } else if (loadState.source.refresh is LoadState.NotLoading) {
                hideShimmer()
                if (loadState.append.endOfPaginationReached && customerCoinsListAdapter.itemCount < 1) {
                    binding.coinsUnAvailableLl.makeViewVisible()
                    binding.coinsAvailableRl.makeViewGone()
                } else {
                    binding.coinsUnAvailableLl.makeViewGone()
                    binding.coinsAvailableRl.makeViewVisible()
                    setUI()
                }
            }
        }

    }

    private fun setUI() {
        val transactions = customerCoinsListAdapter.snapshot().items
        if(!transactions.isNullOrEmpty()){
            try {
                val bal = transactions[0].balanceCurrent
                if(bal==0.0){
                    binding.currentBalTv.text = bal.roundToInt().toString()
                }else{
                    binding.currentBalTv.text = bal?.toString()
                }
            }catch (e: Exception){
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }else{
            binding.currentBalTv.text = "0"
        }
    }

    fun showShimmer() {
        binding.myCoinShimmerSv.pendingOrderShimmerSl.makeViewVisible()
        binding.myCoinShimmerSv.pendingOrderShimmerSl.startShimmer()
        binding.coinsAvailableRl.makeViewGone()
        binding.coinsUnAvailableLl.makeViewGone()
        binding.coinsHeadingLl.makeViewGone()
    }

    fun hideShimmer() {
        binding.myCoinShimmerSv.pendingOrderShimmerSl.makeViewGone()
        binding.myCoinShimmerSv.pendingOrderShimmerSl.stopShimmer()
        binding.coinsAvailableRl.makeViewVisible()
        binding.coinsUnAvailableLl.makeViewVisible()
        binding.coinsHeadingLl.makeViewVisible()
    }

    private fun fetchUserCoins() {
        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.getCustomerCoins().observe(viewLifecycleOwner) {
                customerCoinsListAdapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMyCoinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        fun newInstance(): MyCoinsFragment {
            val viewFragment = MyCoinsFragment()
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
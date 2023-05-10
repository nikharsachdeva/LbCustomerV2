package com.laundrybuoy.customer.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.adapter.price.PricePagerAdapter
import com.laundrybuoy.customer.adapter.price.SelectionInterface
import com.laundrybuoy.customer.databinding.FragmentPricesBinding
import com.laundrybuoy.customer.model.price.InventoryListSegregated
import com.laundrybuoy.customer.model.price.InventoryModel
import com.laundrybuoy.customer.ui.quick.PriceEstFilterFragment
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.utils.roundOffDecimal
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import okhttp3.internal.filterList

@AndroidEntryPoint
class PricesFragment : BaseFragment() {

    private var _binding: FragmentPricesBinding? = null
    private val binding get() = _binding!!

    var inventoryListDummy: MutableList<InventoryModel.Data.Item> = arrayListOf()
    var listSegregatted: MutableList<InventoryListSegregated.InventoryListSegregatedItem> =
        arrayListOf()

    var finalSelectionList: MutableList<InventoryModel.Data.Item> = arrayListOf()
    var finalFilteredList: MutableList<InventoryModel.Data.Item> = arrayListOf()
    var serviceCostReceived: Double? = null
    val compositeDisposable = CompositeDisposable()
    private val orderViewModel by viewModels<OrderViewModel>()
    lateinit var model: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVm()
        setBottomNav()
        initObserver()
        onClick()
        model.setPriceFilter(Pair(null, null))
    }

    private fun initVm() {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private fun fetchPriceList(serviceId: String?, qtyType: String?) {
        val payload = JsonObject()
        payload.addProperty("serviceId", serviceId)
        payload.addProperty("costType", qtyType)
        orderViewModel.fetchListItems(payload)
    }

    private fun initObserver() {

        model.priceFilterPair.observe(viewLifecycleOwner, Observer { pairReceived ->
            if (!pairReceived.first.isNullOrEmpty() || !pairReceived.second.isNullOrEmpty()) {
                fetchPriceList(pairReceived.first,pairReceived.second)
                binding.priceListUnAvailableLl.makeViewGone()
                binding.priceListAvailableRl.makeViewVisible()
            }else{
                binding.priceListUnAvailableLl.makeViewVisible()
                binding.priceListAvailableRl.makeViewGone()
            }

        })


        orderViewModel.priceListLiveData.observe(viewLifecycleOwner, Observer {
            floatOutAnimation()
            showShimmer()
            when (it) {
                is NetworkResult.Loading -> {
                    showShimmer()
                }

                is NetworkResult.Success -> {
                    hideShimmer()
                    if (it.data?.status == true && it.data.data?.items?.isNotEmpty()!!) {
                        for (item in it.data.data.items) {
                            item.selectedQty = 0
                        }
                        serviceCostReceived = it.data.data.serviceCost
                        processListOfItems(it.data.data.items.toMutableList())
                    } else {
                        getMainActivity()?.showSnackBar(it.data?.message)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmer()
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

    }

    private fun processListOfItems(data: MutableList<InventoryModel.Data.Item>) {

        inventoryListDummy.clear()
        listSegregatted.clear()
        finalSelectionList.clear()
        finalFilteredList.clear()

        //save original list
        inventoryListDummy = data

        //get all the distinct categories
        val listOfCategories = inventoryListDummy.map {
            it.category?.categoryName
        }.distinct()

        //alter structure of list as per Ui needs
        for (cat in listOfCategories) {
            val sampleList = data.filterList {
                this.category?.categoryName == cat
            }
            listSegregatted.add(
                InventoryListSegregated.InventoryListSegregatedItem(
                    catName = cat,
                    catList = sampleList
                )
            )
        }
        setupUI(listSegregatted)

    }

    private fun setupUI(listSegregatted: MutableList<InventoryListSegregated.InventoryListSegregatedItem>) {
        binding.addItemRootViewPager.adapter =
            PricePagerAdapter(
                childFragmentManager,
                listSegregatted,
                PricePagerAdapter.TYPE_INVENTORY_FRAGMENT,
                object : SelectionInterface {
                    override fun newListIsHere(mutableList: MutableList<InventoryModel.Data.Item>) {
                        finalSelectionList.addAll(mutableList)
                        calculateTotal(finalSelectionList.distinct().filter {
                            it.selectedQty != 0
                        }, mutableList)
                    }

                }
            )

        binding.addItemRootTabLayout.setupWithViewPager(binding.addItemRootViewPager)
        binding.addItemRootTabLayout.tabRippleColor = null
        binding.addItemRootViewPager.offscreenPageLimit = listSegregatted.size

        binding.addItemRootTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

    private fun calculateTotal(
        filteredList: List<InventoryModel.Data.Item>,
        unFilteredList: List<InventoryModel.Data.Item>,
    ) {
        finalFilteredList = filteredList.toMutableList()
        var total = 0.0
        if (filteredList.isEmpty()) {
            binding.itemCountTv.text = "0 Items"
            binding.billTotalTv.text = "₹0"
            floatOutAnimation()
        } else {
            filteredList.forEach { it ->
                total += (((it.selectedQty)?.times((serviceCostReceived?.times(it.eqCloths!!)!!))!!))
            }
            total = total.roundOffDecimal()!!
            binding.itemCountTv.text = "${filteredList.size} Items"
            binding.billTotalTv.text = "₹${total}"
            floatInAnimation()
        }

    }

    private fun floatOutAnimation() {
        ObjectAnimator.ofFloat(binding.addItemContinueRl, "translationY", 200f).apply {
            duration = 200
            start()
        }
    }

    private fun floatInAnimation() {
        ObjectAnimator.ofFloat(binding.addItemContinueRl, "translationY", -200f).apply {
            duration = 200
            start()
        }

    }

    private fun onClick() {
        binding.priceFilterIv.setOnClickListener {
            val priceFilterBtmSht = PriceEstFilterFragment()
            priceFilterBtmSht.setCallback { serviceIdReceived, typeReceived ->
                model.setPriceFilter(Pair(serviceIdReceived,typeReceived))
            }
            priceFilterBtmSht.isCancelable = true
            priceFilterBtmSht.show(childFragmentManager, "filter_price")
        }

    }

    fun showShimmer() {
        binding.priceShimmerSl.visibility = View.VISIBLE
        binding.priceShimmerSl.startShimmer()
        binding.priceListAvailableRl.makeViewGone()
    }

    fun hideShimmer() {
        binding.priceShimmerSl.visibility = View.GONE
        binding.priceShimmerSl.stopShimmer()
        binding.priceListAvailableRl.makeViewVisible()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPricesBinding.inflate(inflater, container, false)
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
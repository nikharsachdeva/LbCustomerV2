package com.laundrybuoy.customer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.FtsOptions.Order
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.price.PriceItemAdapter
import com.laundrybuoy.customer.adapter.price.SelectionInterface
import com.laundrybuoy.customer.databinding.FragmentDynamicItemBinding
import com.laundrybuoy.customer.model.price.InventoryListSegregated
import com.laundrybuoy.customer.model.price.InventoryModel
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DynamicItemFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentDynamicItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var inventoryItemAdapter: PriceItemAdapter
    private val orderViewModel by viewModels<OrderViewModel>()
    private var selectionInterface: SelectionInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initRv()
        arguments?.getParcelable<InventoryListSegregated.InventoryListSegregatedItem>(
            INVENTORY_ITEM
        )
            ?.let {
                setRecyclerData(it)
            }
        binding.searchView.setOnQueryTextListener(this)

    }



    private fun initObserver() {
        orderViewModel.selectedItemsList.observe(viewLifecycleOwner, Observer {
            selectionInterface?.newListIsHere(it)
        })
    }

    private fun initRv() {
        inventoryItemAdapter = PriceItemAdapter(object : PriceItemAdapter.OnClickInterface {

            override fun onPlusClicked(doc: InventoryModel.Data.Item, position: Int) {
                var currentQty = doc.selectedQty
                currentQty = currentQty?.plus(1)
                val latestSelectionList = inventoryItemAdapter.updateQty(position, currentQty)
                processSelectedList(latestSelectionList)
            }

            override fun onMinusClicked(doc: InventoryModel.Data.Item, position: Int) {
                var currentQty = doc.selectedQty
                if (currentQty != 0) {
                    currentQty = currentQty?.minus(1)
                    val latestSelectionList = inventoryItemAdapter.updateQty(position, currentQty)
                    processSelectedList(latestSelectionList)
                }
            }

        })
        binding.dynamicItemsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.dynamicItemsRv.adapter = inventoryItemAdapter
    }

    private fun processSelectedList(latestSelectionList: MutableList<InventoryModel.Data.Item>) {
        val nonZeroSelectedItems = latestSelectionList.filter {
            it.selectedQty != 0
        }.toMutableList()
        orderViewModel.pushSelectedBillingList(nonZeroSelectedItems)
    }

    private fun setRecyclerData(it: InventoryListSegregated.InventoryListSegregatedItem) {
        inventoryItemAdapter.setData(it.catList.toMutableList())
    }

    internal companion object {
        private const val INVENTORY_ITEM = "INVENTORY_ITEM"

        @JvmStatic
        fun newInstance(
            inventoryItem: InventoryListSegregated.InventoryListSegregatedItem,
            selectionInterface1: SelectionInterface
        ): DynamicItemFragment {
            val frg = DynamicItemFragment()
            val bundle = Bundle()
            bundle.putParcelable(INVENTORY_ITEM, inventoryItem)
            frg.arguments = bundle
            frg.selectionInterface = selectionInterface1
            return frg
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDynamicItemBinding.inflate(inflater, container, false)
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        inventoryItemAdapter.filter.filter(newText)
        return false
    }


}
package com.laundrybuoy.customer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.address.AddressBookAdapter
import com.laundrybuoy.customer.adapter.address.QuickAddressAdapter
import com.laundrybuoy.customer.databinding.FragmentAddressBookBinding
import com.laundrybuoy.customer.databinding.FragmentHomeBinding
import com.laundrybuoy.customer.model.address.GetUserAddressResponse
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressBookFragment : BaseFragment() {

    private var _binding: FragmentAddressBookBinding? = null
    private val binding get() = _binding!!
    private val orderVideModel by viewModels<OrderViewModel>()
    private lateinit var addressAdapter: AddressBookAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initObserver()
        getAddresses()
        onClick()

    }

    private fun getAddresses() {
        orderVideModel.getAddressBook()

    }

    private fun onClick() {
        binding.backFromAddressBookIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.addAddressBookBtn.setOnClickListener {
            val addressAddBtmSheet =
                AddressAddFragment.newInstance()
            addressAddBtmSheet.setCallback {
                getAddresses()
            }
            addressAddBtmSheet.isCancelable = false
            addressAddBtmSheet.show(childFragmentManager, "address_add")
        }
    }


    private fun setData(listOfAddress: List<GetUserAddressResponse.Data>) {
        if (listOfAddress.isEmpty()) {
            binding.addressBookRv.makeViewGone()
            binding.addressBookUnAvailableLl.makeViewVisible()
        } else {
            binding.addressBookRv.makeViewVisible()
            binding.addressBookUnAvailableLl.makeViewGone()
            addressAdapter.submitList(listOfAddress)

        }
    }

    private fun initObserver() {

        orderVideModel.defaultAddressLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data.message=="Address Updated") {
                        getAddresses()
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

        orderVideModel.deleteAddressLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data.message=="Address deleted") {
                        getAddresses()
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

        orderVideModel.addressLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        setData(it.data.data)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddressBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRv() {
        addressAdapter = AddressBookAdapter(object : AddressBookAdapter.AddressClickListeners {
            override fun onAddressEdit(address: GetUserAddressResponse.Data) {

            }

            override fun onAddressDelete(address: GetUserAddressResponse.Data) {
                showDeleteDialog(address)
            }

            override fun onAddressDefault(address: GetUserAddressResponse.Data) {
                val payload = JsonObject()
                payload.addProperty("addressId", address.id)
                orderVideModel.markAddressDefault(payload)

            }

        })
        binding.addressBookRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.addressBookRv.adapter = addressAdapter
    }

    private fun showDeleteDialog(address: GetUserAddressResponse.Data) {

        val infoFrag =
            GeneralInfoFragment.newInstance(
                getString(R.string.confirm_delete),
                getString(R.string.delete_address_confirmation),
                getString(R.string.delete2)
            )
        infoFrag.setCallback {
            val payload = JsonObject()
            payload.addProperty("addressId", address.id)
            orderVideModel.deleteAddress(payload)
        }
        infoFrag.isCancelable = true
        infoFrag.show(childFragmentManager, "delete_frag")

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
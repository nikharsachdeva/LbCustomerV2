package com.laundrybuoy.customer.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.adapter.address.UserAddressAdapter
import com.laundrybuoy.customer.databinding.FragmentAddressSelectBinding
import com.laundrybuoy.customer.model.address.GetUserAddressResponse
import com.laundrybuoy.customer.utils.Constants.SCREEN_TYPE
import com.laundrybuoy.customer.utils.Constants.SCREEN_TYPE_HOME
import com.laundrybuoy.customer.utils.Constants.SCREEN_TYPE_PROFILE
import com.laundrybuoy.customer.utils.Constants.SCREEN_TYPE_SCHEDULE
import com.laundrybuoy.customer.utils.makeViewGone
import com.laundrybuoy.customer.utils.makeViewVisible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressSelectFragment : BaseBottomSheet() {

    private var _binding: FragmentAddressSelectBinding? = null
    private val binding get() = _binding!!
    private lateinit var addressAdapter: UserAddressAdapter
    private var screenTypeReceived: String? = null

    private lateinit var callback: (GetUserAddressResponse.Data) -> Unit
    fun setCallback(callback: (GetUserAddressResponse.Data) -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initRv()
        onClicks()
        setData()
    }

    private fun initView() {
        if (isAdded) {
            screenTypeReceived = requireArguments().getString(SCREEN_TYPE)
        }
        if (screenTypeReceived == SCREEN_TYPE_PROFILE) {
            setBottomNav()
            binding.backFromAddressListIv.makeViewVisible()
            binding.closeAddressListIv.makeViewGone()
        } else if (screenTypeReceived == SCREEN_TYPE_HOME || screenTypeReceived == SCREEN_TYPE_SCHEDULE) {
            binding.backFromAddressListIv.makeViewGone()
            binding.closeAddressListIv.makeViewVisible()
        }
    }

    private fun setData() {
        var listOfAddress: MutableList<GetUserAddressResponse.Data> = arrayListOf()
        val addressObj1 = GetUserAddressResponse.Data(
            addressType = "Home",
            altMobile = 8899889980,
            createdAt = "",
            id = "",
            isDefault = false,
            updatedAt = "",
            userId = "",
            v = 1,
            address = GetUserAddressResponse.Data.Address(
                city = "Muradnagar",
                latitude = "",
                longitude = "",
                landmark = "SBI Bank",
                line1 = "New Defence Colony, Railway Road, Muradnagar",
                pin = "201206",
                state = "Uttar Pradesh"
            )

        )
        val addressObj2 = GetUserAddressResponse.Data(
            addressType = "Office",
            altMobile = 8899889981,
            createdAt = "",
            id = "",
            isDefault = false,
            updatedAt = "",
            userId = "",
            v = 1,
            address = GetUserAddressResponse.Data.Address(
                city = "Noida",
                latitude = "",
                longitude = "",
                landmark = "Sec 135",
                line1 = "DDC4A , Tower 3 , Floor 5",
                pin = "201267",
                state = "Uttar Pradesh"
            )

        )

        val addressObj3 = GetUserAddressResponse.Data(
            addressType = "Work",
            altMobile = 8899889982,
            createdAt = "",
            id = "",
            isDefault = false,
            updatedAt = "",
            userId = "",
            v = 1,
            address = GetUserAddressResponse.Data.Address(
                city = "Greater Noida",
                latitude = "",
                longitude = "",
                landmark = "Advant Tower",
                line1 = "Pind Balluchi, 5th Floor , 22031",
                pin = "201009",
                state = "Uttar Pradesh"
            )

        )

        val addressObj4 = GetUserAddressResponse.Data(
            addressType = "Flat",
            altMobile = 8899889983,
            createdAt = "",
            id = "",
            isDefault = false,
            updatedAt = "",
            userId = "",
            v = 1,
            address = GetUserAddressResponse.Data.Address(
                city = "Greater Noida",
                latitude = "",
                longitude = "",
                landmark = "Gaur City 2",
                line1 = "14th Avenue, GC2 , G Tower , 24050",
                pin = "201009",
                state = "Uttar Pradesh"
            )

        )
        listOfAddress.add(addressObj1)
        listOfAddress.add(addressObj2)
        listOfAddress.add(addressObj3)
        listOfAddress.add(addressObj4)

        setDataToAdapter(listOfAddress)
    }

    private fun setDataToAdapter(listOfAddress: MutableList<GetUserAddressResponse.Data>) {
        if (listOfAddress.isNullOrEmpty()) {
            binding.addressAvailableRv.makeViewGone()
            binding.addressUnAvailableLl.makeViewVisible()
        } else {
            binding.addressAvailableRv.makeViewVisible()
            binding.addressUnAvailableLl.makeViewGone()
            addressAdapter.submitList(listOfAddress)

        }
    }

    private fun onClicks() {
        binding.closeAddressListIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.backFromAddressListIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.addAddressBtn.setOnClickListener {
            val addressAddBtmSheet =
                AddressAddFragment.newInstance()
            addressAddBtmSheet.isCancelable = true
            addressAddBtmSheet.show(childFragmentManager, "address_add")
        }
    }

    private fun initRv() {
        addressAdapter = UserAddressAdapter(object : UserAddressAdapter.AddressClickListener {
            override fun onAddressSelected(address: GetUserAddressResponse.Data) {
                if (::callback.isInitialized) {

                    if (screenTypeReceived == SCREEN_TYPE_PROFILE) {
                        getMainActivity()?.onBackPressed()
                    } else if (screenTypeReceived == SCREEN_TYPE_HOME || screenTypeReceived == SCREEN_TYPE_SCHEDULE) {
                        dialog?.dismiss()
                    }

                    callback.invoke(address)
                }
            }

        })
        binding.addressAvailableRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.addressAvailableRv.adapter = addressAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddressSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        fun newInstance(screenType: String): AddressSelectFragment {
            val addFragment = AddressSelectFragment()
            val args = Bundle()
            args.putString(SCREEN_TYPE, screenType)
            addFragment.arguments = args
            return addFragment
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (screenTypeReceived == SCREEN_TYPE_PROFILE) {
                setBottomNav()
            }
        }
    }


    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
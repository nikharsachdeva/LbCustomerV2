package com.laundrybuoy.customer.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.address.AddressTypeAdapter
import com.laundrybuoy.customer.adapter.address.PlacesResultAdapter
import com.laundrybuoy.customer.adapter.address.UserAddressAdapter
import com.laundrybuoy.customer.databinding.FragmentAddressAddBinding
import com.laundrybuoy.customer.model.address.AddAddressPayload
import com.laundrybuoy.customer.model.address.AddressTypeModel
import com.laundrybuoy.customer.model.address.GetUserAddressResponse
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import com.laundrybuoy.customer.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressAddFragment : BaseBottomSheet(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentAddressAddBinding? = null
    private val binding get() = _binding!!
    var placesClient: PlacesClient? = null
    private lateinit var placeAdapter: PlacesResultAdapter
    private lateinit var mMap: GoogleMap
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val orderViewModel by viewModels<OrderViewModel>()
    private var isGPSEnabled = false
    private lateinit var addressTypeAdapter: AddressTypeAdapter
    var addAddressButtonState = MutableLiveData<Boolean>()

    private var currentState: String? = null
    private var currentPin: String? = null
    private var currentCity: String? = null

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomNav()
        initObserver()
        init()
        initRv()
        setDataToAddressTypeRv()
        onClick()

        binding.locationUnAvailableLl.makeViewVisible()
        binding.locationUnavHeadingTv.text = "Please search location to begin"
        binding.locationFieldsLl.makeViewGone()
        binding.locationSearchRv.makeViewGone()
    }

    private fun setDataToAddressTypeRv() {
        val listOfType: MutableList<AddressTypeModel> = arrayListOf()
        listOfType.add(
            AddressTypeModel(
                type = "Home",
                image = R.drawable.address_home
            )
        )

        listOfType.add(
            AddressTypeModel(
                type = "Office",
                image = R.drawable.address_office
            )
        )

        listOfType.add(
            AddressTypeModel(
                type = "Other",
                image = R.drawable.address_more
            )
        )
        addressTypeAdapter.submitList(listOfType)

    }

    private fun initObserver() {
        profileViewModel.selectedLatLngLiveData.observe(viewLifecycleOwner, Observer {
            var latLng = it

            try {
                getGeoAddress(latLng.latitude,
                    latLng.longitude,
                    object : GetAddrFromLatLog.AddressListener {
                        override fun onAddressFound(
                            address: String?,
                            state: String?,
                            pincode: String?,
                            city: String?,
                        ) {
                            (requireActivity()).runOnUiThread(Runnable {
                                var autoAddress1 =
                                    address?.substring(0, address.indexOf(","))?.trim()
                                var autoAddress2 =
                                    address?.substring(address.indexOf(",").plus(1), address.length)
                                        ?.trim()
                                binding.geocodeAddressLine1Tv.text = autoAddress1
                                binding.geocodeAddressLine2Tv.text = autoAddress2
                                currentState = state;
                                currentPin = pincode;
                                currentCity = city
                            })

                        }

                        override fun onError() {
                        }

                    })
            } catch (e: Exception) {
                getMainActivity()?.onBackPressed()
                if (e.localizedMessage.contains("DEADLINE")) {
                    getMainActivity()?.showSnackBar("Please make sure you have an active internet connection!")
                }
            }

        })

        addAddressButtonState.observe(viewLifecycleOwner, Observer { isEnabled ->
            if (isEnabled) {
                binding.addAddressBtn.makeButtonEnabled()
            } else {
                binding.addAddressBtn.makeButtonDisabled()
            }
        })

        orderViewModel.addAddressLiveData.observe(viewLifecycleOwner, Observer {
            binding.addAddressBtn.makeButtonDisabled()

            when (it) {
                is NetworkResult.Loading -> {
                    binding.addAddressBtn.makeButtonDisabled()

                }
                is NetworkResult.Success -> {
                    binding.addAddressBtn.makeButtonEnabled()
                    Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT)
                        .show()

                    if(it?.data?.success==true){
                        if(::callback.isInitialized){
                            dialog?.dismiss()
                            callback.invoke()
                        }
                    }
                    if (it.data?.success == true) {
                        Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                is NetworkResult.Error -> {
                    binding.addAddressBtn.makeButtonEnabled()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    companion object {

        fun newInstance(): AddressAddFragment {
            val addFragment = AddressAddFragment()
            val args = Bundle()
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


    private fun onClick() {

        binding.addAddressBtn.setOnClickListener {
            if (isValid()) {
                val currentType = addressTypeAdapter.currentList.find {
                    it.isSelected == true
                }?.type

                val addressPayload = AddAddressPayload(
                    altMobile = binding.mobileTiet.text.toString().toLong(),
                    addressType = currentType,
                    address = AddAddressPayload.Address(
                        latitude = profileViewModel.selectedLatLngLiveData.value?.latitude.toString(),
                        longitude = profileViewModel.selectedLatLngLiveData.value?.longitude.toString(),
                        state = currentState,
                        pin = currentPin,
                        city = currentCity,
                        line1 = binding.addressLine1Tiet.text.toString(),
                        landmark = binding.addressLine2Tiet.text.toString() + " " + binding.addressLine3Tiet.text.toString()
                    )

                )

                orderViewModel.addAddress(addressPayload)


            } else {
                Toast.makeText(requireContext(), "Address Incomplete", Toast.LENGTH_SHORT).show()
            }
        }


        binding.backFromAddAddressIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.locateMeBigLl.setOnClickListener {
            invokeLocationAction()
        }

        binding.locateMeSmallLl.setOnClickListener {
            invokeLocationAction()
        }
    }

    private fun isValid(): Boolean {
        var isValid: Boolean = true

        if (binding.addressLine1Tiet.text?.trim().toString().isEmpty()) {
            isValid = false
        }

        if (binding.addressLine2Tiet.text?.trim().toString().isEmpty()) {
            isValid = false
        }

        if (binding.mobileTiet.text?.trim().toString().length != 10) {
            isValid = false
        }

        val selectedLatLng = profileViewModel.selectedLatLngLiveData.value
        if (selectedLatLng?.latitude == null || selectedLatLng?.longitude == null) {
            isValid = false
        }

        val currentType = addressTypeAdapter.currentList.find {
            it.isSelected == true
        }
        if (currentType == null) {
            isValid = false
        }
        return isValid

    }

    private fun initRv() {
        placeAdapter = PlacesResultAdapter(requireContext()) { prediction, place ->
            binding.locationSearchEt.hideKeyboard()
            setUiData(prediction, place)
        }
        binding.locationSearchRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.locationSearchRv.adapter = placeAdapter

        addressTypeAdapter = AddressTypeAdapter(object : AddressTypeAdapter.TypeClickListener {
            override fun onTypeSelected(type: AddressTypeModel) {

            }

        })
        binding.addressTypeRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.addressTypeRv.adapter = addressTypeAdapter
    }

    private fun setUiData(prediction: AutocompletePrediction, place: Place) {
        binding.locationSearchRv.makeViewGone()
        binding.locationUnAvailableLl.makeViewGone()
        binding.locationFieldsLl.makeViewVisible()
        binding.locateMeBigLl.makeViewGone()
        binding.orAddressTv.makeViewGone()
//        binding.locationSearchEt.text.clear()

        val latlng = place.latLng
        if (latlng != null) {
            profileViewModel.setSelectedLatLng(LocationModel(latlng.longitude, latlng.latitude))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17f))
        }
    }

    fun getGeoAddress(lat: Double, lng: Double, callback: GetAddrFromLatLog.AddressListener?) {
        val th = HandlerThread("getAddr")
        th.start()

        val handler = Handler(th.getLooper())
        handler.post(GetAddrFromLatLog(requireContext(), lat, lng).apply {
            setAddressListener(callback)
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap() {
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        binding.addressMapMarker.makeViewVisible()

        mMap.setOnCameraMoveStartedListener { reason: Int ->
            binding.addressShimmerSl.makeViewVisible()
            binding.addressShimmerSl.startShimmer()
            binding.addressPinIconIv.makeViewInVisible()
            binding.geocodeAddressLine1Tv.makeViewInVisible()
            binding.geocodeAddressLine2Tv.makeViewInVisible()
            binding.locationFieldsLl.requestDisallowInterceptTouchEvent(true)
            when (reason) {
                GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE -> {
                    Log.d("midLatLng-->", "The user gestured on the map.")
                }
                GoogleMap.OnCameraMoveStartedListener
                    .REASON_API_ANIMATION,
                -> {
                    Log.d("midLatLng-->", "The user tapped something on the map.")
                }
                GoogleMap.OnCameraMoveStartedListener
                    .REASON_DEVELOPER_ANIMATION,
                -> {
                    Log.d("midLatLng-->", "The app moved the camera.")
                }
            }
        }

        mMap.setOnCameraIdleListener {
            Log.d("midLatLng-->", "Idle")
            binding.addressShimmerSl.makeViewGone()
            binding.addressShimmerSl.stopShimmer()
            binding.addressPinIconIv.makeViewVisible()
            binding.geocodeAddressLine1Tv.makeViewVisible()
            binding.geocodeAddressLine2Tv.makeViewVisible()
            binding.locationFieldsLl.requestDisallowInterceptTouchEvent(false)
            val midLatLng: LatLng =
                mMap.cameraPosition.target//map's center position latitude & longitude
            profileViewModel.setSelectedLatLng(
                LocationModel(
                    midLatLng.longitude,
                    midLatLng.latitude
                )
            )
        }


    }

    override fun onMarkerClick(p0: Marker) = false


    private fun init() {
        addAddressButtonState.value = false
        binding.addressLine1Tiet.addTextChangedListener(AddressTextWatcher(addAddressButtonState))
        binding.addressLine2Tiet.addTextChangedListener(AddressTextWatcher(addAddressButtonState))
        binding.mobileTiet.addTextChangedListener(AddressTextWatcher(addAddressButtonState))

        GpsUtils(requireContext()).turnGPSOn(object : GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                isGPSEnabled = isGPSEnable
            }
        })


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.addressMapFragment) as SupportMapFragment

        mapFragment.getMapAsync(this)


        val apiKey = getString(R.string.GMap_API_Key)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        placesClient = Places.createClient(requireContext())


        binding.locationSearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                if (text.toString().isNotEmpty()) {
                    binding.locationSearchRv.makeViewVisible()
                    binding.locationUnAvailableLl.makeViewGone()
                    binding.locationFieldsLl.makeViewGone()
                    placeAdapter!!.filter.filter(text.toString())

                    binding.orAddressTv.makeViewGone()
                    binding.locateMeRootRl.makeViewGone()
                    binding.locateMeBigLl.makeViewGone()

                } else {
                    binding.locationUnAvailableLl.makeViewVisible()
                    binding.locationUnavHeadingTv.text = "No location found"
                    binding.locationFieldsLl.makeViewGone()
                    binding.locationSearchRv.makeViewGone()

                    binding.orAddressTv.makeViewVisible()
                    binding.locateMeRootRl.makeViewVisible()
                    binding.locateMeBigLl.makeViewVisible()
                }
            }
        })

    }

    /**
     *Location
     */

    private fun startLocationUpdate() {
        profileViewModel.getLiveLocationData().observe(this, Observer {
            Log.d("lolo-->", "startLocationUpdate: " + it.toString())
            var latLng = LatLng(it.latitude, it.longitude)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

        })
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> {
                Toast.makeText(requireContext(), getString(R.string.enable_gps), Toast.LENGTH_SHORT)
                    .show()
            }

            isPermissionsGranted() -> {
                binding.locationSearchRv.makeViewGone()
                binding.locationUnAvailableLl.makeViewGone()
                binding.locationFieldsLl.makeViewVisible()
                binding.locateMeBigLl.makeViewGone()
                binding.orAddressTv.makeViewGone()
                startLocationUpdate()
            }

            shouldShowRequestPermissionRationale() -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.provide_permission),
                    Toast.LENGTH_SHORT
                ).show()
                Snackbar.make(
                    dialog?.window?.decorView!!,
                    getString(R.string.enable_perm),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(getString(R.string.settings)) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        requireContext().startActivity(intent)
                    }
                    .show()
            }

            else -> ActivityCompat.requestPermissions(
                (getMainActivity() as Activity),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
                invokeLocationAction()
            }
        }
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            (getMainActivity() as Activity),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            (getMainActivity() as Activity),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddressAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private inner class AddressTextWatcher(private val addAddressButtonState: MutableLiveData<Boolean>) :
        TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Check if all fields are non-empty
            var allFieldsFilled = true
            if (binding.addressLine1Tiet.text.toString().trim().isEmpty()) {
                allFieldsFilled = false
            }
            if (binding.addressLine2Tiet.text.toString().trim().isEmpty()) {
                allFieldsFilled = false
            }
            if (binding.mobileTiet.text.toString().trim().length != 10) {
                allFieldsFilled = false
            }

            // Update the submitButtonState LiveData with the new value
            addAddressButtonState.value = allFieldsFilled
        }

        override fun afterTextChanged(p0: Editable?) {

        }

        // Implement the other TextWatcher methods if needed
    }

}

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101
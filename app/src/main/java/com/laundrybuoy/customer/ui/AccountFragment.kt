package com.laundrybuoy.customer.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.snackbar.Snackbar
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.databinding.FragmentAccountBinding
import com.laundrybuoy.customer.db.UserStructure
import com.laundrybuoy.customer.model.auth.GetProfileResponse
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.utils.Constants.UPDATE_CONTACT
import com.laundrybuoy.customer.utils.Constants.UPDATE_EMAIL
import com.laundrybuoy.customer.utils.Constants.UPDATE_NAME
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import com.laundrybuoy.customer.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class AccountFragment : BaseFragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        onClick()
    }

    private fun onClick() {

        binding.userProfileCamCv.setOnClickListener {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

        binding.profileLogoutRl.setOnClickListener {
            getMainActivity()?.logout(true)

        }

        binding.userProfileNameUnavRl.setOnClickListener {
            val updateBtmSht = UpdateProfileFragment.newInstance(UPDATE_NAME, null)
            updateBtmSht.setCallback {
                fetchUserProfile()
            }
            updateBtmSht.isCancelable = false
            updateBtmSht.show(childFragmentManager, "update_profile_name")
        }

        binding.profileAltConRootRl.setOnClickListener {
            val contact = binding.profileSecNumValueTv.text.toString()
            val updateBtmSht = UpdateProfileFragment.newInstance(UPDATE_CONTACT, contact)
            updateBtmSht.setCallback {
                fetchUserProfile()
            }
            updateBtmSht.isCancelable = false
            updateBtmSht.show(childFragmentManager, "update_profile_contact")
        }

        binding.profileEmailRootRl.setOnClickListener {
            val email = binding.profileEmailValueTv.text.toString()
            val updateBtmSht = UpdateProfileFragment.newInstance(UPDATE_EMAIL, email)
            updateBtmSht.setCallback {
                fetchUserProfile()
            }
            updateBtmSht.isCancelable = false
            updateBtmSht.show(childFragmentManager, "update_profile_email")
        }

        binding.profileRewardRootRl.setOnClickListener {
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                ScratchCardFragment()
            )
        }

        binding.profileCoinRootRl.setOnClickListener {
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                MyCoinsFragment()
            )
        }

        binding.profileSubRootRl.setOnClickListener {
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                MySubscriptionFragment()
            )
        }

        binding.profileAddRootRl.setOnClickListener {

            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                AddressBookFragment()
            )

            /*
            val addressBtmSheet =
                AddressSelectFragment.newInstance(SCREEN_TYPE_PROFILE)
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                addressBtmSheet
            )

             */
        }

        binding.profilePackRootRl.setOnClickListener {

        }
    }

    private fun initObserver() {

        profileViewModel.uploadDocLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        fetchUserProfile()
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })


        authViewModel.userProfileLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data?.message == "Profile Data") {
                        saveUserToDb(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        authViewModel.loggedInUserLive?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setProfileUI(it)
            }
        })
    }

    private fun setProfileUI(data: UserStructure?) {
        if (data?.name.isNullOrEmpty()) {
            binding.userProfileNameUnavRl.makeViewVisible()
            binding.userProfileNameTv.makeViewGone()
        } else {
            binding.userProfileNameUnavRl.makeViewGone()
            binding.userProfileNameTv.makeViewVisible()
            binding.userProfileNameTv.text = data?.name
        }

        binding.userProfileJoinDateTv.text="Member Since : ${data?.createdAt?.getFormattedDate()}"
        if(!data?.email.isNullOrEmpty()){
            binding.profileEmailValueTv.text=data?.email
        }

        if(!data?.profile.isNullOrEmpty()){
            binding.userProfileIconIv.loadImageWithGlide(data?.profile?:"")
        }

        if(!data?.mobile.isNullOrEmpty()){
            binding.profilePriNumValueTv.text=data?.mobile
        }

        if(!data?.altMobile.isNullOrEmpty()){
            binding.profileSecNumValueTv.text=data?.altMobile
        }
    }

    private fun saveUserToDb(data: GetProfileResponse.Data?) {
        val userStructure = UserStructure(
            id = data?.profile?.id ?: "",
            coins = data?.profile?.coins,
            createdAt = data?.profile?.createdAt,
            email = data?.profile?.email,
            emailVerified = data?.profile?.emailVerified,
            isActive = data?.profile?.isActive,
            isBlocked = data?.profile?.isBlocked,
            mobile = data?.profile?.mobile,
            altMobile = data?.profile?.altMobile,
            name = data?.profile?.name,
            profile = data?.profile?.profile,
            role = data?.profile?.role,
            tagId = data?.profile?.tagId,
            updatedAt = data?.profile?.updatedAt,
            v = data?.profile?.v,
            jwtToken = data?.jwtToken,
            isPremium = data?.isPremium
        )
        authViewModel.saveUserToDb(userStructure)
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var grantedCount = 0;
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    ++grantedCount;
                } else {
                    showSnackbarToSetting()
                }

                if (grantedCount == 3) {
                    openImagePicker()
                }
            }
        }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
            uploadImage(uriContent!!, uriFilePath)
        } else {
            val exception = result.error
            getMainActivity()?.showSnackBar(exception?.localizedMessage.toString())
        }
    }

    private fun uploadImage(uriContent: Uri, uriFilePath: String?) {

        val tFile = File(uriFilePath)
        if (tFile != null) {
            val requestFile = tFile.asRequestBody(
                requireContext().contentResolver.getType(uriContent)?.toMediaTypeOrNull()
            )
            val body = MultipartBody.Part.createFormData("photoId", tFile.name, requestFile)
                profileViewModel.uploadDoc(body)
        } else {
            getMainActivity()?.showSnackBar("Empty File")
        }

    }

    private fun openImagePicker() {
        cropImage.launch(
            options {
                setImageSource(
                    includeGallery = true,
                    includeCamera = true,
                )
                // Normal Settings
                setScaleType(CropImageView.ScaleType.FIT_CENTER)
                setCropShape(CropImageView.CropShape.RECTANGLE)
                setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                setAspectRatio(1, 1)
                setMaxZoom(4)
                setAutoZoomEnabled(true)
                setMultiTouchEnabled(true)
                setCenterMoveEnabled(true)
                setShowCropOverlay(true)
                setAllowFlipping(true)
                setSnapRadius(3f)
                setTouchRadius(48f)
                setInitialCropWindowPaddingRatio(0.1f)
                setBorderLineThickness(3f)
                setBorderLineColor(Color.argb(170, 255, 255, 255))
                setBorderCornerThickness(2f)
                setBorderCornerOffset(5f)
                setBorderCornerLength(14f)
                setBorderCornerColor(Color.WHITE)
                setGuidelinesThickness(1f)
                setGuidelinesColor(R.color.white)
                setBackgroundColor(Color.argb(119, 0, 0, 0))
                setMinCropWindowSize(24, 24)
                setMinCropResultSize(20, 20)
                setMaxCropResultSize(99999, 99999)
                setActivityTitle("")
                setActivityMenuIconColor(0)
                setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                setOutputCompressQuality(90)
                setRequestedSize(0, 0)
                setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                setInitialCropWindowRectangle(null)
                setInitialRotation(0)
                setAllowCounterRotation(false)
                setFlipHorizontally(false)
                setFlipVertically(false)
                setCropMenuCropButtonTitle(null)
                setCropMenuCropButtonIcon(0)
                setAllowRotation(true)
                setNoOutputImage(false)
                setFixAspectRatio(false)

            }
        )
    }


    private fun showSnackbarToSetting() {

        Snackbar.make(
            binding.accountRootSv,
            "Permission blocked",
            Snackbar.LENGTH_LONG
        ).setAction("Settings") {
            // Responds to click on the action
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri =
                Uri.fromParts("package", getMainActivity()?.applicationContext?.packageName, null)
            intent.data = uri
            startActivity(intent)
        }.show()

    }


    private fun fetchUserProfile() {
        authViewModel.getUserProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setBottomNav() {
        fetchUserProfile()
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
package com.laundrybuoy.customer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.databinding.FragmentOtpBinding
import com.laundrybuoy.customer.db.UserStructure
import com.laundrybuoy.customer.model.auth.GetProfileResponse
import com.laundrybuoy.customer.model.auth.VerifyOtpResponse
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class OtpFragment : BaseBottomSheet() {

    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    val compositeDisposable = CompositeDisposable()
    private var numberReceived: String? = null
    private var otpReceived: String? = null
    private var otpIdReceived: String? = null
    private val authViewModel by viewModels<AuthViewModel>()

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val MOBILE_NUMBER = "MOBILE_NUMBER"
        private const val OTP_ID = "OTP_ID"
        private const val OTP = "OTP"

        fun newInstance(
            mobileNumber: String,
            otpId: String,
            otp: String,
        ): OtpFragment {
            val otpFragment = OtpFragment()
            val args = Bundle()
            args.putString(MOBILE_NUMBER, mobileNumber)
            args.putString(OTP_ID, otpId)
            args.putString(OTP, otp)
            otpFragment.arguments = args
            return otpFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        onClick()
        resendOTP()

    }

    private fun resendOTP() {
        compositeDisposable.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .take(30)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    binding.resendText.isClickable = true
                    binding.resendText.text = " Resend OTP"
                }
                .subscribe {
                    binding.resendText.text = " Resend in ${30 - it}s"
                    binding.resendText.isClickable = false
                })
    }


    private fun init() {
        numberReceived = arguments?.getString(MOBILE_NUMBER)
        otpReceived = arguments?.getString(OTP)
        otpIdReceived = arguments?.getString(OTP_ID)

        setUI()
    }

    private fun setUI() {
        binding.otpSentToTv.text = "OTP has been sent to $numberReceived"

        binding.otpNumberEt.setText(otpReceived.toString())
    }

    private fun onClick() {
        binding.resendText.setOnClickListener {
            if (::callback.isInitialized) {
                dialog?.dismiss()
                callback.invoke()
            }
        }

        binding.closeOtpFragIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.submitOtpBtn.setOnClickListener {
            verifyOtp()
        }
    }

    private fun verifyOtp() {
        val payload = JsonObject()
        payload.addProperty("_id", otpIdReceived)
        payload.addProperty("otp", otpReceived)
        authViewModel.verifyOtp(payload)
    }

    private fun initObserver() {
        compositeDisposable.add(
            RxTextView.textChanges(binding.otpNumberEt)
                .subscribe {
                    if (it.trim().length == 6) {
                        binding.otpNumberEt.hideKeyboard()
                        binding.submitOtpBtn.makeButtonEnabled()
                    } else {
                        binding.submitOtpBtn.makeButtonDisabled()
                    }
                })

        compositeDisposable.add(
            RxTextView.textChanges(binding.otpNumberEt)
                .subscribe {
                    if (it.trim().length == 6) {
                        getMainActivity()?.hideKeyboard()
                        binding.submitOtpBtn.makeButtonEnabled()
                    } else {
                        binding.submitOtpBtn.makeButtonDisabled()
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


        authViewModel.verifyOtpLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {
                    binding.submitOtpBtn.makeButtonDisabled()
                }
                is NetworkResult.Success -> {
                    binding.submitOtpBtn.makeButtonEnabled()
                    if (it.data?.success == true && !it.data.data?.token.isNullOrEmpty()) {
                        if (it.data?.data?.isBlocked == true) {
                            redirectToInfo(
                                getString(R.string.ac_block_heading),
                                getString(R.string.ac_block_subheading),
                                getString(R.string.support)
                            )
                        } else {
                            SharedPreferenceManager.setBearerToken(it?.data?.data?.token ?: "")
                            fetchUserProfile()
                        }
                    } else {
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                is NetworkResult.Error -> {
                    binding.submitOtpBtn.makeButtonEnabled()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun fetchUserProfile() {
        authViewModel.getUserProfile()
    }


    private fun goToHome() {
        getMainActivity()?.addFragment(
            false,
            getMainActivity()?.getVisibleFrame()!!,
            HomeFragment()
        )
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

        goToHome()

    }

    private fun redirectToInfo(heading: String, subheading: String, buttonText: String) {
        val infoFrag =
            GeneralInfoFragment.newInstance(
                heading,
                subheading,
                buttonText
            )
        infoFrag.setCallback {
            if (requireContext().isPackageInstalled("com.whatsapp")) {
                val message =
                    "Hello, I'm having trouble logging in with my Lb account." // replace with the message you want to send
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/?text=${Uri.encode(message)}")
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Whatsapp not installed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        infoFrag.isCancelable = false
        infoFrag.show(childFragmentManager, "info_frag")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
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
        compositeDisposable.dispose()

    }


}
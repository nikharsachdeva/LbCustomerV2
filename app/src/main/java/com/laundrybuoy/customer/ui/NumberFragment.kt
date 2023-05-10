package com.laundrybuoy.customer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.laundrybuoy.customer.databinding.FragmentNumberBinding
import com.laundrybuoy.customer.model.auth.SendOtpResponse
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class NumberFragment : BaseBottomSheet() {

    private var _binding: FragmentNumberBinding? = null
    private val binding get() = _binding!!
    val compositeDisposable = CompositeDisposable()
    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        onClick()

    }

    private fun onClick() {
        binding.submitLoginBtn.setOnClickListener {
            triggerOtp()
        }

        binding.closeNumberFragIv.setOnClickListener {
            dialog?.dismiss()
        }
    }

    fun triggerOtp() {
        val payload = JsonObject()
        payload.addProperty("mobile", binding.loginNumberEt.text.toString())
        authViewModel.sendOtp(payload)
    }

    private fun initObserver() {

        authViewModel.sendOtpLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {
                    binding.submitLoginBtn.makeButtonDisabled()
                }
                is NetworkResult.Success -> {
                    binding.submitLoginBtn.makeButtonEnabled()
                    if (it.data?.success == true && !it.data.data?.id.isNullOrEmpty()) {
                        if (it.data?.data?.isBlocked == true) {
                            redirectToInfo(
                                getString(R.string.ac_block_heading),
                                getString(R.string.ac_block_subheading),
                                getString(R.string.support)
                            )
                        } else {
                            redirectToOtp(it.data.data)
                        }
                    } else {
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                is NetworkResult.Error -> {
                    binding.submitLoginBtn.makeButtonEnabled()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        compositeDisposable.add(
            RxTextView.textChanges(binding.loginNumberEt)
                .subscribe {
                    if (it.trim().length == 10) {
                        binding.loginNumberEt.hideKeyboard()
                        binding.submitLoginBtn.makeButtonEnabled()
                    } else {
                        binding.submitLoginBtn.makeButtonDisabled()
                    }
                })
    }

    private fun redirectToInfo(heading: String, subheading: String, buttonText : String) {
        val infoFrag =
            GeneralInfoFragment.newInstance(
                heading,
                subheading,
                buttonText
            )
        infoFrag.setCallback {
            if (requireContext().isPackageInstalled("com.whatsapp")) {
                val message = "Hello, I'm having trouble logging in with my Lb account." // replace with the message you want to send
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/?text=${Uri.encode(message)}")
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Whatsapp not installed", Toast.LENGTH_SHORT).show()
            }
        }
        infoFrag.isCancelable = false
        infoFrag.show(childFragmentManager, "info_frag")
    }

    private fun redirectToOtp(data: SendOtpResponse.Data?) {

        val otpFrag =
            OtpFragment.newInstance(
                binding.loginNumberEt.text.toString(),
                data?.id ?: "",
                data?.otp ?: ""
            )
        otpFrag.setCallback {
            triggerOtp()
        }
        otpFrag.isCancelable = false
        otpFrag.show(childFragmentManager, "otp_frag")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNumberBinding.inflate(inflater, container, false)
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
    }


}
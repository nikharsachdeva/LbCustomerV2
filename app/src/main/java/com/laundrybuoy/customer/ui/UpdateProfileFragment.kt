package com.laundrybuoy.customer.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.databinding.FragmentUpdateProfileBinding
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.utils.Constants.RC_SIGN_IN
import com.laundrybuoy.customer.utils.Constants.SCREEN_TYPE
import com.laundrybuoy.customer.utils.Constants.UPDATE_CONTACT
import com.laundrybuoy.customer.utils.Constants.UPDATE_EMAIL
import com.laundrybuoy.customer.utils.Constants.UPDATE_NAME
import com.laundrybuoy.customer.utils.Constants.UPDATE_VALUE
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class UpdateProfileFragment : BaseBottomSheet() {

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!
    val compositeDisposable = CompositeDisposable()
    private val authViewModel by viewModels<AuthViewModel>()


    //GSignIn
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            val screenType = arguments?.getString(SCREEN_TYPE)
            val screenValue = arguments?.getString(UPDATE_VALUE)

            if (screenType == UPDATE_EMAIL) {
                binding.updateEmailRootRl.makeViewVisible()
                binding.updateContactRootRl.makeViewGone()
                binding.updateNameRootRl.makeViewGone()
            } else if (screenType == UPDATE_CONTACT) {
                binding.updateEmailRootRl.makeViewGone()
                binding.updateNameRootRl.makeViewGone()
                binding.updateContactRootRl.makeViewVisible()
                if (!screenValue.isNullOrEmpty()) {
                    binding.updateContactValueEt.setText(screenValue)
                }
            } else if (screenType == UPDATE_NAME) {
                binding.updateEmailRootRl.makeViewGone()
                binding.updateNameRootRl.makeViewVisible()
                binding.updateContactRootRl.makeViewGone()
            }

            initObserver()
            initGoogle()
            onClick()
        }
    }

    private fun initObserver() {
        compositeDisposable.add(
            RxTextView.textChanges(binding.updateContactValueEt)
                .subscribe {
                    if (it.trim().length == 10) {
                        getMainActivity()?.hideKeyboard()
                        binding.updateContactSubmitBtn.makeButtonEnabled()
                    } else {
                        binding.updateContactSubmitBtn.makeButtonDisabled()
                    }
                })

        compositeDisposable.add(
            RxTextView.textChanges(binding.updateNameValueEt)
                .subscribe {
                    if (!it.trim().isNullOrEmpty()) {
                        binding.updateNameSubmitBtn.makeButtonEnabled()
                    } else {
                        binding.updateNameSubmitBtn.makeButtonDisabled()
                    }
                })

        authViewModel.updateUserLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data.message?.contains("successful") == true) {
                        if (::callback.isInitialized) {
                            dialog?.dismiss()
                            callback.invoke()
                        }
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

        authViewModel.userProfileLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && !it.data.data?.profile?.id.isNullOrEmpty()) {

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

    private fun fetchUserProfile() {
        authViewModel.getUserProfile()
    }

    private fun onClick() {
        binding.gmailUnAvailableRl.setOnClickListener {
            showSignInIntent()
        }

        binding.disconnectGmailIv.setOnClickListener {
            signOutGoogle()

        }

        binding.closeUpdateIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.updateNameSubmitBtn.setOnClickListener {
            val payload = JsonObject()
            payload.addProperty("name", binding.updateNameValueEt.text.toString())
            authViewModel.updateUser(payload)
        }

        binding.updateContactSubmitBtn.setOnClickListener {
            val payload = JsonObject()
            payload.addProperty("altMobile", binding.updateContactValueEt.text.toString())
            authViewModel.updateUser(payload)
        }
    }

    private fun signOutGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(),
            OnCompleteListener<Void?> {

                getMainActivity()?.showSnackBar("Google Sign Out Success.")
                checkLoggedInAccounts()

            })
    }

    private fun showSignInIntent() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            handleGoogleData(account)
            Toast.makeText(requireContext(), "Google Sign In Successful.", Toast.LENGTH_SHORT)
                .show()
        } catch (e: ApiException) {
            Toast.makeText(
                requireContext(),
                "Failed to do Sign In :" + e.statusCode,
                Toast.LENGTH_SHORT
            ).show()
            handleGoogleData(null)
        }
    }

    private fun handleGoogleData(acct: GoogleSignInAccount?) {
        //if account is not null fetch the information
        if (acct != null) {
            val idToken = acct.idToken

            val payload = JsonObject()
            payload.addProperty("email", acct.email.toString())
            authViewModel.updateUser(payload)

            checkLoggedInAccounts()
        } else {
            Toast.makeText(requireContext(), "Couldn't Fetch Account Info", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun checkLoggedInAccounts() {

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            binding.gmailUnAvailableRl.makeViewGone()
            binding.gmailAvailableRl.makeViewVisible()
            binding.gmailAvailableEt.setText(account.email.toString())
        } else {
            binding.gmailUnAvailableRl.makeViewVisible()
            binding.gmailAvailableRl.makeViewGone()
        }
    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.OAuth_SSO))
            .requestEmail() //request email id
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    companion object {

        fun newInstance(screenType: String, updateValue: String?): UpdateProfileFragment {
            val addFragment = UpdateProfileFragment()
            val args = Bundle()
            args.putString(Constants.SCREEN_TYPE, screenType)
            args.putString(Constants.UPDATE_VALUE, updateValue)
            addFragment.arguments = args
            return addFragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        compositeDisposable.dispose()

    }

    override fun onStart() {
        super.onStart()
        checkLoggedInAccounts()
    }


}
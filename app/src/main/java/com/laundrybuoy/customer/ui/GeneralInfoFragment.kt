package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.databinding.FragmentGeneralInfoBinding

class GeneralInfoFragment : BaseBottomSheet() {

    private var _binding: FragmentGeneralInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        onClick()
    }

    private fun init() {

        if (isAdded) {
            val heading = requireArguments().getString(HEADING) ?: ""
            val subHeading = requireArguments().getString(SUB_HEADING) ?: ""
            val buttonText = requireArguments().getString(BUTTON_TEXT) ?: ""

            binding.infoHeadingTv.text=heading
            binding.infoSubHeadingTv.text=subHeading
            binding.infoOkayBtn.text=buttonText
        }

    }

    private fun onClick() {
        binding.closeInfoFragIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.infoOkayBtn.setOnClickListener {
            if(::callback.isInitialized){
                dialog?.dismiss()
                callback.invoke()
            }
        }


    }


    companion object {
        private const val HEADING = "HEADING"
        private const val SUB_HEADING = "SUB_HEADING"
        private const val BUTTON_TEXT = "BUTTON_TEXT"

        fun newInstance(
            heading: String,
            subHeading : String,
            buttonText : String
        ): GeneralInfoFragment {
            val infoFragment = GeneralInfoFragment()
            val args = Bundle()
            args.putString(HEADING, heading)
            args.putString(SUB_HEADING, subHeading)
            args.putString(BUTTON_TEXT, buttonText)
            infoFragment.arguments = args
            return infoFragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGeneralInfoBinding.inflate(inflater, container, false)
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
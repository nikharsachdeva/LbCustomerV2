package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var fadeInOut: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateTagLine(600)
        onClick()

    }

    private fun onClick() {
        binding.getStartedRl.setOnClickListener {
            val numberFrag =
                NumberFragment()
            numberFrag.isCancelable = true
            numberFrag.show(childFragmentManager, "number_frag")
        }
    }

    private fun animateTagLine(duration: Int) {

        var index = 0
        val bannerText = mutableListOf<String>()
        for (i in 1..duration) {
            if (i % 2 == 0) {
                bannerText.add("Happy Pockets")
            } else {
                bannerText.add("Happy Clothes")
            }
        }

        fadeInOut = AlphaAnimation(0.0f, 1.0f)
        fadeInOut.duration = 1400
        fadeInOut.startOffset = 100
        fadeInOut.repeatCount = (bannerText.size - 1)
        fadeInOut.repeatMode = Animation.REVERSE

        fadeInOut.setAnimationListener(object : Animation.AnimationListener {
            var isReversing = false

            override fun onAnimationStart(animation: Animation?) {
                binding.fadeInOutTv.text = bannerText[index]
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.fadeInOutTv.text = ""
            }

            override fun onAnimationRepeat(animation: Animation?) {
                isReversing = !isReversing
                if (!isReversing) {
                    index += 1
                    binding.fadeInOutTv.text = bannerText[index]
                }
            }
        })

        binding.fadeInOutTv.startAnimation(fadeInOut)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
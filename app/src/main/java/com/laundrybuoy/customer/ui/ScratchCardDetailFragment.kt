package com.laundrybuoy.customer.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.anupkumarpanwar.scratchview.ScratchView
import com.google.gson.JsonObject
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.databinding.FragmentScratchCardDetailBinding
import com.laundrybuoy.customer.model.scratch.ScratchCardModel
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.getFormattedDateWithTime
import com.laundrybuoy.customer.utils.makeViewInVisible
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScratchCardDetailFragment : BaseFragment() {

    private var _binding: FragmentScratchCardDetailBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private var scratchCardReceived: ScratchCardModel.ScratchCardModelItem? = null
    private val profileViewModel by viewModels<ProfileViewModel>()

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {

        fun newInstance(): ScratchCardDetailFragment {
            val scratchDetailFragment = ScratchCardDetailFragment()
            val args = Bundle()
            scratchDetailFragment.arguments = args
            return scratchDetailFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        initMediaPlayer()
        initScratchCard()
        onClick()

    }

    private fun initObserver() {

        profileViewModel.availScratchLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(context, R.raw.confetti_sound);
        mediaPlayer?.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(p0: MediaPlayer?) {
                mediaPlayer?.reset();
                mediaPlayer?.release();
                mediaPlayer = null;
            }
        })
    }

    private fun initScratchCard() {
        binding.scratchViewDetailSv.setRevealListener(object : ScratchView.IRevealListener {

            override fun onRevealed(scratchView: ScratchView?) {
                val payload = JsonObject()
                payload.addProperty("scratchId",profileViewModel.selectedScratchCardLiveData.value?.id?:"")
                profileViewModel.availScratchCard(payload)
                binding.itemScratchHeadingTv.text = getString(R.string.congratulations)
                binding.itemScratchSubHeadingTv.text = getString(R.string.utilize_points)
                binding.confettiLottieLv.playAnimation()
                binding.scratchCardMainRl.makeViewVisible()
                binding.scratchCardValueTv.text=profileViewModel.selectedScratchCardLiveData.value?.coins?.toString()
                binding.coinZoomLottieLv.playAnimation()
                mediaPlayer?.start()
                if (::callback.isInitialized) {
                    callback.invoke()
                }
            }

            override fun onRevealPercentChangedListener(scratchView: ScratchView?, percent: Float) {
                if ((percent * 100) > 5.0) {
                    binding.scratchViewDetailSv.reveal()
                }
            }

        })
    }

    private fun onClick() {
        binding.closeScratchDetailIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    private fun init() {
        binding.itemScratchRootCv.transitionName = arguments?.getString("TRANSITION_NAME")
        if(isAdded){
            scratchCardReceived = arguments?.getParcelable("SCRATCH_ITEM")
            profileViewModel.setSelectedScratchCard(scratchCardReceived)
        }

        if(scratchCardReceived?.isRevealed==true){
            binding.itemScratchHeadingTv.text = scratchCardReceived?.date?.getFormattedDateWithTime()
            binding.itemScratchSubHeadingTv.text = scratchCardReceived?.desc?.toString()
            binding.scratchCardMainRl.makeViewVisible()
            binding.scratchViewDetailSv.makeViewInVisible()
            binding.scratchCardValueTv.text=scratchCardReceived?.coins?.toString()
            binding.coinZoomLottieLv.playAnimation()
        }else{
            binding.itemScratchHeadingTv.text = getString(R.string.scratch_card)
            binding.itemScratchSubHeadingTv.text = getString(R.string.could_earn)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentScratchCardDetailBinding.inflate(inflater, container, false)
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
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

}
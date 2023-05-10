package com.laundrybuoy.customer.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.laundrybuoy.customer.BaseBottomSheet
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.price.SelectionInterface
import com.laundrybuoy.customer.adapter.rating.OrderEmojiAdapter
import com.laundrybuoy.customer.adapter.rating.OrderRatingBadgeAdapter
import com.laundrybuoy.customer.adapter.rating.RatingResultListener
import com.laundrybuoy.customer.databinding.FragmentFeedbackDynamicBinding
import com.laundrybuoy.customer.model.order.CustomerOrdersModel
import com.laundrybuoy.customer.model.price.InventoryListSegregated
import com.laundrybuoy.customer.model.rating.BadgeList
import com.laundrybuoy.customer.model.rating.EmojiList
import com.laundrybuoy.customer.model.rating.GiveRatingPayload
import com.laundrybuoy.customer.utils.Constants.SCREEN_FEEDBACK_DELIVERY
import com.laundrybuoy.customer.utils.Constants.SCREEN_FEEDBACK_PARTNER
import com.laundrybuoy.customer.utils.Constants.SCREEN_FEEDBACK_PICKUP
import com.laundrybuoy.customer.utils.Constants.SCREEN_TYPE
import com.laundrybuoy.customer.utils.NetworkResult
import com.laundrybuoy.customer.utils.makeButtonEnabled
import com.laundrybuoy.customer.utils.makeViewVisible
import com.laundrybuoy.customer.viewmodel.AuthViewModel
import com.laundrybuoy.customer.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackDynamicFragment : BaseBottomSheet() {

    private var _binding: FragmentFeedbackDynamicBinding? = null
    private val binding get() = _binding!!
    private lateinit var badgeAdapter: OrderRatingBadgeAdapter
    private lateinit var emojiAdapter: OrderEmojiAdapter
    private lateinit var ratingObjReceived: CustomerOrdersModel.Data.Partner.Rating
    private lateinit var ratingResultListener: RatingResultListener
    private val orderViewModel by viewModels<OrderViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundleData()
        initObserver()
        initRv()
        setBasicUI()
        onClick()
        setDataToRv()
    }

    private fun initObserver() {
        orderViewModel.giveRatingLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        ratingResultListener.onRatingSuccess()
                    } else {
                        ratingResultListener.onRatingError()
                    }
                }

                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })
    }

    private fun getBundleData() {
        ratingObjReceived =
            arguments?.getParcelable<CustomerOrdersModel.Data.Partner.Rating>("ratingsObj")!!
    }

    private fun setDataToRv() {
        badgeAdapter.submitList(getBadgeList())
        emojiAdapter.submitList(getEmojiList())

    }

    internal companion object {

        @JvmStatic
        fun newInstance(
            ratingListener: RatingResultListener,
        ): FeedbackDynamicFragment {
            val frg = FeedbackDynamicFragment()
            frg.ratingResultListener = ratingListener
            return frg
        }
    }

    private fun setBasicUI() {
        val screenType = ratingObjReceived.ratingFor
        if (screenType != null) {
            when (screenType) {

                "deliveryRider" -> {
                    binding.rateServiceHeadingTv.text = getString(R.string.rate_delivery)
                }

                "pickupRider" -> {
                    binding.rateServiceHeadingTv.text = getString(R.string.rate_pickup)
                }

                "partner" -> {
                    binding.rateServiceHeadingTv.text = getString(R.string.rate_partner)
                }
            }
        }
    }

    private fun onClick() {
        binding.submitFeedbackBtn.setOnClickListener {

            generatePayload()
        }

        binding.closeFeedbackIv.setOnClickListener {
            ratingResultListener.onRatingClosed()
        }
    }

    private fun generatePayload() {
        val selectedEmoji = emojiAdapter.currentList.find {
            it.isSelected == true
        }

        val selectedBadges = badgeAdapter.currentList.filter {
            it.isAwarded == true
        }.map {
            it.badgeId?.id
        }
        val rating = selectedEmoji?.value ?: 0
        val desc = binding.feedbackDescTv.text.trim().toString()
        val reviewId = ratingObjReceived.id

        val reviewPayload = GiveRatingPayload(
            badges = selectedBadges,
            reviewId = reviewId,
            rating = rating,
            desc = desc
        )

        orderViewModel.giveRating(reviewPayload)
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

    private fun getEmojiList(): MutableList<EmojiList.EmojiListItem> {
        val listOfEmoji: MutableList<EmojiList.EmojiListItem> = arrayListOf()

        listOfEmoji.add(
            EmojiList.EmojiListItem(
                R.drawable.rating_1,
                1
            )
        )

        listOfEmoji.add(
            EmojiList.EmojiListItem(
                R.drawable.rating_2,
                2
            )
        )

        listOfEmoji.add(
            EmojiList.EmojiListItem(
                R.drawable.rating_3,
                3
            )
        )

        listOfEmoji.add(
            EmojiList.EmojiListItem(
                R.drawable.rating_4,
                4
            )
        )

        listOfEmoji.add(
            EmojiList.EmojiListItem(
                R.drawable.rating_5,
                5
            )
        )

        return listOfEmoji
    }


    private fun getBadgeList(): MutableList<CustomerOrdersModel.Data.Partner.Rating.Badge> {

        return if (!ratingObjReceived.badges.isNullOrEmpty()) {
            ratingObjReceived.badges
        } else {
            arrayListOf()
        }
    }

    private fun initRv() {
        badgeAdapter = OrderRatingBadgeAdapter()
        binding.badgesRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.badgesRv.adapter = badgeAdapter

        emojiAdapter = OrderEmojiAdapter(object : OrderEmojiAdapter.EmojiClickListener {
            override fun onEmojiClicked(emoji: EmojiList.EmojiListItem) {
                reflectChangesInUi(emoji)
            }

        })
        val layoutManager = FlexboxLayoutManager(context).apply {
            justifyContent = JustifyContent.CENTER
            alignItems = AlignItems.CENTER
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }

        binding.emojiRv.layoutManager = layoutManager
        binding.emojiRv.adapter = emojiAdapter
    }

    private fun reflectChangesInUi(emoji: EmojiList.EmojiListItem) {

        binding.feedbackExtraLl.makeViewVisible()
        binding.submitFeedbackBtn.makeButtonEnabled()
        when (emoji.value) {
            1, 2 -> {
                binding.whatWentWrongTv.text = getString(R.string.what_went_wrong)
            }

            3 -> {
                binding.whatWentWrongTv.text = getString(R.string.temm_us_more)
            }

            4, 5 -> {
                binding.whatWentWrongTv.text = getString(R.string.what_you_liked)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFeedbackDynamicBinding.inflate(inflater, container, false)
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
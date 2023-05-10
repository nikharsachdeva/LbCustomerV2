package com.laundrybuoy.customer.ui

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.adapter.PagingLoaderAdapter
import com.laundrybuoy.customer.adapter.scratch.CustomerScratchPagingAdapter
import com.laundrybuoy.customer.databinding.FragmentScratchCardBinding
import com.laundrybuoy.customer.model.scratch.ScratchCardModel
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.utils.*
import com.laundrybuoy.customer.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScratchCardFragment : BaseFragment() {


    private var _binding: FragmentScratchCardBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    lateinit var customerScratchListAdapter: CustomerScratchPagingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setBottomNav()
        initScratchRv()
        fetchUserScratchCard()
    }

    private fun fetchUserScratchCard() {
        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.getCustomerScratchCards().observe(viewLifecycleOwner) {
                customerScratchListAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun init() {
        binding.backFromScratchIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(20)
        .build()

    private fun initScratchRv() {

        customerScratchListAdapter =
            CustomerScratchPagingAdapter(object : CustomerScratchPagingAdapter.OnClickInterface {
                override fun onScratchClicked(
                    scratchCard: CustomerTransactionModel.Data.Partner,
                    itemScratchRootCv: CardView,
                    absolutePos: Int,
                ) {

                    val scratchCardObj = ScratchCardModel.ScratchCardModelItem(
                        scratchCard.coins,
                        scratchCard.createdAt,
                        scratchCard.desc,
                        scratchCard.id,
                        scratchCard.isOpened
                    )
                    val scratchDetailFrag = ScratchCardDetailFragment.newInstance()
                    scratchDetailFrag.setCallback {
                        /*
                        val item = customerScratchListAdapter.getItemAtPosition(absolutePos)
                            ?.copy(isOpened = true)
                        item?.let { modifiedItem ->
                            val currentList = customerScratchListAdapter.snapshot().toMutableList()
                            currentList[absolutePos] = modifiedItem
                            customerScratchListAdapter.submitListToAdapter(currentList)
                        }
                         */
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        scratchDetailFrag.sharedElementEnterTransition = DetailsTransition()
                        scratchDetailFrag.enterTransition = Fade()
                        exitTransition = Fade()
                        scratchDetailFrag.sharedElementReturnTransition = DetailsTransition()
                    }
                    val arguments = Bundle()
                    arguments.putString("TRANSITION_NAME", itemScratchRootCv.transitionName)
                    arguments.putParcelable("SCRATCH_ITEM", scratchCardObj)
                    scratchDetailFrag.arguments = arguments
                    getMainActivity()?.addFragmentTransition(
                        true,
                        getMainActivity()?.getVisibleFrame()!!,
                        scratchDetailFrag,
                        itemScratchRootCv,
                        itemScratchRootCv.transitionName
                    )

                }


            })

        binding.userScratchCardsRv.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.userScratchCardsRv.setHasFixedSize(true)
        binding.userScratchCardsRv.addItemDecoration(
            GridItemOffsetDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.dimen10dp)
            )
        )
        binding.userScratchCardsRv.adapter =
            customerScratchListAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoaderAdapter(),
                footer = PagingLoaderAdapter()
            )
        customerScratchListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.Loading) {
                showShimmer()
            } else if (loadState.source.refresh is LoadState.NotLoading) {
                hideShimmer()
                if (loadState.append.endOfPaginationReached && customerScratchListAdapter.itemCount < 1) {
                    binding.scratchUnAvailableLl.makeViewVisible()
                    binding.userScratchCardsRv.makeViewGone()
                } else {
                    binding.scratchUnAvailableLl.makeViewGone()
                    binding.userScratchCardsRv.makeViewVisible()
                    setUI()
                }
            }
        }

        /*
        scratchCardAdapter =
            ScratchCardAdapter(object : ScratchCardAdapter.ScratchCardClickListener {
                override fun onScratchClicked(
                    scratchCard: ScratchCardModel.ScratchCardModelItem,
                    itemScratchRootCv: CardView,
                    pos: Int,
                ) {
                    val scratchDetailFrag = ScratchCardDetailFragment.newInstance()
                    scratchDetailFrag.setCallback {
                        scratchCardAdapter.currentList[pos].isRevealed = true
                        scratchCardAdapter.notifyItemChanged(pos)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        scratchDetailFrag.sharedElementEnterTransition = DetailsTransition()
                        scratchDetailFrag.enterTransition = Fade()
                        exitTransition = Fade()
                        scratchDetailFrag.sharedElementReturnTransition = DetailsTransition()
                    }
                    val arguments = Bundle()
                    arguments.putString("TRANSITION_NAME", itemScratchRootCv.transitionName)
                    arguments.putParcelable("SCRATCH_ITEM",scratchCard)
                    scratchDetailFrag.arguments = arguments
                    getMainActivity()?.addFragmentTransition(true,
                        getMainActivity()?.getVisibleFrame()!!,
                        scratchDetailFrag,
                        itemScratchRootCv,
                        itemScratchRootCv.transitionName)

                }

            })
        binding.userScratchCardsRv.layoutManager =
            GridLayoutManager(requireContext(), 2)

        binding.userScratchCardsRv.addItemDecoration(GridItemOffsetDecoration(2,
            resources.getDimensionPixelSize(R.dimen.dimen10dp)))
        binding.userScratchCardsRv.adapter = scratchCardAdapter

         */

    }

    private fun setUI() {
        val transactions = customerScratchListAdapter.snapshot().items
        if (!transactions.isNullOrEmpty()) {
            try {
                val bal = transactions[0].totalCount
                binding.totalRewardValueTv.text = bal?.toString()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.totalRewardValueTv.text = "0"
        }
    }

    fun showShimmer() {
        binding.myScratchShimmerSv.pendingOrderShimmerSl.makeViewVisible()
        binding.myScratchShimmerSv.pendingOrderShimmerSl.startShimmer()
        binding.userScratchCardsRv.makeViewGone()
        binding.scratchUnAvailableLl.makeViewGone()
        binding.scratchCardHeaderRl.makeViewGone()
    }

    fun hideShimmer() {
        binding.myScratchShimmerSv.pendingOrderShimmerSl.makeViewGone()
        binding.myScratchShimmerSv.pendingOrderShimmerSl.stopShimmer()
        binding.userScratchCardsRv.makeViewVisible()
        binding.scratchUnAvailableLl.makeViewVisible()
        binding.scratchCardHeaderRl.makeViewVisible()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentScratchCardBinding.inflate(inflater, container, false)
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
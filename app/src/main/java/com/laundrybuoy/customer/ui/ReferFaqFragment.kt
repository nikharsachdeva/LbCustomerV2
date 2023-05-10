package com.laundrybuoy.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.adapter.refer.ReferFaqAdapter
import com.laundrybuoy.customer.databinding.FragmentReferFaqBinding
import com.laundrybuoy.customer.model.refer.FaqModel

class ReferFaqFragment : BaseFragment() {

    private var _binding: FragmentReferFaqBinding? = null
    private val binding get() = _binding!!
    private lateinit var faqAdapter: ReferFaqAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFaqRv()
        setFaqData()
    }

    private fun setFaqData() {
        val listOfFaq: MutableList<FaqModel.FaqModelItem> = arrayListOf()
        listOfFaq.add(FaqModel.FaqModelItem(
            "It’s difficult to get into the head of a shopper. Some buy on impulse and others buy after thorough research. By tracking the clicks on your FAQ page you can gather insights about your product or service that you didn’t know before.",
            1,
            false,
            "Provides new insights?"
        ))

        listOfFaq.add(FaqModel.FaqModelItem(
            "If you want your FAQ to be extremely thorough, which it should be, you can link to resources within your FAQ for your customers to find out more information.",
            2,
            false,
            "Drives internal page views?"
        ))

        listOfFaq.add(FaqModel.FaqModelItem(
            "When looking through negative reviews, there’s almost always one thing in common— the problem could’ve been avoided.",
            3,
            false,
            "Prevents negative reviews"
        ))

        listOfFaq.add(FaqModel.FaqModelItem(
            "In order to take full advantage of your FAQ page’s ability to improve website SEO, create one page with all of the questions and then link out to dedicated pages that answer each question in more depth. ",
            4,
            false,
            "FAQs are good for SEO "
        ))

        listOfFaq.add(FaqModel.FaqModelItem(
            "One of the hottest tech trends is voice search. Siri is an old friend, Alexa is newer to the crew, and there will be many more robots introduced to us as time goes on. ",
            5,
            false,
            "Optimizes your site for voice search"
        ))

        listOfFaq.add(FaqModel.FaqModelItem(
            "If you’re considering making an FAQ page and don’t know if it’s completely necessary, that’s ok. An FAQ page is almost always appropriate. If you get lots of the same questions over and over again, then you definitely need to create an FAQ page.",
            6,
            false,
            "When is an FAQ page appropriate? "
        ))


        faqAdapter.submitList(listOfFaq)
    }

    private fun initFaqRv() {
        faqAdapter = ReferFaqAdapter()
        binding.faqRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.faqRv.adapter = faqAdapter
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentReferFaqBinding.inflate(inflater, container, false)
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
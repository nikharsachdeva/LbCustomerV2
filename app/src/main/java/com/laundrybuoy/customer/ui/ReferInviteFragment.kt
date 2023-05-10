package com.laundrybuoy.customer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.customer.BaseFragment
import com.laundrybuoy.customer.adapter.refer.ReferFriendsAdapter
import com.laundrybuoy.customer.databinding.FragmentReferInviteBinding
import com.laundrybuoy.customer.model.refer.ReferedFriendModel
import com.laundrybuoy.customer.utils.isPackageInstalled


class ReferInviteFragment : BaseFragment() {

    private var _binding: FragmentReferInviteBinding? = null
    private val binding get() = _binding!!
    private lateinit var friendsAdapter: ReferFriendsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initFriendsRv()
        setFriendsData()
        onClick()
    }

    private fun setFriendsData() {
        val listOfFriends: MutableList<ReferedFriendModel.ReferedFriendModelItem> = arrayListOf()
        listOfFriends.add(ReferedFriendModel.ReferedFriendModelItem(
            id = 1,
            hasOrdered = false,
            name = "Nikhar Sachdeva"
            ))

        listOfFriends.add(ReferedFriendModel.ReferedFriendModelItem(
            id = 2,
            hasOrdered = true,
            name = "Anamika"
        ))

        listOfFriends.add(ReferedFriendModel.ReferedFriendModelItem(
            id = 3,
            hasOrdered = true,
            name = "Piku Mayank Singh"
        ))

        listOfFriends.add(ReferedFriendModel.ReferedFriendModelItem(
            id = 4,
            hasOrdered = false,
            name = "T2"
        ))

        listOfFriends.add(ReferedFriendModel.ReferedFriendModelItem(
            id = 5,
            hasOrdered = false,
            name = "T"
        ))

        friendsAdapter.submitList(listOfFriends)
    }

    private fun initFriendsRv() {

        friendsAdapter = ReferFriendsAdapter()
        binding.referFriendsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.referFriendsRv.adapter = friendsAdapter


    }

    private fun onClick() {
        binding.referWhatsappIv.setOnClickListener {
            if (requireContext().isPackageInstalled("com.whatsapp")) {
                val message = "Hello, this is a message from my app!" // replace with the message you want to send
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/?text=${Uri.encode(message)}")
                startActivity(intent)
            } else {
                getMainActivity()?.showSnackBar("Whatsapp not installed")
            }
        }

        binding.referGmailIv.setOnClickListener {
            if (requireContext().isPackageInstalled("com.google.android.gm")) {
                try {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + ""))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject")
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text")
                    startActivity(intent)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else {
                getMainActivity()?.showSnackBar("Whatsapp not installed")
            }
        }

        binding.referSmsIv.setOnClickListener {
            try {
                val smsIntent = Intent(Intent.ACTION_VIEW)
                smsIntent.type = "vnd.android-dir/mms-sms"
//                smsIntent.putExtra("address", "your desired phoneNumber")
                smsIntent.putExtra("sms_body", "your desired message")
                smsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(smsIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "No Sim Present!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.referOtherIv.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "text here")
            startActivity(Intent.createChooser(intent, "Share with:"))

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentReferInviteBinding.inflate(inflater, container, false)
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
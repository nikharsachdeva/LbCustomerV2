package com.laundrybuoy.customer

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.laundrybuoy.customer.databinding.ActivityMainBinding
import com.laundrybuoy.customer.ui.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ParentActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        manageClicks()
        //add Splash Screen
        addFragment(true, 1, SplashFragment())
    }

    private fun manageClicks() {
        binding.bottomNavLayout.ivTabHome?.setOnClickListener(this)
        binding.bottomNavLayout.ivTabPlus?.setOnClickListener(this)
        binding.bottomNavLayout.ivTabPrices?.setOnClickListener(this)
        binding.bottomNavLayout.ivTabAccount?.setOnClickListener(this)
        binding.bottomNavLayout.ivTabOrder?.setOnClickListener(this)
    }

    fun showSnackBar(message: String?) {
        hideKeyboard()
        Snackbar.make(binding.rlMainView, message!!, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivTabHome, R.id.ivTabPlus, R.id.ivTabPrices, R.id.ivTabAccount, R.id.ivTabOrder -> handleBottomNavTabClicks(
                view
            )
        }
    }

    private fun handleBottomNavTabClicks(view: View) {
        when (view.id) {
            R.id.ivTabHome -> {
                if (binding.rlFragmentContainer1.visibility == View.VISIBLE) {
                    if (fragmentStack1.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }
                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.BOLD
                )
                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.colorOrange))
                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorOrange
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.rlFragmentContainer1.visibility = View.VISIBLE
                binding.rlFragmentContainer2.visibility = View.GONE
                binding.rlFragmentContainer3.visibility = View.GONE
                binding.rlFragmentContainer4.visibility = View.GONE
                binding.rlFragmentContainer5.visibility = View.GONE

                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabOrder.setImageDrawable(resources.getDrawable(R.drawable.ic_order))
                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.bottomNavLayout.tvTabPlus.setTypeface(
                    binding.bottomNavLayout.tvTabPlus.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabPlus.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabPlus.setImageDrawable(resources.getDrawable(R.drawable.ic_diamond))


                binding.bottomNavLayout.tvTabPrices.setTypeface(
                    binding.bottomNavLayout.tvTabPrices.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabPrices.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabPrices.setImageDrawable(resources.getDrawable(R.drawable.ic_price))
                binding.bottomNavLayout.ivTabPrices.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabAccount.setTypeface(
                    binding.bottomNavLayout.tvTabAccount.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabAccount.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabAccount.setImageDrawable(resources.getDrawable(R.drawable.ic_user))
                binding.bottomNavLayout.ivTabAccount.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )


                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 1, HomeFragment())
                }
                updateFragments()


            }
            R.id.ivTabOrder -> {

                if (binding.rlFragmentContainer2.visibility == View.VISIBLE) {
                    if (fragmentStack2.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }

                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.colorGreen))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.BOLD
                )

                binding.bottomNavLayout.ivTabOrder.setImageDrawable(resources.getDrawable(R.drawable.ic_order))
                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorGreen
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.rlFragmentContainer1.visibility = View.GONE
                binding.rlFragmentContainer2.visibility = View.VISIBLE
                binding.rlFragmentContainer3.visibility = View.GONE
                binding.rlFragmentContainer4.visibility = View.GONE
                binding.rlFragmentContainer5.visibility = View.GONE

                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.BOLD
                )
                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabPlus.setTypeface(
                    binding.bottomNavLayout.tvTabPlus.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabPlus.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabPlus.setImageDrawable(resources.getDrawable(R.drawable.ic_diamond))


                binding.bottomNavLayout.tvTabPrices.setTypeface(
                    binding.bottomNavLayout.tvTabPrices.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabPrices.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabPrices.setImageDrawable(resources.getDrawable(R.drawable.ic_price))
                binding.bottomNavLayout.ivTabPrices.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabAccount.setTypeface(
                    binding.bottomNavLayout.tvTabAccount.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabAccount.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabAccount.setImageDrawable(resources.getDrawable(R.drawable.ic_user))
                binding.bottomNavLayout.ivTabAccount.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 2, OrdersFragment())
                }

                updateFragments()


            }
            R.id.ivTabPlus -> {
                if (binding.rlFragmentContainer3.visibility == View.VISIBLE) {
                    if (fragmentStack3.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }

                binding.bottomNavLayout.tvTabPlus.setTypeface(
                    binding.bottomNavLayout.tvTabPlus.typeface,
                    Typeface.BOLD
                )
                binding.bottomNavLayout.tvTabPlus.setTextColor(resources.getColor(R.color.colorPink))
                binding.bottomNavLayout.ivTabPlus.setImageDrawable(resources.getDrawable(R.drawable.ic_diamond))


                binding.rlFragmentContainer1.visibility = View.GONE
                binding.rlFragmentContainer2.visibility = View.GONE
                binding.rlFragmentContainer3.visibility = View.VISIBLE
                binding.rlFragmentContainer4.visibility = View.GONE
                binding.rlFragmentContainer5.visibility = View.GONE

                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.BOLD
                )
                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabOrder.setImageDrawable(resources.getDrawable(R.drawable.ic_order))
                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabPrices.setTypeface(
                    binding.bottomNavLayout.tvTabPrices.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabPrices.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabPrices.setImageDrawable(resources.getDrawable(R.drawable.ic_price))
                binding.bottomNavLayout.ivTabPrices.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabAccount.setTypeface(
                    binding.bottomNavLayout.tvTabAccount.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabAccount.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabAccount.setImageDrawable(resources.getDrawable(R.drawable.ic_user))
                binding.bottomNavLayout.ivTabAccount.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )


                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 3, PlusFragment())
                }

                updateFragments()


            }
            R.id.ivTabPrices -> {

                if (binding.rlFragmentContainer4.visibility == View.VISIBLE) {
                    if (fragmentStack4.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }

                binding.bottomNavLayout.tvTabPrices.setTypeface(
                    binding.bottomNavLayout.tvTabPrices.typeface,
                    Typeface.BOLD
                )
                binding.bottomNavLayout.tvTabPrices.setTextColor(resources.getColor(R.color.colorPurple))
                binding.bottomNavLayout.ivTabPrices.setImageDrawable(resources.getDrawable(R.drawable.ic_price))
                binding.bottomNavLayout.ivTabPrices.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorPurple
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.rlFragmentContainer1.visibility = View.GONE
                binding.rlFragmentContainer2.visibility = View.GONE
                binding.rlFragmentContainer3.visibility = View.GONE
                binding.rlFragmentContainer4.visibility = View.VISIBLE
                binding.rlFragmentContainer5.visibility = View.GONE

                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.BOLD
                )
                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabOrder.setImageDrawable(resources.getDrawable(R.drawable.ic_order))
                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabPlus.setTypeface(
                    binding.bottomNavLayout.tvTabPlus.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabPlus.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabPlus.setImageDrawable(resources.getDrawable(R.drawable.ic_diamond))


                binding.bottomNavLayout.tvTabAccount.setTypeface(
                    binding.bottomNavLayout.tvTabAccount.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabAccount.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabAccount.setImageDrawable(resources.getDrawable(R.drawable.ic_user))
                binding.bottomNavLayout.ivTabAccount.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )



                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 4, PricesFragment())
                }

                updateFragments()


            }
            R.id.ivTabAccount -> {

                if (binding.rlFragmentContainer5.visibility == View.VISIBLE) {
                    if (fragmentStack5.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }

                binding.bottomNavLayout.tvTabAccount.setTypeface(
                    binding.bottomNavLayout.tvTabAccount.typeface,
                    Typeface.BOLD
                )
                binding.bottomNavLayout.tvTabAccount.setTextColor(resources.getColor(R.color.colorVoilate))
                binding.bottomNavLayout.ivTabAccount.setImageDrawable(resources.getDrawable(R.drawable.ic_user))
                binding.bottomNavLayout.ivTabAccount.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorVoilate
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.rlFragmentContainer1.visibility = View.GONE
                binding.rlFragmentContainer2.visibility = View.GONE
                binding.rlFragmentContainer3.visibility = View.GONE
                binding.rlFragmentContainer4.visibility = View.GONE
                binding.rlFragmentContainer5.visibility = View.VISIBLE


                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.BOLD
                )
                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabOrder.setImageDrawable(resources.getDrawable(R.drawable.ic_order))
                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabPlus.setTypeface(
                    binding.bottomNavLayout.tvTabPlus.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabPlus.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabPlus.setImageDrawable(resources.getDrawable(R.drawable.ic_diamond))

                binding.bottomNavLayout.tvTabPrices.setTypeface(
                    binding.bottomNavLayout.tvTabPrices.typeface,
                    Typeface.NORMAL
                )
                binding.bottomNavLayout.tvTabPrices.setTextColor(resources.getColor(R.color.unselectedIconColor))
                binding.bottomNavLayout.ivTabPrices.setImageDrawable(resources.getDrawable(R.drawable.ic_price))
                binding.bottomNavLayout.ivTabPrices.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.unselectedIconColor
                    ), PorterDuff.Mode.SRC_IN
                )

                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 5, AccountFragment())
                }

                updateFragments()


            }
        }
    }

    fun setBottomNavigationVisibility(isToShow: Boolean) {
        if (isToShow) {
            binding.bottomNavLayout.bottomnavRootContainer.visibility = View.VISIBLE
        } else {
            binding.bottomNavLayout.bottomnavRootContainer.visibility = View.GONE
        }
    }

    fun updateFragments() {
        val fragment1: Fragment?
        val fragment2: Fragment?
        val fragment3: Fragment?
        val fragment4: Fragment?
        val fragment5: Fragment?
        val fm = supportFragmentManager
        val pos = getVisibleFrame()
        if (fragmentStack1.size > 0) {
            fragment1 = fm.findFragmentByTag(fragmentStack1[fragmentStack1.size - 1])
            if (fragment1 != null) {
                (fragment1 as BaseFragment).setFragmentContainerVisible(pos == 1)
                fragment1.onHiddenChanged(false)
            }
        }
        if (fragmentStack2.size > 0) {
            fragment2 = fm.findFragmentByTag(fragmentStack2[fragmentStack2.size - 1])
            if (fragment2 != null) {
                (fragment2 as BaseFragment).setFragmentContainerVisible(pos == 2)
                fragment2.onHiddenChanged(false)
            }
        }
        if (fragmentStack3.size > 0) {
            fragment3 = fm.findFragmentByTag(fragmentStack3[fragmentStack3.size - 1])
            if (fragment3 != null) {
                (fragment3 as BaseFragment).setFragmentContainerVisible(pos == 3)
                fragment3.onHiddenChanged(false)
            }
        }
        if (fragmentStack4.size > 0) {
            fragment4 = fm.findFragmentByTag(fragmentStack4[fragmentStack4.size - 1])
            if (fragment4 != null) {
                (fragment4 as BaseFragment).setFragmentContainerVisible(pos == 4)
                fragment4.onHiddenChanged(false)
            }
        }
        if (fragmentStack5.size > 0) {
            fragment5 = fm.findFragmentByTag(fragmentStack5[fragmentStack5.size - 1])
            if (fragment5 != null) {
                (fragment5 as BaseFragment).setFragmentContainerVisible(pos == 5)
                fragment5.onHiddenChanged(false)
            }
        }
    }

    fun clickTabPosAt(pos: Int, isToClearStack: Boolean) {
        when (pos) {
            1 -> {
                if (isToClearStack) {
                    clearBackStack(1)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabHome)
            }
            2 -> {
                if (isToClearStack) {
                    clearBackStack(2)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabPlus)
            }
            3 -> {
                if (isToClearStack) {
                    clearBackStack(3)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabOrder)
            }
            4 -> {
                if (isToClearStack) {
                    clearBackStack(4)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabPrices)
            }
            5 -> {
                if (isToClearStack) {
                    clearBackStack(5)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabAccount)
            }
        }
    }

    override fun onBackPressed() {
        if (getVisibleFrameStakList()!!.size == 1 && getVisibleFrame() != 1) {
            clearBackStack(2)
            clearBackStack(3)
            clearBackStack(4)
            clearBackStack(5)
            handleBottomNavTabClicks(binding.bottomNavLayout.ivTabHome)
        } else if (getVisibleFrameStakList()!!.size == 1 && getVisibleFrame() == 1) {
            clearBackStack(0)
            finish()
        } else {
            removeCurrentFragment()

        }
    }

    fun getFirstAndLastMonthWise(monthAsInt: Int) {
        val calendar = GregorianCalendar.getInstance()
        calendar.set(2024, monthAsInt, 24)
        Log.d("calendarTime-->", "getFirstAndLastMonthWise: " + calendar.time)
    }

}
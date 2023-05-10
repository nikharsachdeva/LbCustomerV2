package com.laundrybuoy.customer.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.laundrybuoy.customer.R
import com.laundrybuoy.customer.utils.Constants.BASE_URL
import com.skydoves.balloon.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat


private val ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
fun String.getFormattedDateWithTime(): String? {
    //ISO to 24 Jan 2022 05:00 PM
    val sdf = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    val output = SimpleDateFormat("dd LLL yyyy hh:mm aa")
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun String.getOnlyDate(): String? {
    //ISO to 24 Jan 2022 05:00 PM
    val sdf = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    val output = SimpleDateFormat("dd")
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun LifecycleOwner.showBalloonText(text: String, context: Context) {
    val balloon: Balloon = Balloon.Builder(context)
        .setArrowSize(10)
        .setArrowOrientation(ArrowOrientation.TOP)
        .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        .setArrowPosition(0.5f)
        .setWidth(BalloonSizeSpec.WRAP)
        .setHeight(65)
        .setTextSize(15f)
        .setCornerRadius(4f)
        .setAlpha(0.9f)
        .setText(text)
        .setTextColor(ContextCompat.getColor(context, R.color.colorOrange))
        .setTextIsHtml(true)
        .setIconDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_info_24))
        .setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrangeHighlighter))
//        .setOnBalloonClickListener(onBalloonClickListener)
        .setBalloonAnimation(BalloonAnimation.FADE)
        .setLifecycleOwner(this)
        .build()
}

fun String.getOnlyMonth(): String? {
    //ISO to 24 Jan 2022 05:00 PM
    val sdf = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    val output = SimpleDateFormat("LLL")
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun String.getFormattedDate(): String? {
    //ISO to 24 Jan 2022
    val sdf = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    val output = SimpleDateFormat("dd LLL yyyy")
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun String.getISOToNormal(): String? {
    //ISO to 24 Jan 2022
    val sdf = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    val output = SimpleDateFormat("MM/dd/yyyy")
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText("", text))
    Toast.makeText(this, "Copied $text", Toast.LENGTH_SHORT).show()
}


fun EditText.showKeyboard(
) {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard(
) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}


fun Context.convertDpToPixels(dp: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Long.getStandardizeCount(): String {
    return when {
        this < 1000 -> {
            this.toString()
        }
        this in 1000..999999 -> {
            (this / 1000).toString() + "K"
        }
        else -> {
            (this / 1000000).toString() + "M"
        }
    }
}

fun Button.makeButtonDisabled() {
    alpha = 0.7f
    isEnabled = false
}

fun Button.makeButtonEnabled() {
    alpha = 1.0f
    isEnabled = true
}

fun View.makeViewVisible() {
    visibility = View.VISIBLE
}

fun View.makeViewGone() {
    visibility = View.GONE
}

fun View.makeViewInVisible() {
    visibility = View.INVISIBLE
}

fun View.colorTransition(@ColorRes endColor: Int, duration: Long = 500L) {
    var colorFrom = Color.TRANSPARENT
    if (background is ColorDrawable)
        colorFrom = (background as ColorDrawable).color

    val colorTo = ContextCompat.getColor(context, endColor)
    val colorAnimation: ValueAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = duration

    colorAnimation.addUpdateListener {
        if (it.animatedValue is Int) {
            val color = it.animatedValue as Int
            setBackgroundColor(color)
        }
    }
    colorAnimation.start()
}

fun TextView.colorTransition2(@ColorRes endColor: Int, duration: Long = 500L) {
    var colorFrom = Color.TRANSPARENT
    if (background is ColorDrawable)
        colorFrom = (background as ColorDrawable).color

    val colorTo = ContextCompat.getColor(context, endColor)
    val colorAnimation: ValueAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = duration

    colorAnimation.addUpdateListener {
        if (it.animatedValue is Int) {
            val color = it.animatedValue as Int
            setTextColor(color)
        }
    }
    colorAnimation.start()
}

fun Double.roundOffDecimal(): Double? {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).toDouble()
}

fun String?.getAddressIcon(): Int {

    when (this) {
        "home" -> {
            return R.drawable.address_home
        }

        "office" -> {
            return R.drawable.address_office

        }

        else -> {
            return R.drawable.address_more

        }


    }
}

fun Int.getOrderStatus(): String {

    when (this) {
        -3 -> {
            return "Cancelled"
        }
        -2 -> {
            return "Cancelled"
        }
        -1 -> {
            return "Cancelled"
        }
        0 -> {
            return "Order Placed"
        }
        1 -> {
            return "Order Accepted"
        }
        2 -> {
            return "Rider Assigned For Pickup"
        }
        3 -> {
            return "Out For Pickup"
        }
        4 -> {
            return "Bill Generated"
        }
        5 -> {
            return "Order Received By Vendor"
        }
        6 -> {
            return "Service In Progress"
        }
        7 -> {
            return "Order Packed"
        }
        8 -> {
            return "Rider Assigned For Delivery"
        }
        9 -> {
            return "Out for delivery"
        }
        10 -> {
            return "Delivered"
        }
    }
    return ""
}


fun ImageView.loadImageWithGlide(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .apply(RequestOptions.placeholderOf(R.drawable.ic_home))
        .apply(RequestOptions.errorOf(R.drawable.ic_home))
        .into(this)
}


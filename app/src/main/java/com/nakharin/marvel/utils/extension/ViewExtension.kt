package com.nakharin.marvel.utils.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Px

fun View.updateMargin(@Px left: Int = 0, @Px top: Int = 0, @Px right: Int = 0, @Px bottom: Int = 0) {
    val marginParams = this.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    marginParams.apply {
        setMargins(left, top, right, bottom)
    }
    layoutParams = marginParams
}

fun View?.hideKeyboard() {
    if (this == null) {
        return
    }

    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View?.showSoftKeyboard() {
    if (this == null) {
        return
    }

    if (requestFocus()) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, 0)
    }

}

fun View?.gone() {
    this?.apply {
        visibility = View.GONE
    }
}

fun View?.invisible() {
    this?.apply {
        visibility = View.INVISIBLE
    }
}

fun View?.visible() {
    this?.apply {
        visibility = View.VISIBLE
    }
}


//ref google I/O 2019 https://youtu.be/f3Lm8iOr4mE?t=461
private fun View.animateVisibility(visible: Boolean, duration: Long = 200) {
    val targetAlpha = if (visible) 1f else 0f
    val isSameAlpha = alpha == targetAlpha
    if (visible && visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }

    if (!visible && visibility != View.GONE) {
        visibility = View.GONE
    }

    if (isSameAlpha) return
    visibility = View.VISIBLE
    val anim = animate().alpha(targetAlpha).setDuration(duration)
    if (!visible) {
        anim.withEndAction { visibility = View.GONE }
    }
}

fun View.hide(duration: Long = 200) {
    animateVisibility(false, duration)
}

fun View.show(duration: Long = 200) {
    animateVisibility(true, duration)
}

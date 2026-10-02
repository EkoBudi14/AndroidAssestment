package com.example.androidassestmentproject.presentation.common

import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.androidassestmentproject.R

fun View.applySystemBarInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}

fun TextView.showDataBanner(isOnline: Boolean, isShowingSavedData: Boolean) {
    isVisible = !isOnline || isShowingSavedData
    setText(if (isOnline) R.string.banner_saved_data else R.string.banner_offline)
}
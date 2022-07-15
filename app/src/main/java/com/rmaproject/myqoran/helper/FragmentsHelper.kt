package com.rmaproject.myqoran.helper

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar

fun Fragment.changeToolbarTitle(toolbarId:Int, title:String) {
    requireActivity().findViewById<MaterialToolbar>(toolbarId).title = title
}
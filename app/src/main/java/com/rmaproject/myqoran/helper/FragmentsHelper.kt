package com.rmaproject.myqoran.helper

import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar

fun Fragment.changeToolbarTitle(toolbarId:Int, title:String) {
    requireActivity().findViewById<MaterialToolbar>(toolbarId).title = title
}
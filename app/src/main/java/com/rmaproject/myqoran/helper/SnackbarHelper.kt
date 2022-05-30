package com.rmaproject.myqoran.helper

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarHelper {
    fun showSnackbarShort(rootView: View, text: String) {
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT)
            .show()
    }
    fun showSnackbarLong(rootView: View, text: String) {
        Snackbar.make(rootView,text, Snackbar.LENGTH_SHORT)
            .show()
    }
}
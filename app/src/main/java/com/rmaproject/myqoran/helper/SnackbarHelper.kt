package com.rmaproject.myqoran.helper

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

object SnackbarHelper {
    fun showSnackbarShort(rootView: View, text: String, label:String, onPressed:((View) -> Unit)? = null) {
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT)
            .setAction(label, onPressed)
            .show()
    }
    fun showSnackbarLong(rootView: View, text: String, label:String, onPressed:((View) -> Unit)? = null) {
        Snackbar.make(rootView,text, Snackbar.LENGTH_SHORT)
            .setAction(label, onPressed)
            .show()
    }
}
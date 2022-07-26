package com.rmaproject.myqoran.helper

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.changeToolbarTitle(toolbarId:Int, title:String) {
    requireActivity().findViewById<MaterialToolbar>(toolbarId).title = title
}
fun showMaterialAlertDialog(
    context: Context,
    title: String,
    message: String,
    positiveButtonMessage: String,
    positiveButtonOnClickListnener: () -> Unit
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonMessage) { _, _ ->
            positiveButtonOnClickListnener()
        }
        .show()
}
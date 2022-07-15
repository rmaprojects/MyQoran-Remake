package com.rmaproject.myqoran.ui.footnotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentBottomsheetFootnotesBinding

class FootNotesBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet_footnotes, container, false)
    }

    private val binding: FragmentBottomsheetFootnotesBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getFootnotes = arguments?.getString(RECEIVE_FOOTNOTE_KEY) as String

        binding.txtFootnotes.text = getFootnotes
        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val RECEIVE_FOOTNOTE_KEY = "RECEIVEFOOTNOTE"
    }
}
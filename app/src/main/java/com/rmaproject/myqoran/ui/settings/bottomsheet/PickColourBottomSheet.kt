package com.rmaproject.myqoran.ui.settings.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentBottomsheetPickColourBinding
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences

class PickColourBottomSheet : BottomSheetDialogFragment() {

    private val binding:FragmentBottomsheetPickColourBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_bottomsheet_pick_colour, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.cardDefaultColour.setOnClickListener {
            SettingsPreferences.currentTheme = 0
            dialog?.dismiss()
            requireActivity().recreate()
        }

        binding.cardBlueColour.setOnClickListener {
            SettingsPreferences.currentTheme = 1
            dialog?.dismiss()
            requireActivity().recreate()
        }

        binding.cardGreenColour.setOnClickListener {
            SettingsPreferences.currentTheme = 2
            dialog?.dismiss()
            requireActivity().recreate()
        }

        binding.cardRedColour.setOnClickListener {
            SettingsPreferences.currentTheme = 3
            dialog?.dismiss()
            requireActivity().recreate()
        }
    }
}
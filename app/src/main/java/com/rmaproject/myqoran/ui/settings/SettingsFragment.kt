package com.rmaproject.myqoran.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentSettingsBinding
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = SettingsPreferences

        initialisation(preferences)

        darkModeViewConfiguration(preferences)
        disableTajweedViewConfiguration(preferences)
        ayahTextSizeViewConfiguration(preferences)
        languageViewConfiguration(preferences)
        focusReadViewConfiguration(preferences)
    }

    private fun focusReadViewConfiguration(preferences: SettingsPreferences) {
        binding.cardSettingsReadingMode.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Aktifkan mode membaca?")
                .setMessage("Mode membaca akan menghilangkan terjemahan, lanjut?\nKamu bisa mengubahnya lagi di pengaturan setelah ini")
                .setPositiveButton("Ya") { _, _ ->
                    preferences.isOnFocusReadMode = true
                    binding.txtStatusReadingMode.text = "Status: Aktif"
                }
                .setNegativeButton("Tidak") { _, _ ->
                    preferences.isOnFocusReadMode = false
                    binding.txtStatusReadingMode.text = "Status: Tidak Aktif"
                }
                .show()
        }
    }

    private fun languageViewConfiguration(preferences: SettingsPreferences) {

        binding.cardSettingsLanguage.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Pilih bahasa")
                .setMessage("Bahasa yang dipilih akan diterapkan di terjemahan dan footnote")
                .setSingleChoiceItems(
                    arrayOf("Indonesia", getString(R.string.txt_english)),
                    preferences.languagePreference
                ) { _, index ->
                    when (index) {
                        0 -> preferences.languagePreference = 0
                        1 -> preferences.languagePreference = 1
                    }
                }
                .show()
        }
    }


    private fun ayahTextSizeViewConfiguration(preferences: SettingsPreferences) {
        binding.sliderSettingsAyahSize.addOnChangeListener { _, value, _ ->
            preferences.ayahSizePreference = value
        }
        binding.sliderSettingsAyahSize.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                binding.txtSettingsAyahPreview.textSize = slider.value
            }

            override fun onStopTrackingTouch(slider: Slider) {
                binding.txtSettingsAyahPreview.textSize = slider.value
                preferences.ayahSizePreference = slider.value
            }
        })
    }

    private fun initialisation(preferences: SettingsPreferences) {
        binding.switchSettingsDarkMode.isChecked = preferences.isDarkMode
        binding.switchSettingsDisableTajweed.isChecked = preferences.showTajweed
        binding.sliderSettingsAyahSize.value = preferences.ayahSizePreference
        binding.txtSettingsAyahPreview.textSize = preferences.ayahSizePreference
        binding.txtStatusReadingMode.text = when (preferences.isOnFocusReadMode) {
            true -> "Status: Aktif"
            false -> "Status: Tidak Aktif"
        }
    }

    private fun disableTajweedViewConfiguration(preferences: SettingsPreferences) {
        binding.switchSettingsDisableTajweed.setOnCheckedChangeListener { _, isChecked ->
            preferences.showTajweed = isChecked
        }
    }

    private fun darkModeViewConfiguration(preferences: SettingsPreferences) {
        binding.switchSettingsDarkMode.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                preferences.isDarkMode = true
                setDarkMode(preferences)
            } else if (!buttonView.isChecked) {
                preferences.isDarkMode = false
                setDarkMode(preferences)
            }
        }

        binding.cardSettingsDarkMode.setOnClickListener {
            when (preferences.isDarkMode) {
                true -> {
                    preferences.isDarkMode = false
                    binding.switchSettingsDarkMode.isChecked = false
                    setDarkMode(preferences)
                }
                false -> {
                    preferences.isDarkMode = true
                    binding.switchSettingsDarkMode.isChecked = false
                    setDarkMode(preferences)
                }
            }
        }
    }

    private fun setDarkMode(preferences: SettingsPreferences) {
        when (preferences.isDarkMode) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
package com.rmaproject.myqoran.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        reciterNameConfiguration(preferences)
        changeColourAccentConfiguration()
    }

    private fun changeColourAccentConfiguration() {
        binding.cardSettingsChangeTheme.setOnClickListener {
            findNavController().navigate(R.id.action_nav_settings_to_pickColourBottomSheet)
        }
    }

    private fun reciterNameConfiguration(preferences: SettingsPreferences) {
        binding.cardSettingsReciter.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Pilih Qari")
                setItems(
                    arrayOf(
                        "Abdurrahman As Sudais",
                        "Alafasy",
                        "Hudaify",
                        "Muhammad Ayyoub",
                    )
                ) { _, position ->
                    preferences.currentReciter = position
                    binding.txtCurrentQari.text =
                        getString(R.string.txt_current_qari) + " " + when (position) {
                            0 -> "Abdurrahman As Sudais"
                            1 -> "Alafasy"
                            2 -> "Hudaify"
                            3 -> "Muhammad Ayyoub"
                            else -> "Abdurrahman As Sudais"
                        }
                }
            }.create().show()
        }
    }

    private fun initialisation(preferences: SettingsPreferences) {
        binding.switchSettingsDarkMode.isChecked = preferences.isDarkMode
        binding.switchSettingsDisableTajweed.isChecked = preferences.showTajweed
        binding.sliderSettingsAyahSize.value = preferences.ayahSizePreference
        binding.txtSettingsAyahPreview.textSize = preferences.ayahSizePreference
        binding.txtStatusReadingMode.text = when (preferences.isOnFocusRead) {
            false -> getString(R.string.txt_status_deactivated)
            true -> getString(R.string.txt_status_activated)
        }
        binding.txtCurrentQari.text =
            getString(R.string.txt_current_qari) + " " + when (preferences.currentReciter) {
                0 -> "Abdurrahman As Sudais"
                1 -> "Alafasy"
                2 -> "Hudaify"
                3 -> "Muhammad Ayyoub"
                else -> "Abdurrahman As Sudais"
            }
    }

    private fun focusReadViewConfiguration(preferences: SettingsPreferences) {

        val choises =
            arrayOf(getString(R.string.txt_activated), getString(R.string.txt_deactivated))
        val selected = when (preferences.isOnFocusRead) {
            true -> 0
            false -> 1
        }

        binding.cardSettingsReadingMode.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.txt_question_read_mode))
                .setSingleChoiceItems(choises, selected) { _, index ->
                    when (index) {
                        0 -> preferences.isOnFocusRead = true
                        1 -> preferences.isOnFocusRead = false
                    }
                }
                .setPositiveButton("Ok") { _, _ ->
                    binding.txtStatusReadingMode.text = when (preferences.isOnFocusRead) {
                        true -> getString(R.string.txt_activated)
                        false -> getString(R.string.txt_deactivated)
                    }
                }
                .create()
                .show()
        }
    }

    private fun languageViewConfiguration(preferences: SettingsPreferences) {

        val listLang = arrayOf("Indonesia", getString(R.string.txt_english))

        binding.cardSettingsLanguage.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.txt_question_select_lang))
                .setSingleChoiceItems(
                    listLang,
                    preferences.languagePreference
                ) { _, index ->
                    when (index) {
                        0 -> preferences.languagePreference = 0
                        1 -> preferences.languagePreference = 1
                    }
                }
                .setNegativeButton(getString(R.string.txt_cancel)) { _, _ ->
                }
                .setPositiveButton("Ok") { _, _ ->
                }
                .create()
                .show()
        }
    }


    private fun ayahTextSizeViewConfiguration(preferences: SettingsPreferences) {
        binding.sliderSettingsAyahSize.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                binding.txtSettingsAyahPreview.textSize = slider.value
                preferences.ayahSizePreference = slider.value
            }

            override fun onStopTrackingTouch(slider: Slider) {
                binding.txtSettingsAyahPreview.textSize = slider.value
                preferences.ayahSizePreference = slider.value
            }
        })

        binding.sliderSettingsAyahSize.addOnChangeListener { _, value, _ ->
            preferences.ayahSizePreference = value
            binding.txtSettingsAyahPreview.textSize = value
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
                    setDarkMode(preferences)
                }
                false -> {
                    preferences.isDarkMode = true
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
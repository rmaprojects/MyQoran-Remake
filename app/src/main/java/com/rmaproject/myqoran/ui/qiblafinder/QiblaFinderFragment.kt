package com.rmaproject.myqoran.ui.qiblafinder

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.just.agentweb.AgentWeb
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences

class QiblaFinderFragment : Fragment(R.layout.fragment_qibla_finder) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var url = ""

        when (SettingsPreferences.languagePreference) {
            0 -> url = "https://qiblafinder.withgoogle.com/intl/id/"
            1 -> url = "https://qiblafinder.withgoogle.com/intl/en/"
        }

        AgentWeb.with(requireActivity())
            .setAgentWebParent((view as RelativeLayout), RelativeLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(url)
    }
}
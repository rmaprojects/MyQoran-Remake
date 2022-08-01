package com.rmaproject.myqoran.ui.about

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about) {

    private val binding: FragmentAboutBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnGoToGithub.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = "https://www.github.com/RMA-17".toUri()
                startActivity(intent)
            }
            Glide.with(requireContext())
                .load("https://avatars.githubusercontent.com/u/69703296?v=4")
                .placeholder(R.drawable.ic_baseline_close_24)
                .into(imgDev)
        }
    }
}
package com.rmaproject.myqoran

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import com.rmaproject.myqoran.databinding.ActivityMainBinding
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyDarkModeSettings()
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        binding.drawerLayout

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,R.id.nav_settings, R.id.nav_about
            ), drawerLayout
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home -> {
                    binding.appBarMain.toolbar.isVisible = true
                }
                R.id.nav_settings -> {
                    binding.appBarMain.toolbar.isVisible = true
                }
                R.id.nav_about -> {
                    binding.appBarMain.toolbar.isVisible = true
                }
                R.id.nav_read_fragment -> {
                    binding.appBarMain.toolbar.isVisible = true
                }
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun applyDarkModeSettings() {
        when (SettingsPreferences.isDarkMode) {
            DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        const val DARK_MODE = true
        const val LIGHT_MODE = false
    }
}
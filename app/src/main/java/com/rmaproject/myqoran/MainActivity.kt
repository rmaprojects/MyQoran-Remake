package com.rmaproject.myqoran

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.rmaproject.myqoran.api.ApiInterface
import com.rmaproject.myqoran.databinding.ActivityMainBinding
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences
import com.rmaproject.myqoran.viewmodel.LocationViewModel
import com.rmaproject.myqoran.viewmodel.SholatScheduleViewModel
import kotlinx.coroutines.launch
import mumayank.com.airlocationlibrary.AirLocation
import mumayank.com.airlocationlibrary.AirLocation.Callback

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var airLocation:AirLocation
    private val locationViewModel:LocationViewModel by viewModels()
    private val sholatScheduleViewModel:SholatScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyDarkModeSettings()
        setContentView(binding.root)

        airLocation = AirLocation(this, object: Callback {
            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                Snackbar.make(binding.root, "Tidak dapat mendapatkan lokasi", Snackbar.LENGTH_LONG).show()
            }

            override fun onSuccess(locations: ArrayList<Location>) {
                locationViewModel.setLatitude(locations[0].latitude)
                locationViewModel.setLongitude(locations[0].longitude)

                val longitude = locations[0].longitude
                val latitude = locations[0].latitude

                val api = ApiInterface.createApi()

                lifecycleScope.launch {
                    try {
                        val response = api.getJadwalSholat(latitude.toString(), longitude.toString())
                        if (response.isSuccessful) {
                            sholatScheduleViewModel.shubuhTime = response.body()?.times?.get(0)?.fajr
                            sholatScheduleViewModel.dzuhurTime = response.body()?.times?.get(0)?.dhuhr
                            sholatScheduleViewModel.asharTime = response.body()?.times?.get(0)?.asr
                            sholatScheduleViewModel.maghribTime = response.body()?.times?.get(0)?.maghrib
                            sholatScheduleViewModel.isyaTime = response.body()?.times?.get(0)?.isha
                        } else {
                            sholatScheduleViewModel.shubuhTime = null
                            sholatScheduleViewModel.dzuhurTime = null
                            sholatScheduleViewModel.asharTime = null
                            sholatScheduleViewModel.maghribTime = null
                            sholatScheduleViewModel.isyaTime = null
                        }
                    } catch (e: Exception) {
                        Log.d("Jadwal Sholat Err", e.toString())
                        MaterialAlertDialogBuilder(this@MainActivity)
                            .setTitle("Terjadi kesalahan")
                            .setMessage("Internet dalam kondisi tidak baik, aktifkan internet anda")
                            .setPositiveButton("Ok"){_, _ ->}
                            .create()
                            .show()
                    }
                }
            }

        }, true)

        airLocation.start()

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val DARK_MODE = true
        const val LIGHT_MODE = false
    }
}
package com.rmaproject.myqoran

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.location.LocationManagerCompat
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
import com.rmaproject.myqoran.api.ApiInterface
import com.rmaproject.myqoran.databinding.ActivityMainBinding
import com.rmaproject.myqoran.helper.SnackbarHelper
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences
import com.rmaproject.myqoran.viewmodel.LocationViewModel
import com.rmaproject.myqoran.viewmodel.SholatScheduleViewModel
import kotlinx.coroutines.launch
import mumayank.com.airlocationlibrary.AirLocation
import mumayank.com.airlocationlibrary.AirLocation.Callback

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var airLocation: AirLocation
    private val locationViewModel: LocationViewModel by viewModels()
    private val sholatScheduleViewModel: SholatScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        applyDarkModeSettings()
        applyColourAccent()

        super.onCreate(savedInstanceState)

        airLocation = AirLocation(this, object : Callback {
            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {

                val longitude = locationViewModel.getLongitude()
                val latitude = locationViewModel.getLatitude()

                val api = ApiInterface.createApi()

                fetchApi(longitude, latitude, api)
            }

            override fun onSuccess(locations: ArrayList<Location>) {
                locationViewModel.setLatitude(locations[0].latitude)
                locationViewModel.setLongitude(locations[0].longitude)

                val longitude = locations[0].longitude
                val latitude = locations[0].latitude

                val api = ApiInterface.createApi()

                fetchApi(longitude, latitude, api)
            }
        }, true)

        if (isLocationEnabled(this)) {
            airLocation.start()
        } else {
            val longitude = locationViewModel.getLongitude()
            val latitude = locationViewModel.getLatitude()
            val api = ApiInterface.createApi()
            SnackbarHelper.showSnackbarShort(
                binding.root,
                "Akses lokasi dinonaktifkan,\nberalih ke lokasi default",
                "Ok"
            ) {}
            fetchApi(longitude, latitude, api)
        }

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
                R.id.nav_home, R.id.nav_settings, R.id.nav_about, R.id.nav_kibla
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            var toolbarIsVisible:Boolean
            when (destination.id) {
                R.id.nav_home -> {
                    toolbarIsVisible = true
                }
                R.id.nav_settings -> {
                    toolbarIsVisible = true
                }
                R.id.nav_about -> {
                    toolbarIsVisible = true
                }
                R.id.nav_read -> {
                    toolbarIsVisible = true
                }
                R.id.nav_search_surah -> {
                    toolbarIsVisible = false
                }
                R.id.nav_search_ayah -> {
                    toolbarIsVisible = false
                }
                R.id.nav_bottom_sheet_footnotes -> {
                    toolbarIsVisible = false
                }
                else -> {
                    toolbarIsVisible = true
                }
            }

            binding.appBarMain.toolbar.isVisible = toolbarIsVisible
        }
        navView.setupWithNavController(navController)
    }

    private fun applyColourAccent() {
        when (SettingsPreferences.currentTheme) {
            0 -> setTheme(R.style.AppThemeDefault)
            1 -> setTheme(R.style.AppThemeBlue)
            2 -> setTheme(R.style.AppThemeGreen)
            3 -> setTheme(R.style.AppThemeRed)
        }
    }

    private fun fetchApi(longitude: Double, latitude: Double, api: ApiInterface) {
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
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Terjadi kesalahan")
                    .setMessage("Internet dalam kondisi tidak baik, aktifkan internet anda")
                    .setPositiveButton("Ok") { _, _ -> }
                    .create()
                    .show()
            }
        }
    }

    private fun applyDarkModeSettings() {
        when (SettingsPreferences.isDarkMode) {
            DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    companion object {
        const val DARK_MODE = true
        const val LIGHT_MODE = false
    }
}
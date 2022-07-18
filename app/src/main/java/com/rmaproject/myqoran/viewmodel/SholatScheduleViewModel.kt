package com.rmaproject.myqoran.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class SholatScheduleViewModel : ViewModel() {
    var shubuhTime:String? = null
    var dzuhurTime:String? = null
    var asharTime:String? = null
    var maghribTime:String? = null
    var isyaTime:String? = null
}

package com.rmaproject.myqoran.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){
    var goToTopListener : (() -> Unit)? = null
    var hideFabListener:(() -> Unit)? = null
    var showFabListener:(() -> Unit)? = null
}
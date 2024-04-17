package com.example.scorllablegridview.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



/*class MainViewModelFactory(
    private val connectivityManager: ConnectivityManager,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(  key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        if( modelClass.isAssignableFrom( MainViewModel::class.java ) ) {
            @Suppress( "UNCHECKED_CAST" )
            return MainViewModel( connectivityManager,handle) as T
        }
        throw IllegalArgumentException( "Unknown ViewModel Class" )
    }

}*/

class MainViewModelFactory(
    private val connectivityManager: ConnectivityManager,
    private val appContext: Application,

    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create( modelClass: Class<T> ): T {
        if( modelClass.isAssignableFrom( MainViewModel::class.java ) ) {
            @Suppress( "UNCHECKED_CAST" )
            return MainViewModel( connectivityManager, appContext) as T
        }
        throw IllegalArgumentException( "Unknown ViewModel Class" )
    }

}

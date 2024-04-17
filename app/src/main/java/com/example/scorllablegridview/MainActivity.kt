package com.example.scorllablegridview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scorllablegridview.ui.theme.ScorllableGridViewTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scorllablegridview.utils.ConnectivityStatus
import com.example.scorllablegridview.utils.connectivityManager
import com.example.scorllablegridview.viewmodel.MainViewModel
import com.example.scorllablegridview.viewmodel.MainViewModelFactory

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL


import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.scorllablegridview.utils.ImageListItem
import com.example.scorllablegridview.utils.LocalCachingDataMessage
import com.example.scorllablegridview.utils.loadCircularProgressIndicator
import com.example.scorllablegridview.utils.myGridView
import com.example.scorllablegridview.utils.pulltorefresh.ReversiblePullRefreshIndicator
import com.example.scorllablegridview.utils.pulltorefresh.rememberReversiblePullRefreshState
import com.example.scorllablegridview.utils.pulltorefresh.reversiblePullRefresh

import kotlinx.coroutines.launch

import java.io.File
import java.io.FileOutputStream


class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(

            connectivityManager = application.connectivityManager,
            appContext = application

        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScorllableGridViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if (viewModel.loading) {
                        loadCircularProgressIndicator()

                    } else {
                        myGridView(viewModel)
//                        gridView(viewModel)

                    }
                }
            }
        }
    }
}
















package com.example.scorllablegridview.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.scorllablegridview.R

import com.example.scorllablegridview.utils.pulltorefresh.ReversiblePullRefreshIndicator
import com.example.scorllablegridview.utils.pulltorefresh.rememberReversiblePullRefreshState
import com.example.scorllablegridview.utils.pulltorefresh.reversiblePullRefresh
import com.example.scorllablegridview.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun LocalCachingDataMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Offline Local Data Display",
            color = Color.Red,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
//        CircularProgressIndicator(Modifier.padding(top = 10.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun myGridView(
    viewModel: MainViewModel,
) {

    val reverseLayout = true
    val refreshScope = rememberCoroutineScope()
//    var refreshing by remember { mutableStateOf(false) }
    var weaponsData = viewModel.weaponsData.collectAsState().value
    val isConnectivityAvailable = viewModel.isConnectivityAvailable.collectAsState()
    var isOfflineDataDisplay = !isConnectivityAvailable.value && weaponsData.isEmpty()

    if (isOfflineDataDisplay) {

//        weaponsData = viewModel.localOfflineListImages.collectAsState().value
        weaponsData = viewModel.localOfflineListImages
        Log.e("weaponsDataOf", "${weaponsData.size}")
    }

    fun triggerPagingRefresh() = refreshScope.launch {
        if (isConnectivityAvailable.value) {
            // Here should be your refresh logic (or it can be passed as lambda into Composable function).
            viewModel.paginationPage++
            viewModel.retrieveApiData()
        }

    }

    val state = rememberReversiblePullRefreshState(
        refreshing = viewModel.refreshing,
        onRefresh = ::triggerPagingRefresh,
        reverseLayout = reverseLayout
    )





    Box(modifier = Modifier.reversiblePullRefresh(state = state, reverseLayout = reverseLayout)) {
        Column(

            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize()
                .reversiblePullRefresh(enabled = true, reverseLayout = true, state = state)

        ) {


            Spacer(modifier = Modifier.padding(4.dp))
            if (isConnectivityAvailable != null) {
                ConnectivityStatus(isConnectivityAvailable.value)
            }

            if (isOfflineDataDisplay) {
                LocalCachingDataMessage(
                    Modifier
                        .background(Color.DarkGray)
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(10.dp)
//                    .weight(1f)

            ) {
                items(weaponsData.size) {


                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        // on below line we are adding padding from our all sides.
                        modifier = Modifier
                            .padding(8.dp)
                            .size(140.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )

                        // on below line we are adding elevation for the card.
                    ) {

                        Column(

                            Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.height(5.dp))
                            ImageListItem(
                                weaponsData[it]!!.urls?.full!!,
                                viewModel.downloadedImages,
                                isConnectivityAvailable,
                                viewModel
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))
//            if (viewModel.refreshing) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(100.dp)
//                        .background(Color.LightGray),
//                    contentAlignment = Alignment.Center
//                ) {
//
////                    val spinnerSize = (ArcRadius + StrokeWidth).times(2)
//
////                    if (refreshing) {
//                    CircularProgressIndicator(
//                        color = Color.Blue,
//                        strokeWidth = 3.dp,
//                        modifier = Modifier.size(25.dp),
//                    )
////                    } else {
////                        ReversibleCircularArrowIndicator(state, contentColor, Modifier.size(spinnerSize))
////                    }
//                }
//            }

        }
    }
    ReversiblePullRefreshIndicator(
        refreshing = viewModel.refreshing,
        state = state,
//                modifier = Modifier.align(alignment = Alignment.BottomCenter.takeIf { reverseLayout } ?: Alignment.TopCenter),
        modifier = Modifier
            .size(50.dp),
        reverseLayout = reverseLayout
    )
}


@Composable
fun loadCircularProgressIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = Color.Blue)
    }
}


@Composable
fun LoadingAnimation() {
    val animation = rememberInfiniteTransition()
    val progress by animation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart,
        )
    )


    Box(

        modifier = Modifier
            .size(120.dp)
            .scale(progress)
            .alpha(1f - progress)
            .border(
                5.dp,
                color = Color.Green,
                shape = CircleShape
            )
            .padding(25.dp)
    )


}


@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun ImageListItem(
    imageUrl: String,
    downloadedImages: MutableMap<String, File>,
    isConnectivityAvailable: State<Boolean>,
    viewModel: MainViewModel
) {
    var context = LocalContext.current

    val fileName = imageUrl.substringAfterLast("/")
    val file = File(context.cacheDir, fileName)

    var bitmapState: MutableState<String?> = mutableStateOf(null)



    if (file.exists()) {

        Box(contentAlignment = Alignment.Center) {
            downloadedImages[fileName] = file
            val imageFile = downloadedImages[fileName]
            Log.e("imageFile111111111111", imageFile!!.absolutePath)
//        val painter = rememberImagePainter(data = File(imageFile!!.absolutePath))
            val painter = rememberAsyncImagePainter(
                model = File(imageFile.absolutePath),
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_connectivity_available)
            )

            val state = painter.state

            val transition by animateFloatAsState(
                targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f
            )
//
            if (state is AsyncImagePainter.State.Loading) {
                LoadingAnimation()
            }

            if (state is AsyncImagePainter.State.Error) {
                Log.e("stateError", "stateError")
                LaunchedEffect(imageUrl) {
                    viewModel.reDownloadFileForEmptyValue(
                        imageUrl,
                        fileName,
                        context,

                        { downloadedImage ->
                            downloadedImages[fileName] = downloadedImage
                            bitmapState.value = downloadedImage.absolutePath
                        }
                    )
                }
                bitmapState.value?.let {
                    Log.e("imagedownloadBitmapState", it)
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,

                    modifier = Modifier
                        .size(120.dp)
                        .padding(4.dp)
                )
            }


            Image(
                painter = painter,
                contentDescription = "abcd",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .alpha(transition)
                    .size(120.dp)
                    .padding(5.dp)
            )
//


        }
    } else if (isConnectivityAvailable.value) {
//        Log.e("imagedownloadElse", "imagedownloadElse")


        LaunchedEffect(imageUrl) {

            val downloadedImage = viewModel.downloadImage(imageUrl, fileName, context,
                { downloadedImage, isException ->
                    if (isException) {
                        Log.e("isException", "$isException")
//                        isDownloadError.value = isException
                    } else {
                        Log.e("fileDownloadResponcegetteddddd", downloadedImage.absolutePath)
                        downloadedImages[fileName] = downloadedImage
                        bitmapState.value = downloadedImage.absolutePath
                    }
                }
            )
//            downloadedImages[fileName] = downloadedImage
//            bitmapState.value = downloadedImage.absolutePath

        }
        bitmapState.value?.let {
            Log.e("imagedownloadBitmapState", it)
        }


//        Log.e("fileName_else", "fileName_else")
        LoadingAnimation()

    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_connectivity_unavailable),
            contentDescription = null,

            modifier = Modifier
                .size(120.dp)
                .padding(4.dp)
        )
    }
}


@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(id = R.string.strRetry))
        }
    }
}
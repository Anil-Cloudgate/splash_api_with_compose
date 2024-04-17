package com.example.scorllablegridview.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorllablegridview.connectivity.ConnectionState
import com.example.scorllablegridview.connectivity.ConnectivityObserver
import com.example.scorllablegridview.utils.SharedPreferencesHelper
import com.example.scorllablegridview.utils.currentConnectivityState
import com.example.scorllablegridview.utils.observeConnectivityAsFlow
import com.example.scrollablegridview.data.PhotosItems
import com.example.scrollablegridview.network.RetrofitInstance
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class MainViewModel(
    private val connectivityManager: ConnectivityManager,
    private val appContext: Application,

) : ViewModel(),
    ConnectivityObserver {


    //    var downloadedImages = remember { mutableMapOf<String, File>() }
    val downloadedImages = mutableMapOf<String, File>()

    var paginationPage = 0

    private var _internetData: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isConnectivityAvailable: StateFlow<Boolean> = _internetData

    private val _weaponsData: MutableStateFlow<MutableList<PhotosItems>> =
        MutableStateFlow(mutableListOf())
    val weaponsData: StateFlow<MutableList<PhotosItems>> = _weaponsData


    var errorString: String = " "
    var loading: Boolean by mutableStateOf(false)

    var refreshing: Boolean by mutableStateOf(false)


    private val connectivityObserver: ConnectivityObserver
        get() {
            return this
        }

    var localOfflineListImages: MutableList<PhotosItems> = mutableListOf<PhotosItems>()


    init {

        paginationPage = 0
        paginationPage++
        localOfflineListImages = getSaveLocalData()
        retrieveApiData()
        observeConnectivity()


    }

    fun saveLocalData(mitableList: MutableList<PhotosItems>) {
        val gson = Gson()
        val serializedList = mitableList.map { gson.toJson(it) }
        var data = serializedList.joinToString(separator = ";")
        SharedPreferencesHelper.saveData(appContext, data)
    }

    fun getSaveLocalData()
            : MutableList<PhotosItems> {
        var data = SharedPreferencesHelper.getData(appContext)
        if (!data.isNullOrEmpty()) {
            val gson = Gson()
//            return data.split(";")
//                .mapTo(mutableListOf()) { gson.fromJson(it, PhotosItems::class.java) }

            var list = data.split(";")
                .map { gson.fromJson(it, PhotosItems::class.java) }

            var myList = mutableListOf<PhotosItems>()
            myList.addAll(list)

            return myList
        }

        return mutableListOf<PhotosItems>()
    }

    override val connectionState: Flow<ConnectionState>
        get() = connectivityManager.observeConnectivityAsFlow()

    override val currentConnectionState: ConnectionState
        get() = connectivityManager.currentConnectivityState

    private fun observeConnectivity() {
        connectivityObserver.connectionState
            .distinctUntilChanged()
            .map { it === ConnectionState.Available }
            .onEach {
                _internetData.value = it

                if (it && _weaponsData.value.isEmpty()) {
                    retrieveApiData()
                }
            }
            .launchIn(viewModelScope)
    }


    fun retrieveApiData() {
        viewModelScope.launch {

            if (paginationPage == 1) {
                loading = true
            } else {
                refreshing = true
            }

            val call: Call<MutableList<PhotosItems>> = RetrofitInstance.apiService.getAllPhotos(
                "v1",
                "71ZnBug5MjM8vo_oRyWyM-GWCi78qzr2LWQgWdYQHSk",
                paginationPage,
//                1,
                10
            )
            call.enqueue(object : Callback<MutableList<PhotosItems>> {
                override fun onResponse(
                    call: Call<MutableList<PhotosItems>>,
                    response: Response<MutableList<PhotosItems>>
                ) {
                    Log.e("response.toString()", "Network Success")
                    if (response.isSuccessful) {
//                        Log.e("response.toString()", response.body().toString())
                        val responseData: MutableList<PhotosItems>? = response.body()
                        loading = false
                        refreshing = false
                        if (responseData != null) {

                            var mitableList = mutableListOf<PhotosItems>()
                            mitableList.addAll(_weaponsData.value)
                            mitableList.addAll(responseData)
                            Log.e("mitableList.toString()", "${mitableList.size}")

                            _weaponsData.value = mitableList
                            saveLocalData(mitableList)
//                            Log.e("response.toString()", responseData.toString())
                        }


                    }
                }

                override fun onFailure(call: Call<MutableList<PhotosItems>>, t: Throwable) {
                    errorString = t.toString()
                    Log.e("Network Error", t.toString())
//                    Log.e(t.toString(), "Network Error")
                    loading = false
                    refreshing = false

                }

            })
        }
    }



    suspend fun reDownloadFileForEmptyValue(
        imageUrl: String,
        fileName: String,
        context: Context,

        downloadComplete: (File) -> Unit

    ) {


        val downloadedImage = downloadImage(imageUrl, fileName, context,
            { downloadedImage, isException ->
                if (isException) {
                    Log.e("isException", "$isException")

                } else {
                    downloadComplete(downloadedImage)
                    Log.e("reDownload_File", downloadedImage.absolutePath)

                }

            }
        )

    }

    suspend fun downloadImage(
        imageUrl: String,
        fileName: String,
        context: Context,
//    onProgress: (Float) -> Unit,
        downloadComplete: (File, Boolean) -> Unit
    ): File {
        Log.e("downloadImage1111", "downloadImage2222")
        return withContext(Dispatchers.IO) {
            val file = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection().getInputStream()

                val buffer = ByteArray(1024)
                var bytesRead = connection.read(buffer)
                var totalBytesRead = 0L
//            val length = connection.available().toDouble()

                while (bytesRead != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    bytesRead = connection.read(buffer)
                    totalBytesRead += bytesRead
//                val progress = (totalBytesRead.toDouble() / length).toFloat()
//            Log.e("downloadImage1111_length", "$length")

//                onProgress(progress)
                }
            } catch (e: Exception) {
                Log.e("e_download", e.message!!)
                downloadComplete(file, true)
            }
            outputStream.close()
            downloadComplete(file, false)
            file

        }
    }

}
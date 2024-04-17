package com.example.scorllablegridview.network

import com.example.scrollablegridview.data.PhotosItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Accept: application/json")
    @GET("photos?")
    fun getAllPhotos(
        @Header("Accept-Version") acceptversion: String,
        @Query("client_id") param: String,
        @Query("page") param2: Int,
        @Query("per_page") param3: Int
    ): Call<MutableList<PhotosItems>>
}
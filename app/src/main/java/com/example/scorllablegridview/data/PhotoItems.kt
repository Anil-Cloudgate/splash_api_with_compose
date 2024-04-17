package com.example.scrollablegridview.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


@Parcelize
data class PhotosItems(


    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("alternative_slugs")
    val alternativeSlugs: AlternativeSlugsItem? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

    @field:SerializedName("updated_at")
    val updated_at: String? = null,

    @field:SerializedName("promoted_at")
    val promoted_at: String? = null,

//    @field:SerializedName("width")
//    val width: Int? = 0,
//
//    @field:SerializedName("height")
//    val height: Int? = 0,

    @field:SerializedName("color")
    val color: String? = null,

//    @field:SerializedName("blur_hash")
//    val blur_hash: String? = null,

//    @field:SerializedName("description")
//    val description: String? = null,
//
//    @field:SerializedName("alt_description")
//    val alt_description: String? = null,

//    @field:SerializedName("breadcrumbs")
//    val breadcrumbs: List<Any?>? = null,


    @field:SerializedName("urls")
    val urls: UtlsData? = null,

//    @field:SerializedName("links")
//    val links: LinksData? = null,

//    @field:SerializedName("likes")
//    val likes: Int? = 0,

//    @field:SerializedName("liked_by_user")
//    val liked_by_user: Boolean? = false,
//
//    @field:SerializedName("sponsorship")
//    val sponsorship: String? = null,
//
//    @field:SerializedName("asset_type")
//    val asset_type: String? = null,


) : Parcelable


@Parcelize
data class AlternativeSlugsItem(
    @field:SerializedName("en")
    val en: String? = null,

    @field:SerializedName("es")
    val es: String? = null,

    @field:SerializedName("ja")
    val ja: String? = null,

    @field:SerializedName("fr")
    val fr: String? = null,

    @field:SerializedName("it")
    val it: String? = null,

    @field:SerializedName("ko")
    val ko: String? = null,

    @field:SerializedName("de")
    val de: String? = null,

    @field:SerializedName("pt")
    val pt: String? = null,


    ) : Parcelable


@Parcelize
data class UtlsData(

    @field:SerializedName("raw")
    val raw: String? = null,

    @field:SerializedName("full")
    val full: String? = null,

    @field:SerializedName("regular")
    val regular: String? = null,

    @field:SerializedName("small")
    val small: String? = null,

    @field:SerializedName("thumb")
    val thumb: String? = null,

    @field:SerializedName("small_s3")
    val small_s3: String? = null,

    ) : Parcelable


@Parcelize
data class LinksData(

    @field:SerializedName("self")
    val self: String? = null,

    @field:SerializedName("html")
    val html: String? = null,

    @field:SerializedName("download")
    val download: String? = null,

    @field:SerializedName("download_location")
    val download_location: String? = null,


    ) : Parcelable
package com.example.starnetvoyager.data.network.model

import androidx.core.net.toUri
import com.google.gson.annotations.SerializedName

data class FilmResponse(
    @SerializedName("opening_crawl")
    val openingCrawl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
) {

    val id: Int
        get() = url
            .toUri()
            .lastPathSegment
            ?.toInt() ?: 0

}
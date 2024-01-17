package com.example.starnetvoyager.data.network.model

import com.google.gson.annotations.SerializedName

data class FilmResponse(
    @SerializedName("opening_crawl")
    val openingCrawl: String,
    @SerializedName("title")
    val title: String
)
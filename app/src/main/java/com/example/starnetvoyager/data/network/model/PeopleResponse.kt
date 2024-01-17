package com.example.starnetvoyager.data.network.model

import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val nextPageUrl: String?,
    @SerializedName("previous")
    val previousPageUrl: String?,
    @SerializedName("results")
    val results: List<CharacterResponse>
)
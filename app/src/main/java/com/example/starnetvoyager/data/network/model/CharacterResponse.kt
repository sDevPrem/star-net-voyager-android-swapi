package com.example.starnetvoyager.data.network.model

import androidx.core.net.toUri
import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("birth_year")
    val birthYear: String,
    @SerializedName("eye_color")
    val eyeColor: String,
    @SerializedName("films")
    val films: List<String>,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("hair_color")
    val hairColor: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("homeworld")
    val homeworld: String,
    @SerializedName("mass")
    val mass: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("skin_color")
    val skinColor: String,
    @SerializedName("url")
    val url: String
) {
    val id: Int = url
        .toUri()
        .lastPathSegment
        ?.toInt() ?: 0
}
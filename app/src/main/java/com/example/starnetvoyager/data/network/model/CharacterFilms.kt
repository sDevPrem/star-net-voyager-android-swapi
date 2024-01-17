package com.example.starnetvoyager.data.network.model

import com.google.gson.annotations.SerializedName

data class CharacterFilms(
    @SerializedName("films")
    val films: List<String>,
)
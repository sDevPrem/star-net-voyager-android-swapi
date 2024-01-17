package com.example.starnetvoyager.data.network

import com.example.starnetvoyager.data.network.model.CharacterFilms
import com.example.starnetvoyager.data.network.model.FilmResponse
import com.example.starnetvoyager.data.network.model.PeopleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface StarWarsDataSource {

    @GET("people/?page")
    suspend fun getCharacters(@Query("page") page: Int): PeopleResponse

    @GET
    suspend fun getFilm(@Url url: String): FilmResponse

    @GET("films/{id}")
    suspend fun getFilm(@Path("id") id: Int): FilmResponse

    @GET("people/{id}")
    suspend fun getCharacterFilm(@Path("id") id: Int): CharacterFilms

}
package com.example.starnetvoyager.domain.repository

import androidx.paging.PagingData
import com.example.starnetvoyager.domain.entity.StarWarsCharacter
import com.example.starnetvoyager.domain.entity.StarWarsMovie
import kotlinx.coroutines.flow.Flow

interface StarWarsRepository {
    fun getCharacters(searchQuery: String? = null): Flow<PagingData<StarWarsCharacter>>

    fun getMovies(characterId: Int): Flow<PagingData<StarWarsMovie>>
}
package com.example.starnetvoyager.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.starnetvoyager.data.local.StarWarsDataStore
import com.example.starnetvoyager.data.local.entity.toDomain
import com.example.starnetvoyager.data.network.StarWarsDataSource
import com.example.starnetvoyager.data.repository.paging.CharacterRemoteMediator
import com.example.starnetvoyager.data.repository.paging.FilmRemoteMediator
import com.example.starnetvoyager.domain.entity.StarWarsCharacter
import com.example.starnetvoyager.domain.entity.StarWarsMovie
import com.example.starnetvoyager.domain.repository.StarWarsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StarWarsRepositoryImpl @Inject constructor(
    private val starWarsDataSource: StarWarsDataSource,
    private val starWarsDataStore: StarWarsDataStore
) : StarWarsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(searchQuery: String?): Flow<PagingData<StarWarsCharacter>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
        ),
        remoteMediator = CharacterRemoteMediator(
            searchQuery,
            starWarsDataStore,
            starWarsDataSource
        ),
        pagingSourceFactory = { starWarsDataStore.characterDao().getCharacters(searchQuery) }
    ).flow
        .map { it.map { character -> character.toDomain() } }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(characterId: Int): Flow<PagingData<StarWarsMovie>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
        ),
        remoteMediator = FilmRemoteMediator(
            starWarsDataStore,
            starWarsDataSource,
            characterId
        ),
        pagingSourceFactory = { starWarsDataStore.filmDao().getFilmsOfCharacter(characterId) }
    ).flow
        .map { it.map { movie -> movie.toDomain() } }
}
package com.example.starnetvoyager.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.starnetvoyager.data.local.StarWarsDataStore
import com.example.starnetvoyager.data.network.StarWarsDataSource
import com.example.starnetvoyager.data.repository.paging.CharacterRemoteMediator
import javax.inject.Inject

class StarWarsRepository @Inject constructor(
    private val starWarsDataSource: StarWarsDataSource,
    private val starWarsDataStore: StarWarsDataStore
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getCharacters() = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
        ),
        remoteMediator = CharacterRemoteMediator(
            starWarsDataStore,
            starWarsDataSource
        ),
        pagingSourceFactory = { starWarsDataStore.characterDao().getCharacters() }
    )
}
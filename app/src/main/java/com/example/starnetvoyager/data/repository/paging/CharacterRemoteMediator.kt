package com.example.starnetvoyager.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.starnetvoyager.data.local.StarWarsDataStore
import com.example.starnetvoyager.data.local.entity.Character
import com.example.starnetvoyager.data.local.entity.Character.Companion.toCharacter
import com.example.starnetvoyager.data.local.entity.CharacterRemoteKey
import com.example.starnetvoyager.data.network.StarWarsDataSource

@ExperimentalPagingApi
class CharacterRemoteMediator(
    private val searchQuery: String?,
    private val starWarsDataStore: StarWarsDataStore,
    private val starWarsDataSource: StarWarsDataSource
) : RemoteMediator<Int, Character>() {

    private val characterDao = starWarsDataStore.characterDao()
    private val characterRemoteKeyDao = starWarsDataStore.characterRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = starWarsDataSource.getCharacters(searchQuery.orEmpty(), currentPage)
            val endOfPaginationReached = response.nextPageUrl == null

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            starWarsDataStore.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    characterRemoteKeyDao.deleteAllRemoteKeys()
                    if (searchQuery.isNullOrBlank()) {
                        characterDao.deleteCharacters()
                    }
                }

                val ids = characterDao.insertCharacters(response.results.map { it.toCharacter() })
                val keys = List(response.results.size) { i ->
                    CharacterRemoteKey(
                        id = ids[i].toInt(),
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                characterRemoteKeyDao.addAllRemoteKeys(keys)
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Character>
    ): CharacterRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                characterRemoteKeyDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Character>
    ): CharacterRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                characterRemoteKeyDao.getRemoteKeys(id = character.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Character>
    ): CharacterRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                characterRemoteKeyDao.getRemoteKeys(id = character.id)
            }
    }

}
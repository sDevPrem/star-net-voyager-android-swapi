package com.example.starnetvoyager.data.repository.paging

import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.starnetvoyager.data.local.StarWarsDataStore
import com.example.starnetvoyager.data.local.entity.Film
import com.example.starnetvoyager.data.local.entity.Film.Companion.toFilm
import com.example.starnetvoyager.data.local.entity.FilmCharacterRelation
import com.example.starnetvoyager.data.network.StarWarsDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.ceil
import kotlin.math.min

@ExperimentalPagingApi
class FilmRemoteMediator(
    private val starWarsDataStore: StarWarsDataStore,
    private val starWarDataSource: StarWarsDataSource,
    private val characterId: Int
) : RemoteMediator<Int, Film>() {

    private val filmDao = starWarsDataStore.filmDao()
    private val filmCharacterDao = starWarsDataStore.filmCharacterDao()

    private var filmIds = emptyList<Int>()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Film>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    fetchFilmsId(characterId)
                    val page = getPageClosestToCurrentPosition(state)
                    page?.value ?: 1
                }

                LoadType.PREPEND -> {
                    val page = getFirstPage(state)
                    val prevPage = page?.value?.minus(1)
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = page != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val page = getLastPage(state)
                    val nextPage = page?.value?.plus(1)
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = page != null
                        )
                    nextPage
                }
            }

            val response = fetchFilms(
                filmIds.subList(
                    (currentPage - 1) * state.config.pageSize,
                    min(currentPage * state.config.pageSize, filmIds.size)
                )
            )
            val endOfPaginationReached = currentPage * state.config.pageSize >= filmIds.size

            starWarsDataStore.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    filmCharacterDao.deleteFilmRelationOfCharacter(characterId)
                }

                val ids = filmDao.insertFilms(response.map { it.toFilm() })
                val relations = List(response.size) { i ->
                    FilmCharacterRelation(
                        filmId = ids[i].toInt(),
                        characterId = characterId
                    )
                }
                filmCharacterDao.insertFilmCharacterRelations(relations)
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun fetchFilmsId(characterId: Int) {
        val characterResponse = starWarDataSource.getCharacterFilm(characterId)
        filmIds = characterResponse.films.map {
            it.toUri().lastPathSegment?.toIntOrNull() ?: 0
        }
    }

    private suspend fun fetchFilms(filmIds: List<Int>) = coroutineScope {
        filmIds
            .map { async { starWarDataSource.getFilm(it) } }
            .awaitAll()
    }


    private fun getPageClosestToCurrentPosition(
        state: PagingState<Int, Film>
    ): PageNum? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                val filmIndex = filmIds.indexOfFirst { it == id } + 1

                val num = ceil(filmIndex.toFloat() / state.config.pageSize).toInt()
                PageNum(num)
            }
        }
    }

    private fun getFirstPage(
        state: PagingState<Int, Film>
    ): PageNum? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { film ->
                val filmIndex = filmIds.indexOfFirst { it == film.id } + 1

                val num = if (filmIndex == 1)
                    null
                else
                    ceil(filmIndex.toFloat() / state.config.pageSize).toInt()
                return PageNum(num)
            }
    }

    private fun getLastPage(
        state: PagingState<Int, Film>
    ): PageNum? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { film ->
                val filmIndex = filmIds.indexOfFirst { it == film.id } + 1

                val num = if (filmIndex == filmIds.size)
                    null
                else
                    ceil(filmIndex.toFloat() / state.config.pageSize).toInt()

                return PageNum(num)
            }
    }

}

private data class PageNum(val value: Int?)
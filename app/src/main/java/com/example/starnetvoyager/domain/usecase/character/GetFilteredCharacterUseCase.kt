package com.example.starnetvoyager.domain.usecase.character

import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.starnetvoyager.domain.repository.StarWarsRepository
import com.example.starnetvoyager.domain.usecase.character.model.CharacterFilters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class GetFilteredCharacterUseCase @Inject constructor(
    private val repository: StarWarsRepository
) {
    private val param = MutableStateFlow(Params())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val characters =
        param.distinctUntilChanged { old, new ->
            old.filter.searchQuery == new.filter.searchQuery &&
                    new.cacheScope == old.cacheScope
        }
            .mapLatest { param ->
                val pagingData = repository.getCharacters(param.filter.searchQuery)
                param.cacheScope?.let { pagingData.cachedIn(it) } ?: pagingData
            }

    val filteredCharacters = characters
        .combine(param.distinctUntilChangedBy { it.filter.selectedGender }) { data, state ->
            val selectedGender = state.filter.selectedGender

            data.map {
                it.filter { character ->
                    when (selectedGender) {
                        CharacterFilters.Gender.MALE, CharacterFilters.Gender.FEMALE -> {
                            character.gender.equals(selectedGender.name, true)
                        }

                        CharacterFilters.Gender.OTHER -> {
                            !character.gender.equals(CharacterFilters.Gender.MALE.name, true) &&
                                    !character.gender.equals(
                                        CharacterFilters.Gender.FEMALE.name,
                                        true
                                    )
                        }

                        CharacterFilters.Gender.ALL -> true
                    }
                }
            }
        }

    operator fun invoke(filter: CharacterFilters, cacheScope: CoroutineScope) {
        param.update { it.copy(filter = filter, cacheScope = cacheScope) }
    }


    private data class Params(
        val filter: CharacterFilters = CharacterFilters(),
        val cacheScope: CoroutineScope? = null
    )
}

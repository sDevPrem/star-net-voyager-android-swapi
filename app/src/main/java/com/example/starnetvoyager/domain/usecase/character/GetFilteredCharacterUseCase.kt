package com.example.starnetvoyager.domain.usecase.character

import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.starnetvoyager.domain.repository.StarWarsRepository
import com.example.starnetvoyager.domain.usecase.character.model.CharacterFilters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetFilteredCharacterUseCase @Inject constructor(
    private val repository: StarWarsRepository
) {
    private val filter = MutableStateFlow(CharacterFilters())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val characters =
        filter.distinctUntilChangedBy { it.searchQuery }
            .mapLatest {
                repository.getCharacters(it.searchQuery)
                    //cache in parent's coroutineScope
                    .cachedIn(CoroutineScope(coroutineContext))
            }

    val filteredCharacters = characters
        .combine(filter.distinctUntilChangedBy { it.selectedGender }) { data, filter ->
            val selectedGender = filter.selectedGender

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

    fun updateFilter(filter: CharacterFilters) {
        this.filter.value = filter
    }
}

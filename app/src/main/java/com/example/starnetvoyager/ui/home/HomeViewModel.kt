package com.example.starnetvoyager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.starnetvoyager.domain.repository.StarWarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: StarWarsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUIState.EmptyState)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val characters =
        _state.distinctUntilChangedBy { it.filters.searchQuery }
            .mapLatest {
                repository.getCharacters(it.filters.searchQuery).cachedIn(viewModelScope)
            }


    private val filteredCharacters = characters
        .combine(_state.distinctUntilChangedBy { it.filters.selectedGender }) { data, state ->
            val selectedGender = state.filters.selectedGender
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

    val state = _state.combine(filteredCharacters) { state, characters ->
        state.copy(
            characters = characters,
            filters = state.filters
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeUIState.EmptyState
    )

    fun setFilter(filter: CharacterFilters) {
        _state.update {
            it.copy(
                filters = filter
            )
        }
    }
}
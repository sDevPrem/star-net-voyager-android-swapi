package com.example.starnetvoyager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starnetvoyager.domain.usecase.character.GetFilteredCharacterUseCase
import com.example.starnetvoyager.domain.usecase.character.model.CharacterFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFilteredCharacterUseCase: GetFilteredCharacterUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUIState.EmptyState)

    val state = _state.combine(
        getFilteredCharacterUseCase.filteredCharacters
    ) { state, characters ->
        state.copy(
            characters = characters,
            filters = state.filters
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeUIState.EmptyState
    )

    init {
        _state.distinctUntilChangedBy { it.filters }
            .onEach { getFilteredCharacterUseCase.updateFilter(it.filters) }
            .launchIn(viewModelScope)
    }

    fun setFilter(filter: CharacterFilters) {
        _state.update {
            it.copy(
                filters = filter
            )
        }
    }
}
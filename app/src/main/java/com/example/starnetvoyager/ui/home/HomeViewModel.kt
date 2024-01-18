package com.example.starnetvoyager.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.starnetvoyager.data.repository.StarWarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: StarWarsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val characters =
        savedStateHandle.getStateFlow<String?>(SEARCH_QUERY_KEY, null)
            .debounce(1_000)
            .flatMapLatest { repository.getCharacters(it).flow }
            .cachedIn(viewModelScope)

    fun searchCharacter(name: String?) {
        savedStateHandle[SEARCH_QUERY_KEY] = name
    }

    companion object {
        private const val SEARCH_QUERY_KEY = "character_search_query"
    }
}
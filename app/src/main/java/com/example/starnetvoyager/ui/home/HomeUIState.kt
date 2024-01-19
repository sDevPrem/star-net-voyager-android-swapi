package com.example.starnetvoyager.ui.home

import androidx.paging.PagingData
import com.example.starnetvoyager.domain.entity.StarWarsCharacter
import com.example.starnetvoyager.domain.usecase.character.model.CharacterFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeUIState(
    val characters: Flow<PagingData<StarWarsCharacter>> = emptyFlow(),
    val filters: CharacterFilters = CharacterFilters()
) {
    companion object {
        val EmptyState = HomeUIState()
    }
}


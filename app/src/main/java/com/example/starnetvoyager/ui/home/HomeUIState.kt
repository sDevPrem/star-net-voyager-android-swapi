package com.example.starnetvoyager.ui.home

import androidx.paging.PagingData
import com.example.starnetvoyager.data.local.entity.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeUIState(
    val characters: Flow<PagingData<Character>> = emptyFlow(),
    val filters: CharacterFilters = CharacterFilters()
) {
    companion object {
        val EmptyState = HomeUIState()
    }
}

data class CharacterFilters(
    val searchQuery: String = "",
    val selectedGender: Gender = Gender.ALL
) {
    enum class Gender {
        MALE,
        FEMALE,
        OTHER,
        ALL
    }
}
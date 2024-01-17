package com.example.starnetvoyager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.starnetvoyager.data.repository.StarWarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: StarWarsRepository
) : ViewModel() {
    val characters = repository
        .getCharacters()
        .flow
        .cachedIn(viewModelScope)
}
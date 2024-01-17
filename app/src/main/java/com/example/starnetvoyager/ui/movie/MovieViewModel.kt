package com.example.starnetvoyager.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.starnetvoyager.data.repository.StarWarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    repository: StarWarsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val movies = repository
        .getFilmsOfCharacters(MovieFragmentArgs.fromSavedStateHandle(savedStateHandle).charcterId)
        .flow
        .cachedIn(viewModelScope)
}
package com.example.starnetvoyager.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.starnetvoyager.databinding.FragmentMovieBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {
    private var binding: FragmentMovieBinding? = null
    private val viewModel: MovieViewModel by viewModels()

    private val movieAdapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMovieBinding.inflate(
            inflater,
            container,
            false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpAdapter()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {
                    movieAdapter.submitData(lifecycle, it)
                }
        }
    }

    private fun setUpAdapter() = binding?.run {
        moviesRecyclerview.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        movieAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> showProgressbar()
                is LoadState.NotLoading -> showList()
                is LoadState.Error -> {
                    if (movieAdapter.snapshot().isEmpty()) {
                        showErrorMsg(loadState.refresh as LoadState.Error)
                    } else {
                        showList()
                    }
                }
            }
        }
    }

    private fun showErrorMsg(errorLoadState: LoadState.Error?) = binding?.run {
        moviesProgressBar.isVisible = false
        moviesProgressBar.isVisible = false
        textViewError.isVisible = true
        textViewError.text = errorLoadState?.error?.localizedMessage
    }

    private fun showProgressbar() = binding?.run {
        moviesProgressBar.isVisible = true
        textViewError.isVisible = false
        moviesRecyclerview.isVisible = false
    }

    private fun showList() = binding?.run {
        moviesRecyclerview.isVisible = true
        textViewError.isVisible = false
        moviesProgressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.starnetvoyager.common.ui.paging.SimpleLoadStateAdapter
import com.example.starnetvoyager.databinding.FragmentMovieBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var binding: FragmentMovieBinding? = null
    private val viewModel: MovieViewModel by viewModels()
    private val args by navArgs<MovieFragmentArgs>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding?.run {
        topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        topAppBar.title = "Movies of ${args.characterName}"

        setUpAdapter()
        setUpObserver()
    } ?: Unit

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
        moviesRecyclerView.apply {
            val spanCount = 2
            val footerAdapter = SimpleLoadStateAdapter(retry = movieAdapter::retry)
            val concatAdapter = movieAdapter.withLoadStateFooter(footerAdapter)
            val layoutManger = GridLayoutManager(context, spanCount)
                .apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (
                                position == concatAdapter.itemCount - 1 &&
                                footerAdapter.itemCount > 0
                            ) {
                                spanCount
                            } else spanCount / spanCount
                        }
                    }
                }

            adapter = concatAdapter
            this.layoutManager = layoutManger
        }

        movieAdapter.addLoadStateListener { loadState ->
            moviesProgressBar.isVisible = loadState.refresh is LoadState.Loading
            textViewError.isVisible =
                loadState.refresh is LoadState.Error && movieAdapter.snapshot().isEmpty()
            moviesRecyclerView.isVisible =
                loadState.refresh !is LoadState.Loading && movieAdapter.snapshot().isNotEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
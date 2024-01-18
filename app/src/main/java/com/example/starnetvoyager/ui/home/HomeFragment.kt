package com.example.starnetvoyager.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.starnetvoyager.R
import com.example.starnetvoyager.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter {
            findNavController().navigate(
                HomeFragmentDirections
                    .actionHomeFragmentToMovieFragment(
                        it.id,
                        it.name
                    )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding?.run {
        setUpAdapter()
        setUpObserver()

        characterSearch.addTextChangedListener {
            viewModel.searchCharacter(it?.toString())
        }
        return@run
    } ?: Unit

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.characters
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {
                    charactersAdapter.submitData(lifecycle, it)
                }
        }
    }

    private fun setUpAdapter() = binding?.run {
        charactersRecyclerview.apply {
            adapter = charactersAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        charactersAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> showProgressbar()
                is LoadState.NotLoading -> showList()
                is LoadState.Error -> {
                    if (charactersAdapter.snapshot().isEmpty()) {
                        showErrorMsg(loadState.refresh as LoadState.Error)
                    } else {
                        showList()
                    }
                }
            }
        }
    }

    private fun showErrorMsg(errorLoadState: LoadState.Error?) = binding?.run {
        charactersProgressBar.isVisible = false
        charactersRecyclerview.isVisible = false
        textViewError.isVisible = true
        textViewError.setText(R.string.network_error_msg)
    }

    private fun showProgressbar() = binding?.run {
        charactersProgressBar.isVisible = true
        textViewError.isVisible = false
        charactersRecyclerview.isVisible = false
    }

    private fun showList() = binding?.run {
        charactersRecyclerview.isVisible = true
        textViewError.isVisible = false
        charactersProgressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
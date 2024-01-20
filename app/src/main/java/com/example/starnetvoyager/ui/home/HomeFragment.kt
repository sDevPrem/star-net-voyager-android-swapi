package com.example.starnetvoyager.ui.home

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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.starnetvoyager.R
import com.example.starnetvoyager.common.ui.paging.SimpleLoadStateAdapter
import com.example.starnetvoyager.databinding.FragmentHomeBinding
import com.example.starnetvoyager.domain.entity.StarWarsCharacter
import com.example.starnetvoyager.ui.home.dialog.CharacterFilterBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
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

        characterToolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.character_filter -> {
                    CharacterFilterBottomSheet().show(childFragmentManager, null)
                    true
                }

                R.id.character_refresh -> {
                    charactersAdapter.refresh()
                    true
                }

                else -> false
            }
        }

        return@run
    } ?: Unit

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .distinctUntilChangedBy { it.characters }
                .collectLatest {
                    observePagingData(it.characters)
                }
        }
    }

    private fun observePagingData(flow: Flow<PagingData<StarWarsCharacter>>) {
        viewLifecycleOwner.lifecycleScope.launch {
            flow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {
                    charactersAdapter.submitData(lifecycle, it)
                }
        }
    }

    private fun setUpAdapter() = binding?.run {
        charactersRecyclerview.apply {
            val spanCount = 2
            val footerAdapter = SimpleLoadStateAdapter(retry = charactersAdapter::retry)
            val concatAdapter = charactersAdapter.withLoadStateFooter(footerAdapter)
            val layoutManger = GridLayoutManager(context, spanCount)
                .apply {
                    spanSizeLookup = object : SpanSizeLookup() {
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

        charactersAdapter.addLoadStateListener { loadState ->
            charactersProgressBar.isVisible = loadState.refresh is LoadState.Loading
            textViewError.isVisible =
                loadState.refresh is LoadState.Error && charactersAdapter.snapshot().isEmpty()
            charactersRecyclerview.isVisible =
                loadState.refresh !is LoadState.Loading && charactersAdapter.snapshot().isNotEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
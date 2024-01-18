package com.example.starnetvoyager.ui.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.starnetvoyager.R
import com.example.starnetvoyager.databinding.FragmentHomeCharacterFilterBinding
import com.example.starnetvoyager.ui.home.CharacterFilters
import com.example.starnetvoyager.ui.home.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterFilterBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentHomeCharacterFilterBinding? = null
    private val viewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private var userSelectedGender: CharacterFilters.Gender? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeCharacterFilterBinding
            .inflate(
                inflater,
                container,
                false
            ).also {
                binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding?.run {

        val items = CharacterFilters.Gender.entries
            .map { it.name }
            .toTypedArray()

        selectGender.adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            items
        )
        selectGender.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    userSelectedGender = CharacterFilters.Gender.entries[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        applyFilterBtn.setOnClickListener {
            viewModel.setFilter(
                CharacterFilters(
                    searchQuery = characterSearch.text.toString(),
                    selectedGender = userSelectedGender ?: CharacterFilters.Gender.ALL
                )
            )
            dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {
                    characterSearch.setText(it.filters.searchQuery)
                    selectGender.setSelection(it.filters.selectedGender.ordinal)
                    userSelectedGender = it.filters.selectedGender
                }
        }
        return@run
    } ?: Unit

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
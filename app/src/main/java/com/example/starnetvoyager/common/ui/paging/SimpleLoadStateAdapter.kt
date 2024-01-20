package com.example.starnetvoyager.common.ui.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.starnetvoyager.R
import com.example.starnetvoyager.databinding.ItemLoadStateBinding

class SimpleLoadStateAdapter(
    private val getRetryMsg: Context.() -> String = { getString(R.string.network_error_msg) },
    private val retry: () -> Unit
) : LoadStateAdapter<SimpleLoadStateAdapter.SimpleLoadStateViewHolder>() {

    override fun onBindViewHolder(
        holder: SimpleLoadStateViewHolder,
        loadState: LoadState
    ) = holder.binding.run {

        itemLoadStateRetryBtn.isVisible = loadState is LoadState.Error
        itemLoadStateErrorMsg.isVisible = loadState is LoadState.Error
        itemLoadStateProgressBar.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error) {
            itemLoadStateErrorMsg.text = root.context.getRetryMsg()
        }

        itemLoadStateRetryBtn.setOnClickListener { retry() }
        return@run
    }

    override fun getStateViewType(loadState: LoadState): Int {
        return ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): SimpleLoadStateViewHolder {
        return SimpleLoadStateViewHolder(
            ItemLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    data class SimpleLoadStateViewHolder(val binding: ItemLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val ITEM_VIEW_TYPE = 87499
    }
}
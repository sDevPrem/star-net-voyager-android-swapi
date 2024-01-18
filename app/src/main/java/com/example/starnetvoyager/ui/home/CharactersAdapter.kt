package com.example.starnetvoyager.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starnetvoyager.databinding.RvItemCharacterBinding
import com.example.starnetvoyager.domain.entity.StarWarsCharacter

class CharactersAdapter(
    private val onClickListener: (StarWarsCharacter) -> Unit,
) : PagingDataAdapter<StarWarsCharacter, CharactersAdapter.MyViewHolder>(CHARACTER_COMPARATOR) {

    inner class MyViewHolder(private val binding: RvItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: StarWarsCharacter?) {
            binding.character = character
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RvItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener { _ ->
                onClickListener(it)
            }
        }
    }

    companion object {
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<StarWarsCharacter>() {
            override fun areItemsTheSame(
                oldItem: StarWarsCharacter,
                newItem: StarWarsCharacter
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StarWarsCharacter,
                newItem: StarWarsCharacter
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
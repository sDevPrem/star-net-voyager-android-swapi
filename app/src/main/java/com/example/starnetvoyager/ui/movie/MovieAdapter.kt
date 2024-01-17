package com.example.starnetvoyager.ui.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starnetvoyager.data.local.entity.Film
import com.example.starnetvoyager.databinding.RvItemMovieBinding

class MovieAdapter :
    PagingDataAdapter<Film, MovieAdapter.MovieItemViewHolder>(CHARACTER_COMPARATOR) {

    inner class MovieItemViewHolder(private val binding: RvItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(film: Film?) {
            binding.film = film
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        return MovieItemViewHolder(
            RvItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<Film>() {
            override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
                return oldItem == newItem
            }
        }
    }
}
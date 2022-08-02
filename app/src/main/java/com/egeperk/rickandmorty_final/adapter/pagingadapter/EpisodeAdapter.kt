package com.egeperk.rickandmorty_final.adapter.pagingadapter

import androidx.recyclerview.widget.DiffUtil
import com.egeperk.rickandmorty_final.R
import com.example.rnm_mvvm.EpisodesQuery

class EpisodeAdapter: GenericPagingAdapter<EpisodesQuery.Result>(DiffItem()) {


    override fun onBindViewHolder(
        holder: GenericViewHolder<EpisodesQuery.Result>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = if (getItem(position) is EpisodesQuery.Result) {
        R.layout.episode_item_row
    } else {
        R.layout.episode_item_row
    }

    class DiffItem : DiffUtil.ItemCallback<EpisodesQuery.Result>() {
        override fun areItemsTheSame(oldItem: EpisodesQuery.Result, newItem: EpisodesQuery.Result): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: EpisodesQuery.Result, newItem: EpisodesQuery.Result): Boolean {
            return oldItem == newItem
        }
    }

}
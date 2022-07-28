package com.egeperk.rickandmorty_final.adapter


import androidx.recyclerview.widget.DiffUtil
import com.egeperk.rickandmorty_final.R
import com.example.rnm_mvvm.CharactersQuery

class PagedAdapter: GenericPagingAdapter<CharactersQuery.Result>(DiffItem()) {

    override fun getItemViewType(position: Int): Int = if (getItem(position) is CharactersQuery.Result) {
        R.layout.char_row
    } else {
        R.layout.char_row
    }

    class DiffItem : DiffUtil.ItemCallback<CharactersQuery.Result>() {
        override fun areItemsTheSame(oldItem: CharactersQuery.Result, newItem: CharactersQuery.Result): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: CharactersQuery.Result, newItem: CharactersQuery.Result): Boolean {
            return oldItem == newItem
        }
    }
}

package com.egeperk.rickandmorty_final.adapter.pagingadapter

import androidx.recyclerview.widget.DiffUtil
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.model.Character

class FilterAdapter(private val onItemClickListener: OnItemClickListener): GenericPagingAdapter<Character>(
    DiffItem()) {

    override fun onBindViewHolder(holder: GenericViewHolder<Character>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }

    override fun getItemViewType(position: Int): Int = if (getItem(position) is Character) {
        R.layout.option_row
    } else {
        R.layout.option_row
    }

    class DiffItem : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.charName == newItem.charName
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
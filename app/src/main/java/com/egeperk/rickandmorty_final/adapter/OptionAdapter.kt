package com.egeperk.rickandmorty_final.adapter

import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.model.Character

class OptionAdapter(val onItemClickListener: OnItemClickListener) : BaseAdapter<Character>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.charName == newItem.charName
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position: Int) = if (getItem(position) is Character) {
        R.layout.option_row
    } else {
        R.layout.option_row
    }

    override fun onBindViewHolder(holder: BindingViewHolder<Character>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
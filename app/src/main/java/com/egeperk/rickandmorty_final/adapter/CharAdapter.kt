package com.egeperk.rickandmorty_final.adapter

import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.model.Character
import com.example.rnm_mvvm.CharactersQuery

class CharAdapter() : BaseAdapter<CharactersQuery.Result>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<CharactersQuery.Result>() {
        override fun areItemsTheSame(oldItem: CharactersQuery.Result, newItem: CharactersQuery.Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharactersQuery.Result, newItem: CharactersQuery.Result): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position: Int) = if (getItem(position) is CharactersQuery.Result) {
        R.layout.char_row
    } else {
        R.layout.char_row
    }

    override fun onBindViewHolder(holder: BindingViewHolder<CharactersQuery.Result>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, "$position", Toast.LENGTH_SHORT).show()
        }
    }
}
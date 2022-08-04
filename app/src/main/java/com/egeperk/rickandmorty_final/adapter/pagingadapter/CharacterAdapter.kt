package com.egeperk.rickandmorty_final.adapter.pagingadapter

import android.os.Handler
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.util.Constants.VIEW_DELAY
import com.example.rnm_mvvm.CharactersQuery
import com.facebook.shimmer.ShimmerFrameLayout

class CharacterAdapter(private val listener: OnItemClickListener): GenericPagingAdapter<CharactersQuery.Result>(DiffItem()) {

    override fun onBindViewHolder(
        holder: GenericViewHolder<CharactersQuery.Result>,
        position: Int
    ) {
        holder.bind(getItem(position))
        val shimmerLy = holder.itemView.findViewById<ShimmerFrameLayout>(R.id.shimmer_ly)
        val mainLy = holder.itemView.findViewById<CardView>(R.id.main_ly)
        Handler().postDelayed({
            mainLy.isVisible = true
            shimmerLy.isVisible = false
        },VIEW_DELAY)
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}

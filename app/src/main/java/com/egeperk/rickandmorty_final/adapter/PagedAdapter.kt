package com.egeperk.rickandmorty_final.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.BR
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.databinding.CharRowBinding
import com.egeperk.rickandmorty_final.model.Character
import com.example.rnm_mvvm.CharactersQuery

class PagedAdapter: PagingDataAdapter<CharactersQuery.Result,PagedAdapter.ViewHolder>(DiffUtilx) {
    override fun onBindViewHolder(holder: PagedAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
        //holder.view.itemRowName.text = item?.characters?.results?.get(position)?.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CharRowBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: CharRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Any?) {
            binding.setVariable(BR.item, obj)
            binding.executePendingBindings()
        }
    }

    object DiffUtilx: DiffUtil.ItemCallback<CharactersQuery.Result>() {
        override fun areItemsTheSame(oldItem: CharactersQuery.Result, newItem: CharactersQuery.Result): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharactersQuery.Result, newItem: CharactersQuery.Result): Boolean {
            return oldItem == newItem
        }
    }
}

package com.egeperk.rickandmorty_final.adapter.pagingadapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.BR

abstract class GenericPagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>): PagingDataAdapter<T, GenericPagingAdapter.GenericViewHolder<T>>(diffCallback) {

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), viewType, parent, false)
        return GenericViewHolder(binding)

    }

    class GenericViewHolder<T>(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Any?) {
            binding.setVariable(BR.item, obj)
            binding.executePendingBindings()
        }
    }
}

package com.egeperk.rickandmorty_final.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.BR

abstract class BaseAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseAdapter.BindingViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, viewType, parent, false)
        return BindingViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: BindingViewHolder<T>,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class BindingViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(
        binding.root) {
        fun bind(obj: Any?) {
            binding.setVariable(BR.item, obj)
            binding.executePendingBindings()
        }
    }
}
package com.egeperk.rickandmorty_final.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.databinding.ItemLoadingBinding

class ItemLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ItemLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.binding.apply {
            errorText.isVisible = loadState !is LoadState.Loading
        }

        if (loadState is LoadState.Error) {
            holder.binding.apply {
                errorText.text = loadState.error.localizedMessage
                loadStateRetry.isVisible = true
            }
        }
        holder.binding.loadStateRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }
}
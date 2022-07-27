package com.egeperk.rickandmorty_final.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.databinding.ItemLoadingBinding

class ItemLoadStateAdapter: LoadStateAdapter<ItemLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(val binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        if (loadState is LoadState.Loading) {
            holder.binding.apply {
                progressLoading.isVisible = true
                errorText.isVisible = false
            }
        } else {
            holder.binding.progressLoading.isVisible = false
        }
        if (loadState is LoadState.Error) {
            holder.binding.apply {
                progressLoading.isVisible = false
                errorText.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

}
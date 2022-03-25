package com.egeperk.rickandmorty_final.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.BR


class GenericAdapter<T>(
    private val dataList: List<T>,
    private val layoutId: Int,
   ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPosition = -1
    //private val listener: OnItemClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, layoutId, parent, false
        )
        return GenericHolder(binding) }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: T = dataList[position]
        (holder as GenericHolder).bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class GenericHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Any?) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(selectedPosition: Int)
    }

}
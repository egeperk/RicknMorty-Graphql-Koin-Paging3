package com.egeperk.rickandmorty_final.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.databinding.OptionRowBinding
import com.egeperk.rickandmorty_final.model.Character

class DialogAdapter(
    private val filterList: ArrayList<Character>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DialogAdapter.ViewHolder>() {

    private var selectedPosition = -1

    class ViewHolder(val binding: OptionRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogAdapter.ViewHolder {
        val binding = OptionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: DialogAdapter.ViewHolder, position: Int) {
        holder.binding.optionTextView.text = filterList[position].charName
        holder.binding.btnOptionImageView.setImageResource(if (selectedPosition == position) R.drawable.groupellipse1 else R.drawable.ellipse1)
        holder.binding.root.setOnClickListener {
            if (position != selectedPosition) {
                listener.onItemClick(position)
                selectedPosition = position
                notifyDataSetChanged()
            }
            else {
                selectedPosition = -1
                listener.onItemClick(2)
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(selectedPosition: Int)
    }

}
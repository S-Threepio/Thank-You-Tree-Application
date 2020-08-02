package com.example.thankyoutree.views.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thankyoutree.R
import com.example.thankyoutree.databinding.ListItemLayoutBinding
import com.example.thankyoutree.model.Note
import kotlinx.android.synthetic.main.list_item_layout.view.*

class CustomArrayAdapter :
    ListAdapter<Note, CustomArrayAdapter.ViewHolder>(CustomDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(var binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            binding.note = item
            binding.executePendingBindings()
        }
    }

    class CustomDiffUtils : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
    }


}
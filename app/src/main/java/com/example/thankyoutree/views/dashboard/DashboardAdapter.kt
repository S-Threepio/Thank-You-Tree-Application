package com.example.thankyoutree.views.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thankyoutree.databinding.DashboardItemLayoutBinding
import com.example.thankyoutree.model.Person

class DashboardAdapter(val dashBoardListener: DashBoardListener) :
    ListAdapter<Person, DashboardAdapter.ViewHolder>(DashboardDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(DashboardItemLayoutBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, dashBoardListener)
    }

    class ViewHolder(val binding: DashboardItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Person, dashBoardListener: DashBoardListener) {
            binding.person = item
            binding.clickListener = dashBoardListener
            binding.executePendingBindings()
        }
    }

    class DashboardDiffUtils : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
            oldItem == newItem
    }

    class DashBoardListener(val clickListener: (person: Person) -> Unit) {
        fun onClick(person: Person) = clickListener(person)
    }

}
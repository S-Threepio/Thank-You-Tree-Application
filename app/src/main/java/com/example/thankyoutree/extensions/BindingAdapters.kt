package com.example.thankyoutree.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.liveDataReponses.NotesResponse
import com.example.thankyoutree.views.notes.CustomArrayAdapter

@BindingAdapter("notesList")
fun bindRecyclerView(recyclerView: RecyclerView, notesResponse: NotesResponse?) {
    val adapter = recyclerView.adapter as CustomArrayAdapter
    adapter.submitList(notesResponse?.data?.notes?.toMutableList())
}
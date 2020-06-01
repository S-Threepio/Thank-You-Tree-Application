package com.example.thankyoutree.views.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.thankyoutree.views.add.AddNoteViewModel

@Suppress("UNCHECKED_CAST")
class AddNoteViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddNoteViewModel() as T
    }
}
package com.example.thankyoutree.views.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.thankyoutree.views.notes.NotesViewModel

@Suppress("UNCHECKED_CAST")
class LandingViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LandingViewModel() as T
    }
}
package com.example.thankyoutree.views.dashboard.humble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.thankyoutree.views.dashboard.helper.HelperViewModel
import com.example.thankyoutree.dashboard.helper.HumbleViewModel

@Suppress("UNCHECKED_CAST")
class HumbleViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HumbleViewModel() as T
    }
}
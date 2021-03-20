package com.example.thankyoutree.views.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.thankyoutree.databinding.FragmentShowNotesBinding
import com.example.thankyoutree.model.liveDataReponses.NotesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import kotlinx.android.synthetic.main.loader_layout.*

class NotesFragment : Fragment() {
    lateinit var notesViewModel: NotesViewModel
    lateinit var binding: FragmentShowNotesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Thank You Notes"
        binding = FragmentShowNotesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = notesViewModel
        binding.listViewId.adapter = CustomArrayAdapter()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel = ViewModelProvider(
            this
        ).get(NotesViewModel::class.java)
        notesViewModel.notesLiveData.observe(this, Observer {
            processResponse(it)
        })
    }

    private fun processResponse(response: NotesResponse) {
        when (response.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> {
                hideLoadingView()
            }
            Status.ERROR -> {
                hideLoadingView()
                Toast.makeText(
                    activity, "please check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                Log.v("api machine broke", response.error?.message.toString())
            }
        }
    }

    fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
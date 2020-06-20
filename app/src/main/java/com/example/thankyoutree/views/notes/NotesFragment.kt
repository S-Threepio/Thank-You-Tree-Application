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
import com.example.thankyoutree.R
import com.example.thankyoutree.model.liveDataReponses.NotesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import kotlinx.android.synthetic.main.fragment_show_notes.*
import kotlinx.android.synthetic.main.loader_layout.*

class NotesFragment : Fragment() {
    lateinit var notesViewModel: NotesViewModel
    lateinit var myAdapter: CustomArrayAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Thank You Notes")
        return inflater.inflate(R.layout.fragment_show_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel.callApi()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel = ViewModelProvider(
            this,
            NotesViewModelFactory()
        ).get(NotesViewModel::class.java)
        notesViewModel.notesLiveData.observe(this, Observer {
            processResponse(it)
        })
    }

    private fun processResponse(response: NotesResponse) {
        when (response.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> {
                response.data?.let {
                    hideLoadingView()
                    Log.v("boom", "notes working fine")
                    view?.apply {
                        myAdapter =
                            CustomArrayAdapter(
                                this.context,
                                0,
                                it.notes
                            )
                    }
                    list_view_id.adapter = myAdapter
                }
            }
            Status.ERROR -> {
                hideLoadingView()
                Toast.makeText(
                    activity, "please check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                Log.v("boom", response.error?.message.toString())
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
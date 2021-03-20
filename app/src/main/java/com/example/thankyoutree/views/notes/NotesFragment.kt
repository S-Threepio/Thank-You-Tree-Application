package com.example.thankyoutree.views.notes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.application.example.FilterDialog
import com.example.thankyoutree.R
import com.example.thankyoutree.databinding.FragmentShowNotesBinding
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.liveDataReponses.NamesResponse
import com.example.thankyoutree.model.liveDataReponses.NotesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.loader_layout.*

class NotesFragment : Fragment(), FilterDialog.FilterDialogListener {
    lateinit var notesViewModel: NotesViewModel
    lateinit var binding: FragmentShowNotesBinding
    var names = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        activity?.title = "Thank You Notes"
        binding = FragmentShowNotesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = notesViewModel
        binding.listViewId.adapter = CustomArrayAdapter()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filterName -> openFilter()
            R.id.clearFilter -> {
                notesViewModel.callNotesApi()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel = ViewModelProvider(
            this
        ).get(NotesViewModel::class.java)
        notesViewModel.notesLiveData.observe(this, Observer {
            processResponse(it)
        })
        notesViewModel.namesLiveData.observe(this, Observer {
            processNamesResponse(it)
        })

        notesViewModel.displayLiveData.observe(this, Observer {
            it.data?.notes?.apply {

            }
        })
    }

    private fun processNamesResponse(response: NamesResponse) {
        when (response.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> {
                // Clear views only in case of success.
                // since we have already moved to next screen,
                // it's less likely you'll find a glitch
                names = response.data?.names.toString()
                hideLoadingView()
            }
            Status.ERROR -> {
                hideLoadingView()
                Toast.makeText(
                    activity, "error loading names",
                    Toast.LENGTH_SHORT
                ).show()
                Log.v("api machine broke", response.error?.message.toString())
            }

        }
    }

    fun openFilter() {
        val filterDialog = FilterDialog()
        filterDialog.arguments = Bundle().apply { putString("names", names) }
        filterDialog.show(childFragmentManager, "example dialog")
    }

    private fun processResponse(response: NotesResponse) {
        when (response.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> {
                notesViewModel.callNamesApi()
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

    override fun applyTexts(fromText: String?, toText: String?) {

        var filteredNotes: List<Note>? =
            notesViewModel.notesLiveData.value?.data?.notes?.filter { note ->
                checkField(fromText, note.from) && checkField(toText, note.to)
            }

        if (filteredNotes.isNullOrEmpty()) {
            Snackbar.make(
                requireView(),
                "Sorry there are no notes for your selection",
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction("ok") { dismiss() }
                show()
            }

        }
        notesViewModel.apply {
            filteredNotes?.toTypedArray()?.let {
                displayLiveData.postValue(
                    success(
                        data = Notes(it)
                    )
                )
            }
        }
    }

    private fun checkField(field: String?, noteValue: String): Boolean =
        field.isNullOrEmpty() || (names.split(",").toMutableList()
            .contains(field) && field == noteValue)
}
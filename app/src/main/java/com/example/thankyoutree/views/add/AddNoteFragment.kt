package com.example.thankyoutree.views.add

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.thankyoutree.R
import com.example.thankyoutree.TreeBaseContract
import com.example.thankyoutree.databinding.FragmentAddNoteBinding
import com.example.thankyoutree.model.liveDataReponses.AddNotesResponse
import com.example.thankyoutree.model.liveDataReponses.NamesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.loader_layout.*

class AddNoteFragment : Fragment(),
    TreeBaseContract.View {

    lateinit var adapter: ArrayAdapter<String>
    lateinit var fromAdapter: ArrayAdapter<String>
    lateinit var toAdapter: ArrayAdapter<String>
    lateinit var fragmentAddNoteBinding: FragmentAddNoteBinding
    lateinit var addNoteViewModel: AddNoteViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNoteViewModel.callNamesApi()
    }

    fun initializeArrray(names: String, context: Context) {
        var listOfNames = names.split(",").toMutableList()
        var arrayNames: Array<String>? = listOfNames.toTypedArray()
        arrayNames?.let {
            adapter =
                ArrayAdapter<String>(
                    context,
                    R.layout.spinner_layout, arrayNames
                ).also {
                    it.setDropDownViewResource(R.layout.spinner_layout)
                }
            fromAdapter = adapter
            toAdapter = adapter
            fromAdapter.also {
                fromSpinner.apply {
                    setAdapter(it)
                    threshold = 1
                    setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            showDropDown()
                        } else {
                            text.let {
                                if (it.isBlank() || !(arrayNames.contains(it.toString()))) {
                                    setText("")
                                } else if (it.isNotBlank()) {
                                    listOfNames = arrayNames.toMutableList()
                                    listOfNames.remove(it.toString())
                                    var toArrayNames = listOfNames.toTypedArray()
                                    toAdapter = ArrayAdapter<String>(
                                        context,
                                        R.layout.spinner_layout,
                                        toArrayNames
                                    ).also {
                                        it.setDropDownViewResource(R.layout.spinner_layout)
                                        toSpinner.setAdapter(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            toAdapter.also {
                toSpinner.apply {
                    setAdapter(it)
                    threshold = 1
                    setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            showDropDown()
                        } else {
                            text.let {
                                if (!(arrayNames.contains(it.toString()))) {
                                    setText("")
                                } else if (it.isNotBlank()) {
                                    listOfNames = arrayNames.toMutableList()
                                    listOfNames.remove(it.toString())
                                    var fromArrayNames = listOfNames.toTypedArray()
                                    fromAdapter = ArrayAdapter<String>(
                                        context,
                                        R.layout.spinner_layout,
                                        fromArrayNames
                                    ).also {
                                        it.setDropDownViewResource(R.layout.spinner_layout)
                                        fromSpinner.setAdapter(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onClickAddNote(view: View) {
        hideSoftKeyboard(editNote)
        if (editNote.text.toString().isNotEmpty()) {
            addNoteViewModel.callApi(
                from = fromSpinner.text.toString(),
                noteData = editNote.text.toString(),
                to = toSpinner.text.toString()
            )
        }
    }

    protected fun hideSoftKeyboard(input: EditText) {
        val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(input.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addNoteViewModel = ViewModelProvider(
            this
        ).get(AddNoteViewModel::class.java)
        addNoteViewModel.addNoteLiveData.observe(this, Observer {
            processAddResponse(it)
        })
        addNoteViewModel.namesLiveData.observe(this, Observer {
            processNamesResponse(it)
        })
    }

    private fun processAddResponse(response: AddNotesResponse) {
        when (response.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> {
                // Clear views only in case of success.
                // since we have already moved to next screen,
                // it's less likely you'll find a glitch
                hideSoftKeyboard(editNote)
                clearViews()
                hideLoadingView()
                Snackbar.make(
                    requireView(),
                    "Your note was added successfully",
                    Snackbar.LENGTH_SHORT
                ).show()
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

    private fun processNamesResponse(response: NamesResponse) {
        when (response.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> {
                // Clear views only in case of success.
                // since we have already moved to next screen,
                // it's less likely you'll find a glitch
                hideLoadingView()
                initializeArrray(response.data?.names.toString(), requireContext())
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Add Your Note"
        fragmentAddNoteBinding =
            FragmentAddNoteBinding.inflate(inflater)
        fragmentAddNoteBinding.addNoteFragment = this
        return fragmentAddNoteBinding.root
    }

    companion object {
        private const val EMPTY_STRING = ""
        fun newInstance(names: String): AddNoteFragment {
            val frag = AddNoteFragment()
            val myArgs = Bundle()
            myArgs.putString("names", names)
            frag.arguments = myArgs
            return frag
        }
    }

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun clearViews() {
        fromSpinner.setText(EMPTY_STRING)
        toSpinner.setText(EMPTY_STRING)
        editNote.apply {
            setText(EMPTY_STRING)
            hint = getString(R.string.thank_you_note_label)
        }
    }
}


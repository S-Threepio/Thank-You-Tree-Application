package com.example.thankyoutree

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
import com.example.thankyoutree.extensions.addTo
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.model.Request
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.loader_layout.*
import retrofit2.Retrofit

class AddNoteFragment : Fragment(), TreeBaseContract.View {

    lateinit var adapter: ArrayAdapter<String>
    private val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    lateinit var fromAdapter: ArrayAdapter<String>
    lateinit var toAdapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var listOfNames = this.arguments?.getString("names")?.split(",")?.toMutableList()
        var arrayNames: Array<String>? = listOfNames?.toTypedArray()
        arrayNames?.let {
            adapter =
                ArrayAdapter<String>(view.context, R.layout.spinner_layout, arrayNames).also {
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
                                    listOfNames?.remove(it.toString())
                                    var toArrayNames = listOfNames?.toTypedArray() ?: arrayNames
                                    toAdapter = ArrayAdapter<String>(
                                        view.context,
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
                                    listOfNames?.remove(it.toString())
                                    var fromArrayNames = listOfNames?.toTypedArray() ?: arrayNames
                                    fromAdapter = ArrayAdapter<String>(
                                        view.context,
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
        addNoteButton.setOnClickListener {
            hideSoftKeyboard(editNote)
            if (editNote.text.toString().isNotEmpty()) {
                addNewNote(
                    fromSpinner.text.toString(),
                    editNote.text.toString(),
                    toSpinner.text.toString()
                )
            }
        }
    }

    protected fun hideSoftKeyboard(input: EditText) {
        val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(input.windowToken, 0)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Add Your Note"
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    private fun addNewNote(from: String, noteData: String, to: String) {
        var fromData = from
        var toData = to
        if (from.equals("")) {
            fromData = "-"
        }
        if (to.equals("")) {
            toData = "-"
        }

        val request = Request(toData, fromData, noteData)

        retrofitRepositoryImpl.create(NotesApi::class.java)
            .addNote(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoadingView()
            }
            .subscribe(
                {
                    activity?.replace(NotesFragment())
                    hideLoadingView()

                    // Clear views only in case of success.
                    // since we have already moved to next screen,
                    // it's less likely you'll find a glitch
                    clearViews()
                }, {
                    Log.v("error for adding a note", it.message ?: "No message from exception")
                    hideLoadingView()
                    Toast.makeText(
                        activity,
                        "please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ).addTo(compositeDisposable)
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


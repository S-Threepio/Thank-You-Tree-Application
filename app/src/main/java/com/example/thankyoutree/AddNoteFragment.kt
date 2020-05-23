package com.example.thankyoutree

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.model.Request
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.loader_layout.*
import retrofit2.Retrofit

class AddNoteFragment : Fragment(), TreeBaseContract.View {

    lateinit var adapter: ArrayAdapter<String>
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayNames: Array<String>? =
            this.arguments?.getString("names")?.split(",")?.toTypedArray()

        arrayNames?.let {
            adapter =
                ArrayAdapter<String>(view.context, R.layout.spinner_layout, arrayNames).also {
                    it.setDropDownViewResource(R.layout.spinner_layout)
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
                                    }
                                }
                            }
                        }
                    }
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
        return inflater.inflate(R.layout.activity_add_note, container, false)
    }

    private fun addNewNote(from: String, noteData: String, to: String) {
        var fromData = ""
        var toData = ""
        if (from.equals("")) {
            fromData = "-"
        }
        if (to.equals("")) {
            toData = "-"
        }

        val request = Request(toData, fromData, noteData)
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoadingView()
            }
            .subscribe(
                {
                    activity?.replace(NotesFragment())
                    hideLoadingView()
                }, {
                    Log.v("error for adding a note", it.message)
                    hideLoadingView()
                    Toast.makeText(
                        activity,
                        "please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
    }


    companion object {
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

    override fun onResume() {
        super.onResume()
        fromSpinner.setText("")
        toSpinner.setText("")
        editNote.setText("")
        editNote.setHint("Write your thank you note")
    }
}

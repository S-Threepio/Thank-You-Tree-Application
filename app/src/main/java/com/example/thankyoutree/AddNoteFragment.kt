package com.example.thankyoutree

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
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

class AddNoteFragment : Fragment(), TreeBaseContract.View, AdapterView.OnItemSelectedListener {

    lateinit var adapter: ArrayAdapter<String>
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayNames: Array<String>? =
            this.arguments?.getString("names")?.split(",")?.toTypedArray()
        arrayNames?.let {
            adapter = ArrayAdapter<String>(view.context, R.layout.spinner_layout, arrayNames)
            adapter.setDropDownViewResource(R.layout.spinner_layout)
            fromSpinner.adapter = adapter
            fromSpinner.setOnItemSelectedListener(this)
            toSpinner.adapter = adapter
            toSpinner.setOnItemSelectedListener(this)
        }

        addNoteButton.setOnClickListener {
            hideSoftKeyboard(editNote)
            if (editNote.text.toString().isNotEmpty()) {
                addNewNote(
                    fromSpinner.selectedItem.toString(),
                    editNote.text.toString(),
                    toSpinner.selectedItem.toString()
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
        activity?.setTitle("Add Your Note")
        return inflater.inflate(R.layout.activity_add_note, container, false)
    }

    private fun addNewNote(from: String, noteData: String, to: String) {
        val request = Request(to, from, noteData)
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoadingView()
            }
            .subscribe(
                {
                    activity?.replace(NotesFragment(),withStateLoss = false)
                    hideLoadingView()
                }, {
                    Log.v("boom", it.message)
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

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text = parent?.getItemAtPosition(position).toString()
    }

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}


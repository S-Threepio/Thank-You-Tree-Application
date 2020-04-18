package com.example.thankyoutree

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thankyoutree.model.Request
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.spinner_layout.view.*
import retrofit2.Retrofit


class AddNote : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        val arrayNames= arrayListOf<String>("-","one","two","three","one","two","three","one","two","three","one","two","three","one","two","three","one","two","three","one","two","three","one","two","three","one","two","three","one","two","three")
        val adapter = ArrayAdapter<String>(this,R.layout.spinner_layout,arrayNames)
        adapter.setDropDownViewResource(R.layout.spinner_layout)
        fromSpinner.adapter = adapter
        fromSpinner.setOnItemSelectedListener(this)
        toSpinner.adapter = adapter
        toSpinner.setOnItemSelectedListener(this)

        addNoteButton.setOnClickListener {
            if (editNote.text.toString().isNotEmpty()){
                addNewNote(fromSpinner.selectedItem.toString(),editNote.text.toString(),toSpinner.selectedItem.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loading.visibility=View.GONE
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text = parent!!.getItemAtPosition(position).toString()
    }

    private fun addNewNote(from: String, noteData: String, to: String) {
        val request  = Request(to,from,noteData)
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .addNote(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loading.visibility=View.VISIBLE
            }
            .subscribe(
                {
                    val intent = Intent(this, ShowNotes::class.java)
                    startActivity(intent)
                }, {
                    loading.visibility=View.GONE
                    Log.v("boom", it.message)
                    Toast.makeText(this,"please check your internet connection",Toast.LENGTH_SHORT).show()
                }
            )


    }
}

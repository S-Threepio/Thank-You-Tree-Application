package com.example.thankyoutree

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Request
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_show_notes.*
import kotlinx.android.synthetic.main.list_item_layout.*
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter

class AddNote : AppCompatActivity() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        addNoteButton.setOnClickListener {
            if (editNote.text.toString().isNotEmpty())
                addNewNote(edit_from.text.toString(),editNote.text.toString(),edit_to.text.toString())
        }
    }

    private fun addNewNote(from: String, noteData: String, to: String) {
        val request  = Request(to,from,noteData)
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .addNote(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val intent = Intent(this, ShowNotes::class.java)
                    startActivity(intent)
                }, {
                    Log.v("boom", it.message)
                }
            )


    }
}

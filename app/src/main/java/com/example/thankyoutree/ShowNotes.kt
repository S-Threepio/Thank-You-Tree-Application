package com.example.thankyoutree

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_show_notes.*
import retrofit2.Retrofit
import java.io.*

class ShowNotes : AppCompatActivity() {

    lateinit var notes: List<String>
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_notes)
        callApi()
    }

    fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingNotes.visibility = View.VISIBLE
            }
            .subscribe(
                {
                    Log.v("boom", "working fine")
                    var myAdapter: CustomArrayAdapter = CustomArrayAdapter(this, 0, it)
                    list_view_id.adapter = myAdapter
                    loadingNotes.visibility = View.GONE
                }, {
                    loadingNotes.visibility = View.GONE
                    Toast.makeText(
                        this, "please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.v("boom", it.message)
                }
            )
    }
}

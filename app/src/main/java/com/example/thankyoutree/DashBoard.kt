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
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_show_notes.*
import retrofit2.Retrofit
import java.io.*

class DashBoard : AppCompatActivity() {

    lateinit var notes: List<String>
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        helper.setOnClickListener {
            val intent = Intent(this, Humble::class.java)
            startActivity(intent)
        }
        humble.setOnClickListener {
            val intent = Intent(this, Helper::class.java)
            startActivity(intent)
        }
    }
}



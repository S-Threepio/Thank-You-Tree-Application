package com.example.thankyoutree

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_show_notes.*
import retrofit2.Retrofit
import java.io.*


class MainActivity : AppCompatActivity() {
    lateinit var names: List<String>
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var notes = ""
        add.setOnClickListener {
            callApi()
        }
        show.setOnClickListener {
            val intent = Intent(this, ShowNotes::class.java)
            startActivity(intent)
        }
        dashboard.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadingMain.visibility = View.GONE
    }


    private fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getListOfNames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingMain.visibility = View.VISIBLE
            }
            .subscribe(
                {
                    val intent = Intent(this, AddNote::class.java)
                    intent.putExtra("names",it.names)
                    startActivity(intent)
                }, {
                    loadingMain.visibility = View.GONE
                    Toast.makeText(
                        this, "please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.v("boom", it.message)
                }
            )
    }
}

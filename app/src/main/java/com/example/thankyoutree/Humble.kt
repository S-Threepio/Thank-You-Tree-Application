package com.example.thankyoutree

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Person
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_helper.*
import kotlinx.android.synthetic.main.activity_humble.*
import retrofit2.Retrofit

class Humble : AppCompatActivity() {

    lateinit var notes: List<String>
    lateinit var countings: Array<Person>
    var listOfNames: Array<String>? = null
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Most Thank You Given")
        setContentView(R.layout.activity_humble)
        callApi()
    }

    private fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingHumble.visibility = View.VISIBLE
            }
            .subscribe(
                {
                    Log.v("boom", "working fine")
                    var sortedList = makePersonList(it)
                    sortedList.sort()
                    var myAdapter: DashboardAdapter = DashboardAdapter(
                        this, 0, sortedList
                    )
                    humble_view.adapter = myAdapter
                    loadingHumble.visibility = View.GONE
                }, {
                    loadingHumble.visibility = View.GONE
                    Toast.makeText(
                        this, "please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.v("boom", it.message)
                }
            )
    }

    private fun makePersonList(notes: Array<Note>): Array<Person> {

        val countingData: ArrayList<Person> = ArrayList()
        var flag = false
        for (note in notes) {
            if (note.from == "-")
                continue
            if (countingData.isEmpty())
                countingData.add(Person(note.from, 0))
            flag = false
            for (person in countingData) {
                if (note.from == person.name) {
                    person.count = person.count + 1
                    flag = true
                }
            }
            if (!flag)
                countingData.add(Person(note.from, 1))
        }
        val arrayOfData = arrayOfNulls<Person>(countingData.size)
        return countingData.toArray(arrayOfData)
    }
}

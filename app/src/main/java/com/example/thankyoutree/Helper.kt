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
import retrofit2.Retrofit

class Helper : AppCompatActivity() {

    lateinit var notes: List<String>
    lateinit var countings: Array<Person>
    var listOfNames: Array<String>? = null
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Most Thank You Received")
        setContentView(R.layout.activity_helper)
        callApi()
    }

    private fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingHelper.visibility = View.VISIBLE
            }
            .subscribe(
                {
                    Log.v("boom", "working fine")
                    var sortedList = makePersonList(it)
                    sortedList.sort()
                    var myAdapter: DashboardAdapter = DashboardAdapter(
                        this, 0, sortedList
                    )
                    helper_view.adapter = myAdapter
                    loadingHelper.visibility = View.GONE
                }, {
                    loadingHelper.visibility = View.GONE
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
            if (note.to == "-")
                continue
            if (countingData.isEmpty())
                countingData.add(Person(note.to, 0))
            flag = false
            for (person in countingData) {
                if (note.to == person.name) {
                    person.count = person.count + 1
                    flag = true
                }
            }
            if (!flag)
                countingData.add(Person(note.to, 1))
        }
        val arrayOfData = arrayOfNulls<Person>(countingData.size)
        return countingData.toArray(arrayOfData)
    }
}

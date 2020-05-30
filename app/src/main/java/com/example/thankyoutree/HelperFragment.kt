package com.example.thankyoutree

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Person
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.dashboard_data.*
import kotlinx.android.synthetic.main.loader_layout.*
import retrofit2.Retrofit

class HelperFragment : Fragment(), TreeBaseContract.View {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    lateinit var myAdapter: DashboardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Most Thank You Received")
        callApi()

        data_list.setOnItemClickListener { parent, view, position, id ->
            myAdapter.getItem(position)?.apply {
                val name = name
                val count = count.toString()
                replace(ReceiptFragment.newInstance(name, count, "receiving"))
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dashboard_data, container, false)
    }

    private fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoadingView()
            }
            .subscribe(
                {
                    Log.v("boom", "working fine")
                    var sortedList = makePersonList(it)
                    sortedList.sort()
                    view?.let {
                        myAdapter = DashboardAdapter(
                            it.context, 0, sortedList
                        )
                    }

                    data_list.adapter = myAdapter
                    hideLoadingView()
                }, {
                    hideLoadingView()
                    Toast.makeText(
                        activity, "please check your internet connection",
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

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
package com.example.thankyoutree

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_show_notes.*
import kotlinx.android.synthetic.main.loader_layout.*
import retrofit2.Retrofit

class NotesFragment : Fragment() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    lateinit var myAdapter: CustomArrayAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Thank You Notes")
        return inflater.inflate(R.layout.fragment_show_notes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }

    fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoadingView()
            }
            .subscribe(
                { notes ->
                    hideLoadingView()
                    Log.v("boom", "notes working fine")
                    view?.let {
                        myAdapter = CustomArrayAdapter(it.context, 0, notes)
                    }
                    list_view_id.adapter = myAdapter
                }, {
                    hideLoadingView()
                    Toast.makeText(
                        activity, "please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.v("error", it.message)
                }
            )
    }

    fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
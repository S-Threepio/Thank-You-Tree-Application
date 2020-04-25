package com.example.thankyoutree

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loader_layout.*
import retrofit2.Retrofit

class LandingFragment : Fragment(), TreeBaseContract.View {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    lateinit var names: List<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var notes = ""
        add.setOnClickListener {
            showLoadingView()
            callApi()
        }
        show.setOnClickListener {
           // showLoadingView()
           activity?.replace(NotesFragment())
        }
        dashboard.setOnClickListener {
            activity?.replace(DashBoardFragment())
        }
    }

    private fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getListOfNames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    activity?.replace(AddNoteFragment.newInstance(it.names))
                }, {
                    (activity as MainActivity).hideLoadingView()
                    Toast.makeText(
                        activity, "please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.v("boom", it.message)
                }
            )
    }

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
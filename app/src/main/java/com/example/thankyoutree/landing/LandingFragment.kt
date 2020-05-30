package com.example.thankyoutree.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.thankyoutree.*
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.loader_layout.*
import retrofit2.Retrofit

class LandingFragment : Fragment(),
    TreeBaseContract.View {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    lateinit var names: List<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Thank You Tree")
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var notes = ""
        add.setOnClickListener {
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
            .doOnSubscribe {
                showLoadingView()
            }
            .subscribe(
                {
                    replace(
                        AddNoteFragment.newInstance(
                            it.names
                        )
                    )
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

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
package com.example.thankyoutree.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.thankyoutree.*
import com.example.thankyoutree.databinding.FragmentLandingBinding
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.loader_layout.*
import retrofit2.Retrofit

class LandingFragment : Fragment(),
    TreeBaseContract.View {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    lateinit var names: List<String>
    lateinit var viewModel: LandingViewModel
    lateinit var landingFragmentBinding: FragmentLandingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Thank You Tree")
        landingFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_landing, container, false)
        landingFragmentBinding.landingFragment = this
        viewModel = ViewModelProvider(this, ViewModelFactory()).get(LandingViewModel::class.java)
        return landingFragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var notes = ""
        show.setOnClickListener {
            // showLoadingView()
            activity?.replace(NotesFragment())
        }
        dashboard.setOnClickListener {
            activity?.replace(DashBoardFragment())
        }
    }


    fun onClickAddNote(view: View) {
        showLoadingView()
        viewModel.callApi()
        viewModel.liveDataSuccess.observe(this, Observer<NamesOfEmployees>() {
            replace(
                AddNoteFragment.newInstance(
                    it.names
                )
            )
        })
        viewModel.liveDataError.observe(this, Observer<Throwable>() {
            hideLoadingView()
            Toast.makeText(
                activity, "please check your internet connection",
                Toast.LENGTH_SHORT
            ).show()
            Log.v("boom", it.message)
        })
    }


    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
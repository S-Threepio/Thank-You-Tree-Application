package com.example.thankyoutree.views.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.thankyoutree.*
import com.example.thankyoutree.views.dashboard.DashBoardFragment
import com.example.thankyoutree.databinding.FragmentLandingBinding
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.model.liveDataReponses.NamesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.views.notes.NotesFragment
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import com.example.thankyoutree.views.add.AddNoteFragment
import kotlinx.android.synthetic.main.loader_layout.*
import retrofit2.Retrofit

class LandingFragment : Fragment(),
    TreeBaseContract.View {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    lateinit var names: List<String>
    lateinit var landingViewModel: LandingViewModel
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
        return landingFragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landingViewModel = ViewModelProvider(
            this,
            LandingViewModelFactory()
        ).get(LandingViewModel::class.java)
        landingViewModel.landingLiveData.observe(this, Observer<NamesResponse> {
            processResponse(it)
        })
    }

    private fun processResponse(response: NamesResponse) {
        when (response.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> {
                response.data?.let {
                    replace(
                        AddNoteFragment.newInstance(
                            it.names
                        )
                    )
                }
            }
            Status.ERROR -> {
                hideLoadingView()
                Toast.makeText(
                    activity, "please check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                Log.v("boom", response.error?.message.toString())
            }

        }
    }

    fun onClickAddNote(view: View) {
        landingViewModel.callApi()
    }

    fun onClickShowNotes(view: View) {
        activity?.replace(NotesFragment())
    }

    fun onClickDashboard(view: View) {
        activity?.replace(DashBoardFragment())
    }

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
package com.example.thankyoutree.views.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.thankyoutree.TreeBaseContract
import com.example.thankyoutree.databinding.FragmentLandingBinding
import com.example.thankyoutree.model.liveDataReponses.NamesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import kotlinx.android.synthetic.main.loader_layout.*

class LandingFragment : Fragment(),
    TreeBaseContract.View {
    lateinit var landingViewModel: LandingViewModel
    lateinit var landingFragmentBinding: FragmentLandingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Thank You Tree"
        landingFragmentBinding = FragmentLandingBinding.inflate(inflater)
        landingFragmentBinding.landingFragment = this
        return landingFragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landingViewModel = ViewModelProvider(
            this
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
                   this.findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToAddNoteFragment(it.names))
                }
            }
            Status.ERROR -> {
                hideLoadingView()
                Toast.makeText(
                    activity, "please check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                Log.v("api machine broke", response.error?.message.toString())
            }

        }
    }

    fun onClickAddNote(view: View) {
        landingViewModel.callApi()
    }

    fun onClickShowNotes(view: View) {
        this.findNavController()
            .navigate(LandingFragmentDirections.actionLandingFragmentToNotesFragment())
    }

    fun onClickDashboard(view: View) {
        this.findNavController()
            .navigate(LandingFragmentDirections.actionLandingFragmentToDashBoardFragment())
    }

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
package com.example.thankyoutree.views.dashboard.helper

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
import com.example.thankyoutree.databinding.DashboardDataBinding
import com.example.thankyoutree.model.liveDataReponses.PersonListResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.views.dashboard.DashboardAdapter
import kotlinx.android.synthetic.main.loader_layout.*

class HelperFragment : Fragment(),
    TreeBaseContract.View {
    lateinit var helperViewModel: HelperViewModel
    lateinit var binding: DashboardDataBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Most Thank You Received"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helperViewModel = ViewModelProvider(
            this
        ).get(HelperViewModel::class.java)
        helperViewModel.helperLiveData.observe(this, Observer {
            processResponse(it)
        })
    }

    private fun processResponse(response: PersonListResponse) {
        when (response.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> {
                response.data?.let {
                    hideLoadingView()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardDataBinding.inflate(inflater)
        binding.lifecycleOwner = this
        helperViewModel.helperLiveData.observe(viewLifecycleOwner, Observer {
            binding.personList = it
        })

        binding.dataList.adapter = DashboardAdapter(DashboardAdapter.DashBoardListener {
            this.findNavController().navigate(
                HelperFragmentDirections.actionHelperFragmentToReceiptFragment(
                    it.name,
                    it.count.toString(),
                    "receiving"
                )
            )
        })
        return binding.root
    }

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
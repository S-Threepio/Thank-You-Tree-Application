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
import com.example.thankyoutree.R
import com.example.thankyoutree.views.receipt.ReceiptFragment
import com.example.thankyoutree.TreeBaseContract
import com.example.thankyoutree.extensions.add
import com.example.thankyoutree.views.dashboard.DashboardAdapter
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.model.liveDataReponses.PersonListResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import kotlinx.android.synthetic.main.dashboard_data.*
import kotlinx.android.synthetic.main.loader_layout.*

class HelperFragment : Fragment(),
    TreeBaseContract.View {
    lateinit var myAdapter: DashboardAdapter
    lateinit var helperViewModel: HelperViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Most Thank You Received")
        helperViewModel.callApi()
        data_list.setOnItemClickListener { parent, view, position, id ->
            myAdapter.getItem(position)?.apply {
                val name = name
                val count = count.toString()
                add(
                    ReceiptFragment.newInstance(
                        name,
                        count,
                        "receiving"
                    )
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helperViewModel = ViewModelProvider(
            this,
            HelperViewModelFactory()
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
                    view?.apply {
                        myAdapter =
                            DashboardAdapter(
                                this.context, 0, it
                            )
                    }
                    data_list.adapter = myAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dashboard_data, container, false)
    }

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }
}
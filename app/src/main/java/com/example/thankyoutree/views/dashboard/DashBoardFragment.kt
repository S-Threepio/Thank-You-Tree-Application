package com.example.thankyoutree.views.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.thankyoutree.R
import com.example.thankyoutree.views.dashboard.helper.HelperFragment
import com.example.thankyoutree.views.dashboard.humble.HumbleFragment
import com.example.thankyoutree.databinding.FragmentDashboardBinding
import com.example.thankyoutree.extensions.replace

class DashBoardFragment : Fragment() {

    lateinit var dashboardBinding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("DashBoard")
        dashboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        dashboardBinding.dahboardFragment = this
        return dashboardBinding.root
    }

    fun onClickHelper(view: View) {
        this.findNavController()
            .navigate(DashBoardFragmentDirections.actionDashBoardFragmentToHelperFragment())
    }

    fun onClickHumble(view: View) {
        this.findNavController()
            .navigate(DashBoardFragmentDirections.actionDashBoardFragmentToHumbleFragment())
    }

}
package com.example.thankyoutree

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.thankyoutree.extensions.replace
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashBoardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helper.setOnClickListener {
            activity?.replace(HelperFragment())
        }
        humble.setOnClickListener {
            activity?.replace(HumbleFragment())
        }
    }
}
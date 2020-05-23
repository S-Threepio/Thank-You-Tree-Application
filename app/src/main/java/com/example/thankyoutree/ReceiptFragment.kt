package com.example.thankyoutree

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.dashboard_item_layout.*
import kotlinx.android.synthetic.main.fragment_receipt.*

class ReceiptFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var description = ""
        this.arguments?.get("journey").toString().let {
            if (it.equals("giving"))
                activity?.setTitle("Thank you notes Given")
            else
                activity?.setTitle("Thank you notes Received")
            description = it.toString()
        }
        val personName = this.arguments?.get("name")
        val countOfNotes = this.arguments?.get("count")
        name.setText("Congratulations $personName on $description $countOfNotes Thank you notes!!! ")
    }

    companion object {
        fun newInstance(name: String, count: String, journey: String): ReceiptFragment {
            val frag = ReceiptFragment()
            val myArgs = Bundle()
            myArgs.putString("name", name)
            myArgs.putString("count", count)
            myArgs.putString("journey", journey)
            frag.arguments = myArgs
            return frag
        }
    }
}
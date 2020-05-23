package com.example.thankyoutree

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.dashboard_item_layout.*
import kotlinx.android.synthetic.main.fragment_receipt.*

class ReceiptFragment : Fragment() {
    lateinit var personName: String
    lateinit var countOfNotes: String
    lateinit var shareSubject: String
    lateinit var sharebody: String
    lateinit var description: String

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu, inflater);
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.mShare -> {
                val intent = Intent(Intent.ACTION_SEND)
                shareSubject = "Thank you note share receipt"
                sharebody =
                    "Congratulations $personName on $description $countOfNotes Thank you notes!!! "
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
                intent.putExtra(Intent.EXTRA_TEXT, sharebody)
                startActivity(Intent.createChooser(intent, "share using"))

                Toast.makeText(
                    activity,
                    "You click on menu share",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.arguments?.get("journey").toString().let {
            if (it.equals("giving"))
                activity?.setTitle("Thank you notes Given")
            else
                activity?.setTitle("Thank you notes Received")
            description = it
        }
        personName = this.arguments?.get("name").toString()
        countOfNotes = this.arguments?.get("count").toString()
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
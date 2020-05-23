package com.example.thankyoutree

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_receipt.*
import java.io.File
import java.io.FileOutputStream


class ReceiptFragment : Fragment() {
    lateinit var personName: String
    lateinit var countOfNotes: String
    lateinit var shareSubject: String
    lateinit var description: String

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu, inflater);
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(
            activity,
            "Creating your receipt . . .",
            Toast.LENGTH_SHORT
        ).show()
        when (item.getItemId()) {
            R.id.mShare -> {
                val myBitmap = getBitmapFromView(receipt)
                val file = File(activity?.getExternalFilesDir(null)?.absolutePath, "/myImage.png")
                val fout = FileOutputStream(file)
                myBitmap?.compress(Bitmap.CompressFormat.PNG, 90, fout)
                fout.flush()
                fout.close()
                file.setReadable(true, false)
                val intent = Intent(Intent.ACTION_SEND)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context?.let {
                    intent.putExtra(
                        Intent.EXTRA_STREAM,
                        FileProvider.getUriForFile(
                            it,
                            it.getApplicationContext().getPackageName() + ".provider",
                            file
                        )
                    )
                }
                intent.type = "image/png"
                shareSubject = "Thank you note share receipt"
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
                startActivity(Intent.createChooser(intent, "share using"))
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

    private fun getBitmapFromView(view: View): Bitmap? { //Define a bitmap with the same size as the view
        val returnedBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) { //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else { //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

}
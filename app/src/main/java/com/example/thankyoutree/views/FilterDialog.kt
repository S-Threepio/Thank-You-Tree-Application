package com.example.application.example

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.thankyoutree.R

class FilterDialog : AppCompatDialogFragment() {
    private var listener: FilterDialogListener? = null
    lateinit var adapter: ArrayAdapter<String>
    lateinit var fromAdapter: ArrayAdapter<String>
    lateinit var toAdapter: ArrayAdapter<String>
    lateinit var editFromText: AutoCompleteTextView
    lateinit var editToText: AutoCompleteTextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.layout_dialog, null)

        builder.setView(view)
            .setTitle("Filter Notes")
            .setNegativeButton(
                "cancel"
            ) { dialogInterface, i -> }
            .setPositiveButton(
                "ok"
            ) { dialogInterface, i ->
                val from = editFromText.editableText.toString()
                val to = editToText.editableText.toString()
                listener?.applyTexts(from, to)
            }
        editFromText = view.findViewById<AutoCompleteTextView>(R.id.edit_from)
        editToText = view.findViewById<AutoCompleteTextView>(R.id.edit_to)
        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var names = arguments?.get("names").toString()
        initializeArrray(names, requireContext())
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    fun initializeArrray(names: String, context: Context) {
        var listOfNames = names.split(",").toMutableList()
        var arrayNames: Array<String>? = listOfNames.toTypedArray()
        arrayNames?.let {
            adapter =
                ArrayAdapter<String>(
                    context,
                    R.layout.spinner_layout, arrayNames
                ).also {
                    it.setDropDownViewResource(R.layout.spinner_layout)
                }
            fromAdapter = adapter
            toAdapter = adapter
            fromAdapter.also {
                editFromText.apply {
                    setAdapter(it)
                    threshold = 1
                    setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            showDropDown()
                        } else {
                            text.let {
                                if (it.isBlank() || !(arrayNames.contains(it.toString()))) {
                                    setText("")
                                } else if (it.isNotBlank()) {
                                    listOfNames = arrayNames.toMutableList()
                                    listOfNames.remove(it.toString())
                                    var toArrayNames = listOfNames.toTypedArray()
                                    toAdapter = ArrayAdapter<String>(
                                        context,
                                        R.layout.spinner_layout,
                                        toArrayNames
                                    ).also {
                                        it.setDropDownViewResource(R.layout.spinner_layout)
                                        editToText.setAdapter(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            toAdapter.also {
                editToText.apply {
                    setAdapter(it)
                    threshold = 1
                    setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            showDropDown()
                        } else {
                            text.let {
                                if (!(arrayNames.contains(it.toString()))) {
                                    setText("")
                                } else if (it.isNotBlank()) {
                                    listOfNames = arrayNames.toMutableList()
                                    listOfNames.remove(it.toString())
                                    var fromArrayNames = listOfNames.toTypedArray()
                                    fromAdapter = ArrayAdapter<String>(
                                        context,
                                        R.layout.spinner_layout,
                                        fromArrayNames
                                    ).also {
                                        it.setDropDownViewResource(R.layout.spinner_layout)
                                        editFromText.setAdapter(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as FilterDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling fragment must implement Callback interface")
        }
    }

    interface FilterDialogListener {
        fun applyTexts(from: String?, to: String?)
    }
}
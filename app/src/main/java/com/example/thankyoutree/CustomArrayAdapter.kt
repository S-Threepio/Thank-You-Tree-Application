package com.example.thankyoutree

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.thankyoutree.model.Note
import kotlinx.android.synthetic.main.list_item_layout.view.*

class CustomArrayAdapter(context: Context, layout: Int, val myList: Array<Note>) :
    ArrayAdapter<Note>(context, layout, myList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.list_item_layout,
            parent,
            false
        )

        val currentData = getItem(position)
        listItemView.to.text="To : "+currentData?.to
        listItemView.noteItem.text=currentData?.noteData
        listItemView.from.text="From : "+currentData?.from
        return listItemView
    }


}
package com.example.thankyoutree

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Person
import kotlinx.android.synthetic.main.dashboard_item_layout.view.*
import kotlinx.android.synthetic.main.list_item_layout.view.*

class DashboardAdapter(context: Context, layout: Int, val myList: Array<Person>) :
    ArrayAdapter<Person>(context, layout, myList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dashboardItemView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.dashboard_item_layout,
            parent,
            false
        )

        val currentData = getItem(position)
        dashboardItemView.nameOfThePerson.text=currentData?.name.toString()
        dashboardItemView.countOfThePerson.text=currentData?.count.toString()
        return dashboardItemView
    }


}
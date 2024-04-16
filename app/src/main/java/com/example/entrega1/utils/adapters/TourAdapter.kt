package com.example.entrega1.utils.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.entrega1.R

class TourAdapter(
    private val context: Activity,
    private val userName: ArrayList<String>,
    private val tourDescription: ArrayList<String>
) : ArrayAdapter<String>(context, R.layout.tour_item, userName) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.tour_item, null, true)

        val nameUser = rowView.findViewById(R.id.textUserNameOffer) as TextView
        val tourdesc = rowView.findViewById(R.id.tourTitle) as TextView

        nameUser.text = userName[position]
        tourdesc.text = tourDescription[position]

        return rowView
    }
}
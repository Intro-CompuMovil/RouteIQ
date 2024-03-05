package com.example.entrega1.utils.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.entrega1.R

class OffersAdapter(
    private val context: Activity,
    private val agencyName: ArrayList<String>,
    private val description: ArrayList<String>,
    private val price: ArrayList<String>
    ) : ArrayAdapter<String>(context, R.layout.custom_item, agencyName) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_item, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val priceView = rowView.findViewById(R.id.price) as TextView
        val subtitleText = rowView.findViewById(R.id.description) as TextView

        titleText.text = agencyName[position]
        subtitleText.text = description[position]
        priceView.text = price[position]

        return rowView
    }
}
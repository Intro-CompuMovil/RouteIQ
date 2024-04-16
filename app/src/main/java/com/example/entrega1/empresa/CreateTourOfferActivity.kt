package com.example.entrega1.empresa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.entrega1.R
import com.example.entrega1.utils.data.Tours
import com.example.entrega1.utils.schemas.User

class CreateTourOfferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tour_offer)

        val tourID: Int = intent.getBundleExtra("tourInfo")?.getInt("tourId")!!
        val user : User = intent.getBundleExtra("tourInfo")?.getParcelable("user")!!
        Log.i("TOUR", user.toString())
    }
}
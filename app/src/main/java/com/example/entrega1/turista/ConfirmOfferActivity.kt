package com.example.entrega1.turista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityConfirmOfferBinding
import com.example.entrega1.utils.data.Offers
import com.example.entrega1.utils.data.Places
import com.example.entrega1.utils.data.PlacesRender
import com.example.entrega1.utils.data.Tours
import com.example.entrega1.utils.roadRender.RoadRenderActivity
import com.example.entrega1.utils.schemas.Offer
import com.example.entrega1.utils.schemas.Place

class ConfirmOfferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmOfferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PlacesRender.clearPlaces()

        var offer : Offer? = null
        Offers.findById(intent.getStringExtra("offerId")!!) {
            offer = it

            Places.getPlacesFromUser(offer?.agency?.uid!!) { agencyPlaces ->
                if (agencyPlaces != null) {
                    PlacesRender.setPlaces(
                        agencyPlaces.filter { p ->
                            offer?.places?.find { offP -> offP == p.firebaseUid!! }  != null
                        } as ArrayList<Place>
                    )
                }
                //PlacesRender.setPlaces(offer?.places!!)
            }

            binding.offerCost.text = offer!!.amount.toString()
            binding.offerDate.text = offer!!.date.toString()
            binding.offerComments.text = offer!!.comments

            binding.offerShowRoute.setOnClickListener {
                startActivity( Intent(baseContext, RoadRenderActivity::class.java) )
            }

            binding.acceptOffer.setOnClickListener {
                Offers.acceptOfferById(intent.getStringExtra("offerId")!!)
                Tours.approveTourById(offer!!.tourId)
                binding.acceptOffer.isEnabled = false

                Toast.makeText(applicationContext, "Contacta con la agencia", Toast.LENGTH_SHORT).show()
            }

            binding.denyOffer.setOnClickListener {
                binding.denyOffer.isEnabled = false

                Toast.makeText(applicationContext, "Denegaste una oferta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
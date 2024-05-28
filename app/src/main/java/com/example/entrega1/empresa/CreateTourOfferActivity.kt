package com.example.entrega1.empresa

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityCreateTourOfferBinding
import com.example.entrega1.utils.data.Offers
import com.example.entrega1.utils.data.Places
import com.example.entrega1.utils.data.PlacesRender
import com.example.entrega1.utils.data.Tours
import com.example.entrega1.utils.data.UserProvider
import com.example.entrega1.utils.misc.DatePicker
import com.example.entrega1.utils.roadRender.RoadRenderActivity
import com.example.entrega1.utils.schemas.Agency
import com.example.entrega1.utils.schemas.Offer
import com.example.entrega1.utils.schemas.Place
import com.example.entrega1.utils.schemas.Tour
import com.example.entrega1.utils.schemas.User
import java.util.Calendar
import java.util.Date

class CreateTourOfferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTourOfferBinding
    private var choosenDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlacesRender.clearPlaces()
        binding = ActivityCreateTourOfferBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val tourID: String = intent.getBundleExtra("tourInfo")?.getString("tourId")!!
        val user : User = UserProvider.actualUser!!

        var tour: Tour?
        Tours.getById(tourID) {
            tour = it
            binding.tourTitle.text = tour?.title
            binding.tourDesc.text = tour?.description
        }

        binding.chooseDate.setOnClickListener {
            val newFragment = DatePicker.newInstance { view, year, month, dayOfMonth ->
                choosenDate = Date(year, month, dayOfMonth)
                val arrString = choosenDate.toString().split(" ")
                binding.placedDate.text = "${arrString[0]} ${arrString[1]} ${arrString[2]}"
            }

            newFragment.show(supportFragmentManager, "datePicker")
        }

        Places.getPlacesFromUser(user) { userPlaces ->
            if (userPlaces != null) {
                for (place in userPlaces) {
                    val newCheck: CheckBox = CheckBox(this)
                    newCheck.setText("${place.title}  ${place.description}")
                    newCheck.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)

                    newCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (!isChecked) PlacesRender.deletePlace(place)
                        else PlacesRender.addPlace(place)

                        Log.i("TO RENDER", PlacesRender.printPlaces())
                    }

                    binding.placesOptions.addView(newCheck)
                }
            } else {
                Toast.makeText(this, "Ups, no tienes lugares", Toast.LENGTH_SHORT).show()
            }
        }


        binding.viewRoute.setOnClickListener {
            startActivity( Intent(baseContext, RoadRenderActivity::class.java) )
        }

        binding.sendOffer.setOnClickListener {

            if (binding.cost.text.toString() == "") {
                Toast.makeText(applicationContext, "Hay que colocar un costo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.comments.text.toString() == "") {
                Toast.makeText(applicationContext, "Hay que colocar un Comentario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (choosenDate == null) {
                Toast.makeText(applicationContext, "Hay que elegir una fecha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Offers.addOffer(
                Offer(
                    Agency(user.name!!, user.email!!, user.firebaseUid),
                    binding.cost.text.toString().toDouble(),
                    binding.comments.text.toString(),
                    choosenDate!!,
                    tourID,
                    PlacesRender.getPlacesUids(),
                    "0" ,// Este valor se cambia en el companion object,
                    false
                )
            )

            val intent = Intent(applicationContext, HomeEnterpriseActivity::class.java)
            startActivity(intent)
        }

    }
}
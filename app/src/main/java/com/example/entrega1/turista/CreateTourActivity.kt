package com.example.entrega1.turista

import android.app.ActivityManager.TaskDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityCreateTourBinding
import com.example.entrega1.databinding.ActivityCreateTourOfferBinding
import com.example.entrega1.utils.data.Tours
import com.example.entrega1.utils.data.UserProvider
import com.example.entrega1.utils.schemas.Tour
import com.example.entrega1.utils.schemas.User

class CreateTourActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTourBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTourBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val user = UserProvider.actualUser

        binding.createTour.setOnClickListener {
            if (binding.tourTitle.text.toString() == "") {
                Toast.makeText(applicationContext, "Debes poner un titulo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.tourDescription.text.toString() == "") {
                Toast.makeText(applicationContext, "Debes poner un titulo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Tours.addTour(
                Tour(
                    user!!,
                    binding.tourTitle.text.toString(),
                    binding.tourDescription.text.toString(),
                    "0", // Se cambia en el companion object
                    false
                )
            )

            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
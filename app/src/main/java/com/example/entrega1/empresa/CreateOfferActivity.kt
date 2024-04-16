package com.example.entrega1.empresa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityCreateOfferBinding


class CreateOfferActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateOfferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
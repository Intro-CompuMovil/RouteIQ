package com.example.entrega1.empresa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityCreateOfferBinding
import com.example.entrega1.utils.adapters.TourAdapter
import com.example.entrega1.utils.data.Tours
import com.example.entrega1.utils.data.UserProvider
import com.example.entrega1.utils.misc.NavInit
import com.example.entrega1.utils.schemas.Tour
import com.example.entrega1.utils.schemas.User
import kotlin.collections.ArrayList


class CreateOfferActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateOfferBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = UserProvider.actualUser!!

        toggle = NavInit().initNavigationBar(user, R.id.navViewEnterprise, R.id.drawerLayoutEnterprise, this)



        var tours: ArrayList<Tour>?
        Tours.watchTours {
            tours = it
            val usersNames : ArrayList<String> = ArrayList()
            val descriptions: ArrayList<String> = ArrayList()
            val mappedIds : ArrayList<String> = ArrayList()
            for (tour in tours!!) {
                if (!tour.approved) {
                    usersNames.add(tour.user.name.toString())
                    descriptions.add(tour.title)
                    mappedIds.add(tour.id)
                }
            }

            binding.listTours.adapter = TourAdapter(this, usersNames, descriptions)
            binding.listTours.setOnItemClickListener { parent, view, position, id ->
                val intent = Intent(applicationContext, CreateTourOfferActivity::class.java)
                val bundle = Bundle()
                bundle.putString("tourId", mappedIds[position])
                intent.putExtra("tourInfo", bundle)
                startActivity( intent )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
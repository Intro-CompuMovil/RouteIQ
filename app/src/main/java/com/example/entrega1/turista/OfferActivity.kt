package com.example.entrega1.turista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.entrega1.R
import com.example.entrega1.utils.adapters.OffersAdapter
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.data.Offers
import com.example.entrega1.utils.data.Tours
import com.example.entrega1.utils.misc.NavInit
import com.example.entrega1.utils.schemas.User

class OfferActivity : AppCompatActivity() {

    private lateinit var toggleButton: ActionBarDrawerToggle;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer)


        var user = intent.getParcelableExtra<User>("user")

        if (user == null) {
            user = LoginStub.anonymousUser
            Log.i("USER HUELLA", "El usuario se ha loggeado con la huella, hay que hacer eso")
        }

        user.email?.let { Log.i("USER", it) }


        toggleButton = NavInit().initNavigationBar(user,
            R.id.navViewOffer,
            R.id.drawerLayoutOffer, this)


        val listOffers = findViewById<ListView>(R.id.listOffers)
        val createTour = findViewById<Button>(R.id.createTourButton)

        val agencyArr = ArrayList<String>()
        val descriptionArr = ArrayList<String>()
        val priceArr = ArrayList<String>()
        val mappedIds = ArrayList<Int>()
        //Offers.seed() // Añade datos de pruieba

        Log.i("OFFER", Offers.offers.toString())

        for (offer in Offers.offers) {
            val tour = Tours.getById(offer.tourId)
            if (tour?.user?.email == user.email && !offer.accepted) {
                agencyArr.add(offer.agency.name)
                descriptionArr.add(offer.comments)
                priceArr.add(offer.amount.toString())
                mappedIds.add(offer.id)
            }
        }

        val adapter = OffersAdapter(this, agencyArr, descriptionArr, priceArr)
        listOffers.adapter = adapter

        createTour.setOnClickListener {
            val intent = Intent(applicationContext, CreateTourActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

        listOffers.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, ConfirmOfferActivity::class.java)
            intent.putExtra("offerId", mappedIds[position])
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
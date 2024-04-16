package com.example.entrega1.utils.misc

import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.entrega1.turista.AffiliateActivity
import com.example.entrega1.turista.HomeActivity
import com.example.entrega1.turista.LocationActivity
import com.example.entrega1.turista.OfferActivity
import com.example.entrega1.R
import com.example.entrega1.empresa.CreateOfferActivity
import com.example.entrega1.empresa.CreatePlaceActivity
import com.example.entrega1.empresa.HomeEnterpriseActivity
import com.example.entrega1.turista.MapTouristActivity
import com.example.entrega1.utils.schemas.User
import com.google.android.material.navigation.NavigationView

class NavInit {

    fun initNavigationBar(
        user : User,
        navViewId: Int,
        drawerLayoutId: Int,
        context: AppCompatActivity
    ) : ActionBarDrawerToggle {

        val drawerLayout = context.findViewById<DrawerLayout>(drawerLayoutId)
        val navView = context.findViewById<NavigationView>(navViewId)

        val navigationViewHeader = navView.getHeaderView(0)
        val usernameHeader = navigationViewHeader.findViewById<TextView>(R.id.usernameHeader)
        val emailHeader = navigationViewHeader.findViewById<TextView>(R.id.emailHeader)

        usernameHeader.text = user.name
        emailHeader.text = user.email


        val toggleButton = ActionBarDrawerToggle(
            context,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggleButton)
        toggleButton.syncState()

        context.supportActionBar!!.setDisplayHomeAsUpEnabled(true) // Cambia entre el boton y la actividad principal

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeIcon -> {
                    val intent = Intent(context.applicationContext, HomeActivity::class.java)
                    intent.putExtra("user", user)
                    context.startActivity( intent )
                }
                R.id.offerIcon -> {
                    val intent = Intent(context.applicationContext, OfferActivity::class.java)
                    intent.putExtra("user", user)
                    context.startActivity( intent )
                }
                R.id.mapIcon -> {
                    val intent = Intent(context.applicationContext, MapTouristActivity::class.java)
                    intent.putExtra("user", user)
                    context.startActivity( intent )
                }
                R.id.affiliatesIcon -> {
                    val intent = Intent(context.applicationContext, AffiliateActivity::class.java)
                    intent.putExtra("user", user)
                    context.startActivity( intent )
                }

                R.id.cameraIcon -> {
                    val intent = Intent(context.applicationContext, LocationActivity::class.java)
                    intent.putExtra("user", user)
                    context.startActivity( intent )
                }

                R.id.homeIconEnterprise -> {
                    val intent = Intent(context.applicationContext, HomeEnterpriseActivity::class.java)
                    intent.putExtra("user", user)
                    context.startActivity( intent )
                }

                R.id.createPlace -> {
                    val intent = Intent(context.applicationContext, CreatePlaceActivity::class.java)
                    intent.putExtra("user", user)
                    context.startActivity( intent )
                }

                R.id.crearOferta -> {
                    val intent = Intent(context.applicationContext, CreateOfferActivity::class.java)
                    intent.putExtra("user", user)
                    context.startActivity( intent )
                }
            }

            true
        }

        return toggleButton
    }

}
package com.example.entrega1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggleButton: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)

        // Se supone que se debe ver con el toolbar por default
        //Cambiar en el layout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toggleButton = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggleButton)
        toggleButton.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Cambia entre el boton y la actividad principal

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> Toast.makeText(applicationContext, "Clicked Item 1", Toast.LENGTH_SHORT).show()
                R.id.item2 -> Toast.makeText(applicationContext, "Clicked Item 2", Toast.LENGTH_SHORT).show()
                R.id.item3 -> Toast.makeText(applicationContext, "Clicked Item 3", Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
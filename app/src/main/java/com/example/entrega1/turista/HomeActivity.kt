package com.example.entrega1.turista

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.entrega1.R
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.data.UserProvider
import com.example.entrega1.utils.misc.NavInit
import com.example.entrega1.utils.schemas.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class HomeActivity : AppCompatActivity() {
    private lateinit var toggleButton: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var user = UserProvider.actualUser

        if (user == null) {
            // Maneja el caso en que el usuario sea anónimo
            user = User("anonymous@example.com", null, "Anonymous", "user")
            Log.i("USER HUELLA", "El usuario se ha loggeado con la huella, hay que hacer eso")
        }

        toggleButton = NavInit().initNavigationBar(user, R.id.navView, R.id.drawerLayout, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
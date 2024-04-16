package com.example.entrega1.empresa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityHomeEnterpriseBinding
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.misc.NavInit
import com.example.entrega1.utils.schemas.User

class HomeEnterpriseActivity : AppCompatActivity() {
    private lateinit var toggleButton: ActionBarDrawerToggle

    private lateinit var binding : ActivityHomeEnterpriseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeEnterpriseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var user = intent.getParcelableExtra<User>("user")
        Log.i("USERRRR", user.toString())
        if (user == null) {
            user = LoginStub.anonymousUser
            Log.i("USER HUELLA", "El usuario se ha loggeado con la huella, hay que hacer eso")
        }
        toggleButton = NavInit().initNavigationBar(user, R.id.navViewEnterprise, R.id.drawerLayoutEnterprise, this)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.infoEnterprise.text = "Que gusto tenerte de vuelta. El nombre de tu empresa es: " + user.name
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
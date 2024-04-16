package com.example.entrega1.turista

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.entrega1.R
import com.example.entrega1.utils.data.Agencies
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.misc.NavInit
import com.example.entrega1.utils.schemas.User

class AffiliateActivity : AppCompatActivity() {

    private lateinit var toggleButton : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affiliate)

        var user = intent.getParcelableExtra<User>("user")

        if (user == null) {
            user = LoginStub.anonymousUser
            Log.i("USER HUELLA", "El usuario se ha loggeado con la huella, hay que hacer eso")
        }

        user.email?.let { Log.i("USER", it) }

        toggleButton = NavInit().initNavigationBar(user,
            R.id.navViewAffiliate,
            R.id.drawerLayoutAffiliate, this)

        renderizarMensajes()
    }

    private fun renderizarMensajes() {

        for ((index, agencia) in Agencies.agenciasDeViaje.withIndex()) {
            val logoId = resources.getIdentifier("logo${index + 1}", "id", packageName)
            val mensajeId = resources.getIdentifier("mensaje${index + 1}", "id", packageName)

            // Configurar imágenes y textos en los ImageView y TextView correspondientes
            findViewById<ImageView>(logoId).setImageResource(obtenerLogoId(index + 1))
            findViewById<TextView>(mensajeId).text = agencia.message
        }
    }

    private fun obtenerLogoId(index: Int): Int {
        return when (index) {
            1 -> R.drawable.home
            2 -> R.drawable.home
            3 -> R.drawable.home
            else -> throw IllegalArgumentException("Índice de logo inválido")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
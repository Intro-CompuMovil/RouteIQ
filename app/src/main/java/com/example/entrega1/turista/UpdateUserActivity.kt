package com.example.entrega1.turista

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityUpdateUserBinding
import com.example.entrega1.empresa.HomeEnterpriseActivity
import com.example.entrega1.login.MainActivity
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.data.UserProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdateUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var user = UserProvider.actualUser

        binding.createemail.setText(user?.email)
        binding.createName.setText(user?.name)

        binding.createAccountButton.setOnClickListener {
            val name = binding.createName.text.toString()
            val password = binding.createpassword.text.toString()
            val confirmPassword = binding.createconfirmPassword.text.toString()
            val email = binding.createemail.text.toString()

            if (name == "") {
                Toast.makeText(applicationContext, "Debes actualizar el nombre", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password == "") {
                Toast.makeText(
                    applicationContext,
                    "Debes actualizar la constraseña",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (confirmPassword == "") {
                Toast.makeText(
                    applicationContext,
                    "Debes confirmar la contraseña",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(
                    applicationContext,
                    "La confirmacion debe coincidir",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (email == "") {
                Toast.makeText(applicationContext, "Debes actualizar el email", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            Toast.makeText(baseContext, "Actualizando usuario...", Toast.LENGTH_SHORT).show()
            LoginStub.updateUser(Firebase.auth.currentUser!!, email, name, password, user!!) {
                if (it) {
                    LoginStub.logoutUser()
                    UserProvider.actualUser = null

                    Toast.makeText(baseContext, "Confirma tu correo.", Toast.LENGTH_SHORT).show()
                    Thread.sleep(1_000)

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity( intent )
                } else {
                    Toast.makeText(applicationContext, "Algo salió mal!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
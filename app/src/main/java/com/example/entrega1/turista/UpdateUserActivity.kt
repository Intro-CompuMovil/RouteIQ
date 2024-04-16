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
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.schemas.User

class UpdateUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var user = intent.getParcelableExtra<User>("user")

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

            user = LoginStub.updateUserByEmail(user?.email!!, email, name, password)

            val intent : Intent
            if (user?.type == "Empresa") {
                intent = Intent(applicationContext, HomeEnterpriseActivity::class.java)
            } else {
                intent = Intent(applicationContext, HomeActivity::class.java)
            }

            intent.putExtra("user", user)
            startActivity(intent)
        }
    }
}
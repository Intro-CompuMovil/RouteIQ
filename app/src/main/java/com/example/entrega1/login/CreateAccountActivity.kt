package com.example.entrega1.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.entrega1.R
import com.example.entrega1.utils.data.LoginStub

class CreateAccountActivity : AppCompatActivity() {

    lateinit var typeUserSelected: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val createEmail = findViewById<EditText>(R.id.createemail)
        val createPassword = findViewById<EditText>(R.id.createpassword)
        val createConfirmPassword = findViewById<EditText>(R.id.createconfirmPassword)
        val createName = findViewById<EditText>(R.id.createName)
        val toSignInButton = findViewById<Button>(R.id.toSignIn)
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        val typeUserSpinner = findViewById<Spinner>(R.id.userType)

        typeUserSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typeUserSelected = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        toSignInButton.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        createAccountButton.setOnClickListener {
            val email = createEmail.text.toString()
            val password = createPassword.text.toString()
            val confirmPassword = createConfirmPassword.text.toString()
            val name = createName.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
                Toast.makeText(applicationContext, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(applicationContext, "Ups, la confirmación de la contraseña difiere", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            LoginStub.createUser(email, password, name, typeUserSelected) { success ->
                if (success) {
                    Toast.makeText(applicationContext, "Cuenta creada, redirigiendo a login...", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, "Algo sucedió creando la cuenta", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

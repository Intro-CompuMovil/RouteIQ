package com.example.entrega1.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.entrega1.turista.HomeActivity
import com.example.entrega1.R
import com.example.entrega1.empresa.HomeEnterpriseActivity
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.data.UserProvider
import com.example.entrega1.utils.schemas.User
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext,
                        "Autenticate con tu usuario y contraseña", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Autenticate con tu usuario y contraseña", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)

        val signInButton = findViewById<Button>(R.id.iniciarSesion)
        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val createAccountButton = findViewById<Button>(R.id.createAccount)
        val anonymousLoginButton = findViewById<Button>(R.id.anonymousLogin)

        createAccountButton.setOnClickListener {
            startActivity(Intent(applicationContext, CreateAccountActivity::class.java))
        }

        signInButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            LoginStub.loginUser(email, password) { user ->
                if (user == null) {
                    Toast.makeText(applicationContext, "Ups, alguna de tu información no es correcta", Toast.LENGTH_SHORT).show()
                    emailInput.setText("")
                    passwordInput.setText("")
                } else {
                    UserProvider.setActualUser(user.firebaseUid!!){
                        if (it) {
                            val intent: Intent = if (user.type == "Turista") {
                                Intent(applicationContext, HomeActivity::class.java)
                            } else {
                                Intent(applicationContext, HomeEnterpriseActivity::class.java)
                            }
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "No pudimos colocarte como usuario actual", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        anonymousLoginButton.setOnClickListener {
            LoginStub.loginAnonymously { success, user ->
                if (success && user != null) {
                    UserProvider.actualUser = user
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Error al iniciar sesión anónimamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
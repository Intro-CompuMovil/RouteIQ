package com.example.entrega1.utils.data

import android.util.Log
import com.example.entrega1.utils.schemas.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.toxicbakery.bcrypt.Bcrypt

object LoginStub {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    fun createUser(email: String, password: String, name: String, type: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newUser = User(email, Bcrypt.hash(password, 10), name, type)
                    db.collection("users").document(email).set(newUser)
                        .addOnSuccessListener {
                            Log.i("LoginStub", "Usuario creado.")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("LoginStub", "Error creando el usuario n FireStore", e)
                            callback(false)
                        }
                } else {
                    Log.e("LoginStub", "Error creando el usuario en FirebaseAuth", task.exception)
                    callback(false)
                }
            }
    }

    fun loginUser(email: String, password: String, callback: (User?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("users").document(email).get()
                        .addOnSuccessListener { document ->
                            val user = document.toObject(User::class.java)
                            if (user != null && user.password != null && Bcrypt.verify(password, user.password)) {
                                Log.i("LoginStub", "Sesión iniciada.")
                                callback(user)
                            } else {
                                Log.e("LoginStub", "Falló la verificación de la contraseña")
                                callback(null)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("LoginStub", "Error buscando usuario de Firestore", e)
                            callback(null)
                        }
                } else {
                    Log.e("LoginStub", "Error entrando con FirebaseAuth", task.exception)
                    callback(null)
                }
            }
    }

    fun loginAnonymously(callback: (Boolean, User?) -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val anonymousUser = User("anonymous@example.com", null, "Anonymous", "user")
                    Log.i("LoginStub", "Sesión anónima iniciada.")
                    callback(true, anonymousUser)
                } else {
                    Log.e("LoginStub", "Error entrando anónimamente", task.exception)
                    callback(false, null)
                }
            }
    }
    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
    }
}
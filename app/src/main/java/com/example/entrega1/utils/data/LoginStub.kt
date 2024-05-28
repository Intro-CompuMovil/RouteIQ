package com.example.entrega1.utils.data

import android.util.Log
import com.example.entrega1.utils.schemas.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

object LoginStub {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: RealTimeCRUD = RealTimeCRUD()

    fun createUser(email: String, password: String, name: String, type: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.writeData("users/${auth.currentUser?.uid}",
                        User(
                            auth.currentUser?.uid,
                            email,
                            name,
                            type
                        )
                        ) {
                        if (it == null) {
                            Log.i("ERROR READING", "")
                            callback(false)
                        }
                        else callback(true)
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
                    db.readData<User>("users/${auth.currentUser?.uid}", callback)
                } else {
                    Log.e("LoginStub", "Error entrando con FirebaseAuth", task.exception)
                    callback(null)
                }
            }
    }

    fun updateUser(user: FirebaseUser, email: String, name: String, password: String, nativeUser: User ,callback: (Boolean) -> Unit) {
        user.verifyBeforeUpdateEmail(email).addOnCompleteListener { em ->
            if (em.isSuccessful) {
                user.updatePassword(password).addOnCompleteListener{ psw ->
                    if (psw.isSuccessful) {
                        nativeUser.email = email
                        nativeUser.name = name
                        db.writeData("users/${nativeUser.firebaseUid}", nativeUser) {
                            if (it != null) callback(true)
                            else callback(false)
                        }
                    }
                    else callback(false)
                }
            } else callback(false)
        }.addOnFailureListener {
            Log.i("[UPDATE USER]", it.toString())
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
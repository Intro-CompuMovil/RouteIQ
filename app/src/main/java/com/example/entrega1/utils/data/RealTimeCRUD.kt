package com.example.entrega1.utils.data

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class RealTimeCRUD {
    val dbReference: DatabaseReference = Firebase.database.reference

    inline fun <reified T> readData(path: String, crossinline callback: (T?) -> Unit) {
        dbReference.child(path).get().addOnSuccessListener {
            // Comes as hashmap
            val f = it.getValue<T>()
            callback(f)
        }.addOnFailureListener {
            Log.i("ERROR FIREBASE", it.toString())
            callback(null)
        }
    }

    suspend inline fun <reified T> readDataTask(path: String): T? {
        return dbReference.child(path).get().await().getValue<T>()
    }

    fun writeData(path: String, data: Any, callback: (Boolean?) -> Unit) {
        dbReference.child(path).setValue(data).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(null)
        }
    }

    fun watchData(path: String, listener: ValueEventListener) {
        dbReference.child(path).addValueEventListener(listener)
    }

    fun getUniqueKey(path: String) : String? {
        return dbReference.child(path).push().key
    }
}
package com.example.entrega1.utils.data

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

class StorageCRUD {

    private val storage : StorageReference = Firebase.storage.reference

    fun uploadFile(path: String, uri: Uri, callback: (Boolean?) -> Unit) {
        storage.child(path).putFile(uri).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(null)
        }
    }

    fun getFile(path: String, destinationUri: File, callback: (File?) -> Unit) {
        storage.child(path).getFile(destinationUri).addOnSuccessListener {
            callback(destinationUri)
        }.addOnFailureListener {
            callback(null)
        }
    }
}
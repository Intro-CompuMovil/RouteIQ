package com.example.entrega1.utils.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

object StorageUtil {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    fun uploadImage(uri: Uri, path: String, callback: (Boolean, String?) -> Unit) {
        val fileRef = storageRef.child(path)
        val uploadTask = fileRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                callback(true, uri.toString())
            }
        }.addOnFailureListener {
            callback(false, null)
        }
    }

    fun downloadImage(path: String, callback: (Boolean, Uri?) -> Unit) {
        val fileRef = storageRef.child(path)
        fileRef.downloadUrl.addOnSuccessListener { uri ->
            callback(true, uri)
        }.addOnFailureListener {
            callback(false, null)
        }
    }
}

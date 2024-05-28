package com.example.entrega1.utils.data

import android.util.Log
import com.example.entrega1.utils.schemas.User

class UserProvider {

    companion object {
        var actualUser : User? = null
        private val database : RealTimeCRUD = RealTimeCRUD()

        fun setActualUser(uid: String, callback: (Boolean) -> Unit) {
            database.readData<User>("users/${uid}") {
                if (it == null) callback(false)
                else {
                    actualUser = it
                    callback(true)
                }
            }
        }
    }

}
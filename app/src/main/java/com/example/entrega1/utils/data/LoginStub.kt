package com.example.entrega1.utils.data

import com.example.entrega1.utils.schemas.User
import com.toxicbakery.bcrypt.Bcrypt

class LoginStub {
    companion object Login {
        private val users : ArrayList<User> = ArrayList()
        private val anonymousPassword = Bcrypt.hash("123", 10)
        val anonymousUser : User = User("anonymous@example.com", anonymousPassword, "Anonymous")

        private fun findUser(email: String) : Boolean {
            return users.find {
                it.email == email
            } != null
        }

        fun createUser(email : String, password : String, name: String) : Boolean {
            if (findUser(email)) return false

            val newPassword = Bcrypt.hash(password, 10)

            users.add(User(email, newPassword, name))
            return true
        }

        fun loginUser(email: String, password: String) : User? {
            val user : User = users.find { it.email == email } ?: return null
            if (user.password == null) return null
            return if (!Bcrypt.verify(password, user.password)) null
            else user
        }
    }
}
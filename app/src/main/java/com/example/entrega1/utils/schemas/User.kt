package com.example.entrega1.utils.schemas

import android.os.Parcel
import android.os.Parcelable

data class User (
    val email: String?,
    val password: ByteArray?,
    val name: String?,
    val type: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        email = parcel.readString(),
        password = parcel.createByteArray(),
        name = parcel.readString(),
        type = parcel.readString()
    )

    override fun describeContents(): Int {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(email)
        dest.writeByteArray(password)
        dest.writeString(name)
        dest.writeString(type)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

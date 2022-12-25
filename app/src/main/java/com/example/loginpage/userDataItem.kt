package com.example.loginpage

import android.os.Parcel
import android.os.Parcelable

data class userDataItem(
    val id: Int,
    val password: String,
    val username: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(password)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<userDataItem> {
        override fun createFromParcel(parcel: Parcel): userDataItem {
            return userDataItem(parcel)
        }

        override fun newArray(size: Int): Array<userDataItem?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.konyvguilde

import android.os.Parcel
import android.os.Parcelable

data class WordModel(var id: Int = 0, var article: String , var name: String = "", var meaning: String = "" , var plural: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(article)
        parcel.writeString(name)
        parcel.writeString(meaning)
        parcel.writeString(plural)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WordModel> {
        override fun createFromParcel(parcel: Parcel): WordModel {
            return WordModel(parcel)
        }

        override fun newArray(size: Int): Array<WordModel?> {
            return arrayOfNulls(size)
        }
    }
}

package com.example.recipeapp

import android.os.Parcel
import android.os.Parcelable

data class RecipeInformation (
    val id: Int,
    val title: String,
    val image: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipeInformation> {
        override fun createFromParcel(parcel: Parcel): RecipeInformation {
            return RecipeInformation(parcel)
        }

        override fun newArray(size: Int): Array<RecipeInformation?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.deber03

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Pelicula(
    var nombre: String?,
    var fechaEstreno: Date,
    var cantidadActores: Int,
    var duracionMs: Double,
    var enCartelera: Boolean
) : Parcelable {
    override fun toString(): String {
        return "$nombre - $duracionMs"
    }
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        Date(parcel.readLong()),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte()
    ) {

    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeLong(fechaEstreno.time)
        parcel.writeInt(cantidadActores)
        parcel.writeDouble(duracionMs)
        parcel.writeByte(if (enCartelera) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pelicula> {
        override fun createFromParcel(parcel: Parcel): Pelicula {
            return Pelicula(parcel)
        }

        override fun newArray(size: Int): Array<Pelicula?> {
            return arrayOfNulls(size)
        }
    }
}

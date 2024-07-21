package com.example.deber03

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Director(
    var nombre: String?,
    var fechaNacimiento: Date,
    var peliculasDirigidas: Int,
    var calificacionIMDB: Double,
    var enRodaje: Boolean
) : Parcelable {
    override fun toString(): String {
        return "$nombre - $calificacionIMDB"
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
        parcel.writeLong(fechaNacimiento.time)
        parcel.writeInt(peliculasDirigidas)
        parcel.writeDouble(calificacionIMDB)
        parcel.writeByte(if (enRodaje) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Director> {
        override fun createFromParcel(parcel: Parcel): Director {
            return Director(parcel)
        }

        override fun newArray(size: Int): Array<Director?> {
            return arrayOfNulls(size)
        }
    }
}

package com.example.deber03

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Director(
    var idDirector: Int,
    var nombre: String?,
    var fechaNacimiento: Date,
    var peliculasDirigidas: Int,
    var calificacionIMDB: Double,
    var enRodaje: Boolean
)
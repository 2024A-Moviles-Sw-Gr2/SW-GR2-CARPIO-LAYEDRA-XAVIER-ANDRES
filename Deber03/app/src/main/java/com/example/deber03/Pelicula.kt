package com.example.deber03

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Pelicula(
    var idPelicula: Int,
    var nombre: String?,
    var fechaEstreno: Date,
    var cantidadActores: Int,
    var duracionMs: Double,
    var enCartelera: Boolean,
    var id_Director: Int
)
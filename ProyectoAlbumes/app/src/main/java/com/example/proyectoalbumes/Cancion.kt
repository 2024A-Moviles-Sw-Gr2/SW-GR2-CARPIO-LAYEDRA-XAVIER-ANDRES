package com.example.proyectoalbumes

data class Cancion(
    var title: String,
    var duracion: Float,
    val albumId: Int,
    val id: Int = 0
)

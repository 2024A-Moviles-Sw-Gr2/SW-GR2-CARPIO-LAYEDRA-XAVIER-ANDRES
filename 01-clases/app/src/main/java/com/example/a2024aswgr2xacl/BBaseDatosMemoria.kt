package com.example.a2024aswgr2xacl

class BBaseDatosMemoria {
    companion object{
        val arreBEntrenador= arrayListOf<BEntrenador>()
    }
    init {
        arreBEntrenador.add(BEntrenador(1,"Xavier","a@a.com"))
        arreBEntrenador.add(BEntrenador(2,"Andres","b@b.com"))
        arreBEntrenador.add(BEntrenador(3,"Carpio","c@c.com"))
    }
}
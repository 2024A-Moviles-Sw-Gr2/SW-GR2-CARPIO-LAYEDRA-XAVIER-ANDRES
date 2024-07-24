package com.example.a2024aswgr2xacl

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador
                .add(
                    BEntrenador(1,"Xavier","a@a.com")
                )

            arregloBEntrenador
                .add(
                    BEntrenador(2,"Carpio","b@b.com")
                )

            arregloBEntrenador
                .add(
                    BEntrenador(3,"Andres","c@c.com")
                )
        }
    }
}
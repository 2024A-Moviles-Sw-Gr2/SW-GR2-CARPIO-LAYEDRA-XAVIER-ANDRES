package com.example.deber03

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat

class BCrearPelicula : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bcrear_pelicula)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pelicula = intent.getParcelableExtra<Pelicula>("pelicula")
        val boton = findViewById<Button>(R.id.btn_crear_pelicula)
        if (pelicula != null) {
            val nombreEditText = findViewById<EditText>(R.id.input_nombrePelicula)
            val fechaEstreno = findViewById<EditText>(R.id.input_fechaEstreno)
            val cantidadActores = findViewById<EditText>(R.id.input_cantidadActores)
            val duracion = findViewById<EditText>(R.id.input_duracion)
            val enCartelera = findViewById<Switch>(R.id.input_enCartelera)
            boton.setText("Actualizar")

            nombreEditText.setText(pelicula.nombre)
            fechaEstreno.setText(SimpleDateFormat("dd/MM/yyyy").format(pelicula.fechaEstreno))
            cantidadActores.setText(pelicula.cantidadActores.toString())
            duracion.setText(pelicula.duracionMs.toString())
            enCartelera.isChecked = pelicula.enCartelera
            boton.setOnClickListener {
                actualizarPelicula(pelicula)
            }

        } else {

            boton.setOnClickListener {
                crearPelicula()
            }
        }
    }
    fun actualizarPelicula(pelicula: Pelicula){
        val nombre = findViewById<EditText>(R.id.input_nombrePelicula).text.toString()
        val fechaEstreno = SimpleDateFormat("dd/MM/yyyy").parse(findViewById<EditText>(R.id.input_fechaEstreno).text.toString())
        val cantidadActores = findViewById<EditText>(R.id.input_cantidadActores).text.toString().toInt()
        val duracionMs = findViewById<EditText>(R.id.input_duracion).text.toString().toDouble()
        val enCartelera = findViewById<Switch>(R.id.input_enCartelera).isChecked

        val index = BBaseDatosMemoria.arregloBPeliculas.indexOfFirst { it.nombre == pelicula.nombre }
        // Actualiza el director con los nuevos datos
        pelicula.nombre = nombre
        pelicula.fechaEstreno = fechaEstreno
        pelicula.cantidadActores = cantidadActores
        pelicula.duracionMs = duracionMs
        pelicula.enCartelera = enCartelera

        // Reemplaza el director en la base de datos en memoria

        if (index != -1) {
            BBaseDatosMemoria.arregloBPeliculas[index] = pelicula
        }
        setResult(RESULT_OK)
        finish()

    }
    fun crearPelicula(){
        // Obtén los datos de los campos
        val nombre = findViewById<EditText>(R.id.input_nombrePelicula).text.toString()
        val fechaEstreno = SimpleDateFormat("dd/MM/yyyy").parse(findViewById<EditText>(R.id.input_fechaEstreno).text.toString())
        val cantidadActores = findViewById<EditText>(R.id.input_cantidadActores).text.toString().toInt()
        val duracionMs = findViewById<EditText>(R.id.input_duracion).text.toString().toDouble()
        val enCartelera = findViewById<Switch>(R.id.input_enCartelera).isChecked

        val nuevaPelicula = Pelicula(nombre, fechaEstreno, cantidadActores, duracionMs, enCartelera)
        BBaseDatosMemoria.arregloBPeliculas.add(nuevaPelicula)

        // Devuelve un resultado indicando que se creó un nuevo director
        setResult(RESULT_OK)
        finish()
    }


}
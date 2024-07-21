package com.example.deber03

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class BCrearDirector : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bcrear_director)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_crear_director)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AlertDialog.Builder(this).setMessage("entrada a la pantalla crearDirector")

        //val director: Director? = intent.getParcelableExtra("director")
        val director = intent.getParcelableExtra<Director>("director")
        val boton = findViewById<Button>(R.id.btn_crear)


        if (director != null) {
            val nombreEditText = findViewById<EditText>(R.id.input_nombre)
            val fechaNacimientoEditText=findViewById<EditText>(R.id.inputFechaNac)
            val peliculasDirigidasEditText = findViewById<EditText>(R.id.input_peliculasDirigidas)
            val calificacionIMDBEditText = findViewById<EditText>(R.id.input_puntuacion)
            val enRodajeCheckBox = findViewById<Switch>(R.id.input_rodaje)
            boton.setText("Actualizar")

            nombreEditText.setText(director.nombre)
            fechaNacimientoEditText.setText(SimpleDateFormat("dd/MM/yyyy").format(director.fechaNacimiento))
            peliculasDirigidasEditText.setText(director.peliculasDirigidas.toString())
            calificacionIMDBEditText.setText(director.calificacionIMDB.toString())
            enRodajeCheckBox.isChecked = director.enRodaje
            boton.setOnClickListener {
                actualizarDirector(director)
            }

        } else {

            boton.setOnClickListener {
                crearDirector()
            }
        }
    }



    private fun actualizarDirector(director: Director) {

        val nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
        val fechaNacimiento = SimpleDateFormat("dd/MM/yyyy").parse(findViewById<EditText>(R.id.inputFechaNac).text.toString())
        val peliculasDirigidas = findViewById<EditText>(R.id.input_peliculasDirigidas).text.toString().toInt()
        val calificacionIMDB = findViewById<EditText>(R.id.input_puntuacion).text.toString().toDouble()
        val enRodaje = findViewById<Switch>(R.id.input_rodaje).isChecked

        val index = BBaseDatosMemoria.arregloBDirector.indexOfFirst { it.nombre == director.nombre }
        // Actualiza el director con los nuevos datos
        director.nombre = nombre
        director.fechaNacimiento = fechaNacimiento
        director.peliculasDirigidas = peliculasDirigidas
        director.calificacionIMDB = calificacionIMDB
        director.enRodaje = enRodaje

        // Reemplaza el director en la base de datos en memoria

        if (index != -1) {
            BBaseDatosMemoria.arregloBDirector[index] = director
        }
        setResult(RESULT_OK)
        finish()
    }


    private fun crearDirector() {
        // Obtén los datos de los campos
        val nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
        val fechaNacimiento = SimpleDateFormat("dd/MM/yyyy").parse(findViewById<EditText>(R.id.inputFechaNac).text.toString())
        val peliculasDirigidas = findViewById<EditText>(R.id.input_peliculasDirigidas).text.toString().toInt()
        val calificacionIMDB = findViewById<EditText>(R.id.input_puntuacion).text.toString().toDouble()
        val enRodaje = findViewById<Switch>(R.id.input_rodaje).isChecked

        val nuevoDirector = Director(nombre, fechaNacimiento, peliculasDirigidas, calificacionIMDB, enRodaje)
        BBaseDatosMemoria.arregloBDirector.add(nuevoDirector)

        // Devuelve un resultado indicando que se creó un nuevo director
        setResult(RESULT_OK)
        finish()
    }

}

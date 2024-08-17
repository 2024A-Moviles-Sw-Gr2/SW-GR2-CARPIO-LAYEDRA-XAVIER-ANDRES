package com.example.deber03

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat

class CrearDirector : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var directorId: Int = -1 // Variable para almacenar el ID del director si estamos en modo de edición

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_director)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Referencias a las vistas
        val inputNombre = findViewById<EditText>(R.id.input_nombre)
        val inputFechaNac = findViewById<EditText>(R.id.inputFechaNac)
        val inputPeliculasDirigidas = findViewById<EditText>(R.id.input_peliculasDirigidas)
        val inputPuntuacion = findViewById<EditText>(R.id.input_puntuacion)
        val inputRodaje = findViewById<Switch>(R.id.input_rodaje)
        val btnCrear = findViewById<Button>(R.id.btn_crear)

        // Verificar si la actividad se inició en modo de edición
        if (intent.hasExtra("directorId")) {
            directorId = intent.getIntExtra("directorId", -1)
            val director = databaseHelper.getDirector(directorId)

            if (director != null) {
                inputNombre.setText(director.nombre)
                inputFechaNac.setText(SimpleDateFormat("dd/MM/yyyy").format(director.fechaNacimiento))
                inputPeliculasDirigidas.setText(director.peliculasDirigidas.toString())
                inputPuntuacion.setText(director.calificacionIMDB.toString())
                inputRodaje.isChecked = director.enRodaje

                btnCrear.text = "Actualizar" // Cambiar el texto del botón
            }
        }

        // Listener para el botón de crear o actualizar director
        btnCrear.setOnClickListener {
            val nombre = inputNombre.text.toString()
            val fechaNacStr = inputFechaNac.text.toString()
            val peliculasDirigidas = inputPeliculasDirigidas.text.toString().toIntOrNull()
            val puntuacion = inputPuntuacion.text.toString().toDoubleOrNull()
            val enRodaje = inputRodaje.isChecked

            if (nombre.isEmpty() || fechaNacStr.isEmpty() || peliculasDirigidas == null || puntuacion == null) {
                Toast.makeText(this, "Por favor, completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val fechaNac = dateFormat.parse(fechaNacStr)

                if (directorId == -1) {
                    // Crear un nuevo director
                    val nuevoDirector = Director(
                        idDirector = 0, // Se generará automáticamente por la base de datos
                        nombre = nombre,
                        fechaNacimiento = fechaNac!!,
                        peliculasDirigidas = peliculasDirigidas,
                        calificacionIMDB = puntuacion,
                        enRodaje = enRodaje
                    )

                    // Insertar el nuevo director en la base de datos
                    val resultado = databaseHelper.addDirector(nuevoDirector)

                    if (resultado > 0) {
                        Toast.makeText(this, "Director creado exitosamente.", Toast.LENGTH_SHORT).show()
                        finish() // Cerrar la actividad y volver a la anterior
                    } else {
                        Toast.makeText(this, "Error al crear el director.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Actualizar un director existente
                    val directorActualizado = Director(
                        idDirector = directorId,
                        nombre = nombre,
                        fechaNacimiento = fechaNac!!,
                        peliculasDirigidas = peliculasDirigidas,
                        calificacionIMDB = puntuacion,
                        enRodaje = enRodaje
                    )

                    val resultado = databaseHelper.updateDirector(directorActualizado)

                    if (resultado > 0) {
                        Toast.makeText(this, "Director actualizado exitosamente.", Toast.LENGTH_SHORT).show()
                        finish() // Cerrar la actividad y volver a la anterior
                    } else {
                        Toast.makeText(this, "Error al actualizar el director.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

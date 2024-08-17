package com.example.deber03

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat

class CrearPelicula : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private var directorId: Int = 0
    private var peliculaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_pelicula)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        directorId = intent.getIntExtra("directorId", -1)
        peliculaId = intent.getIntExtra("peliculaId", -1)

        val inputNombre = findViewById<EditText>(R.id.input_nombrePelicula)
        val inputFechaEst = findViewById<EditText>(R.id.input_fechaEstreno)
        val inputCantAct = findViewById<EditText>(R.id.input_cantidadActores)
        val inputDuracion = findViewById<EditText>(R.id.input_duracion)
        val inputCartelera = findViewById<Switch>(R.id.input_enCartelera)
        val btnCrear = findViewById<Button>(R.id.btn_crear_pelicula)

        if (peliculaId != -1) {
            val pelicula = databaseHelper.getPeliculaById(peliculaId)
            inputNombre.setText(pelicula?.nombre)
            inputFechaEst.setText(SimpleDateFormat("dd/MM/yyyy").format(pelicula?.fechaEstreno))
            inputCantAct.setText(pelicula?.cantidadActores.toString())
            inputDuracion.setText(pelicula?.duracionMs.toString())
            inputCartelera.isChecked = pelicula?.enCartelera == true
            btnCrear.text = "Actualizar Película"
        }

        btnCrear.setOnClickListener {
            val nombre = inputNombre.text.toString()
            val fechaEst = inputFechaEst.text.toString()
            val cantActores = inputCantAct.text.toString().toIntOrNull()
            val durac = inputDuracion.text.toString().toDoubleOrNull()
            val enCart = inputCartelera.isChecked

            if (nombre.isEmpty() || fechaEst.isEmpty() || cantActores == null || durac == null) {
                Toast.makeText(this, "Por favor, completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val fechaEstr = dateFormat.parse(fechaEst)

                val nuevaPelicula = Pelicula(
                    idPelicula = peliculaId, // Usar el ID existente si estamos actualizando
                    nombre = nombre,
                    fechaEstreno = fechaEstr!!,
                    cantidadActores = cantActores,
                    duracionMs = durac,
                    enCartelera = enCart,
                    id_Director = directorId
                )

                val resultado: Long = if (peliculaId == -1) {
                    // Insertar nueva película
                    databaseHelper.addPelicula(nuevaPelicula)
                } else {
                    // Actualizar película existente
                    databaseHelper.updatePelicula(nuevaPelicula).toLong()
                }

                if (resultado > 0) {
                    val message = if (peliculaId == -1) {
                        "Película creada exitosamente."
                    } else {
                        "Película actualizada exitosamente."
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                    finish() // Cerrar la actividad y volver a la anterior
                } else {
                    Toast.makeText(this, "Error al guardar la película.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

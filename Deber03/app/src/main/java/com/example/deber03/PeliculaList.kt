package com.example.deber03

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PeliculaList : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var peliculaAdapter: PeliculaAdapter
    private var directorId: Int = 0
    private var selectedPeliculaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelicula_list)

        databaseHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lv_listt_view_peliculas)

        val btnCrearPelicula: Button = findViewById(R.id.btn_crearPelicula)
        val directorNombre = intent.getStringExtra("directorNombre")
        findViewById<TextView>(R.id.txt_director).text = directorNombre
        directorId = intent.getIntExtra("directorId", -1)

        if (directorId != -1) {
            val peliculaList = databaseHelper.getPeliculasByDirector(directorId)
            peliculaAdapter = PeliculaAdapter(this, peliculaList.toMutableList())
            listView.adapter = peliculaAdapter

            // Registrar ListView para el menú contextual
            registerForContextMenu(listView)
        }

        btnCrearPelicula.setOnClickListener {
            val intent = Intent(this, CrearPelicula::class.java).apply {
                putExtra("directorId", directorId)
            }
            startActivity(intent)
        }
        val btnIrUbi: Button = findViewById(R.id.btn_irGoogleMaps)
        btnIrUbi.setOnClickListener {
            val intent = Intent(this, GoogleMapsActivity::class.java)
            startActivity(intent)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val peliculaSeleccionada = peliculaAdapter.getItem(position)
            val intent = Intent(this, CrearPelicula::class.java).apply {
                putExtra("directorId", directorId)
                putExtra("peliculaId", peliculaSeleccionada?.idPelicula) // Pasar el ID de la película
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Actualizar la lista de películas al regresar a la PeliculaList
        val peliculasActualizadas = databaseHelper.getPeliculasByDirector(directorId)
        peliculaAdapter.clear()
        peliculaAdapter.addAll(peliculasActualizadas)
    }

    // Crear el menú contextual para la lista de películas
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu2, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        selectedPeliculaId = peliculaAdapter.getItem(info.position).idPelicula
    }

    // Manejar la selección de elementos en el menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                editarPelicula(selectedPeliculaId)
                true
            }
            R.id.mi_eliminar -> {
                databaseHelper.deletePelicula(selectedPeliculaId)
                onResume()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun editarPelicula(peliculaId: Int) {
        val pelicula = databaseHelper.getPeliculaById(peliculaId)

        val intent = Intent(this, CrearPelicula::class.java).apply {
            putExtra("peliculaId", pelicula?.idPelicula)
            putExtra("directorId", pelicula?.id_Director)
            putExtra("peliculaNombre", pelicula?.nombre)
            putExtra("peliculaFechaEstreno", pelicula?.fechaEstreno?.time)
            putExtra("peliculaCantidadActores", pelicula?.cantidadActores)
            putExtra("peliculaDuracion", pelicula?.duracionMs)
            putExtra("peliculaEnCartelera", pelicula?.enCartelera)
        }

        startActivity(intent)
    }
}

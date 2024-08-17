package com.example.deber03

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.PopupMenu

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var directorAdapter: DirectorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lv_list_view)
        val btnCrearArtista: Button = findViewById(R.id.btn_crearArtista)

        // Obtener la lista de directores de la base de datos
        val directorList = databaseHelper.getAllDirectors()

        // Inicializar el DirectorAdapter con la lista de directores
        directorAdapter = DirectorAdapter(this, directorList.toMutableList())
        listView.adapter = directorAdapter

        // Configurar el botón para crear un nuevo director
        btnCrearArtista.setOnClickListener {
            val intent = Intent(this, CrearDirector::class.java)
            startActivity(intent)
        }

        // Configurar el listener para el clic largo en un director
        listView.setOnItemLongClickListener { _, view, position, _ ->
            showPopupMenu(view, position)
            true
        }

        // Configurar el listener para el clic en un director
        listView.setOnItemClickListener { _, _, position, _ ->
            val directorSeleccionado = directorAdapter.getItem(position)

            // Crear un Intent para navegar a la actividad PeliculaList
            val intent = Intent(this, PeliculaList::class.java).apply {
                // Pasar datos del director seleccionado a la actividad PeliculaList
                putExtra("directorId", directorSeleccionado?.idDirector)
                putExtra("directorNombre", directorSeleccionado?.nombre)
            }
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        // Actualizar la lista de directores al regresar a la MainActivity
        val directoresActualizados = databaseHelper.getAllDirectors()
        directorAdapter.clear()
        directorAdapter.addAll(directoresActualizados)
        directorAdapter.notifyDataSetChanged()
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu2)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.mi_editar -> {
                    editarDirector(position)
                    true
                }
                R.id.mi_eliminar -> {
                    eliminarDirector(position)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun editarDirector(position: Int) {
        val director = directorAdapter.getItem(position)

        val intent = Intent(this, CrearDirector::class.java).apply {
            putExtra("directorId", director?.idDirector)
            putExtra("directorNombre", director?.nombre)
            putExtra("directorFechaNacimiento", director?.fechaNacimiento?.time)
            putExtra("directorPeliculasDirigidas", director?.peliculasDirigidas)
            putExtra("directorCalificacionIMDB", director?.calificacionIMDB)
            putExtra("directorEnRodaje", director?.enRodaje)
        }

        startActivity(intent)
    }

    private fun eliminarDirector(position: Int) {
        val director = directorAdapter.getItem(position)

        // Eliminar el director de la base de datos
        director?.let {
            databaseHelper.deleteDirector(it.idDirector)
        }

        // Remover el director de la lista en el adaptador
        directorAdapter.remove(position)
        directorAdapter.notifyDataSetChanged()
    }
}

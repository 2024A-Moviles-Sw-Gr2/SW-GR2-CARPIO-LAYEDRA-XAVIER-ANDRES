package com.example.proyectoalbumes

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaCanciones : AppCompatActivity() {
    private lateinit var cancionAdapter: CancionAdapter
    private lateinit var dbHelper: CancionDatabaseHelper
    private lateinit var dbHelperAux: DatabaseHelper
    private var albumId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_canciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbHelperAux = DatabaseHelper(this)
        dbHelperAux.clearAllCanciones()
        albumId = intent.getIntExtra("albumId", -1)
        insertDummyData()
        val albumName = intent.getStringExtra("name")
        val txtNombre: TextView = findViewById(R.id.albumAsociado)
        txtNombre.text = albumName.toString()
        cancionAdapter = CancionAdapter(dbHelperAux.getCancionesByAlbumId(albumId).toMutableList())

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCanciones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cancionAdapter

        // Configurar long click para mostrar menú
        cancionAdapter.setOnItemLongClickListener { view, position ->
            showPopupMenu(view, position)
            true
        }

        val addSongButton: Button = findViewById(R.id.addSongButton)
        addSongButton.setOnClickListener {
            showAddAlbumDialog()
        }

        val botonAtras: ImageButton = findViewById(R.id.backButton)
        botonAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.song_options_menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    showEditSongDialog(position)
                    true
                }
                R.id.menu_delete -> {
                    deleteSong(position)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showEditSongDialog(position: Int) {
        val song = cancionAdapter.getCancion(position)
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_cancion)

        val songNameEditText: EditText = dialog.findViewById(R.id.cancionName)
        val songDurEditText: EditText = dialog.findViewById(R.id.cancionDuracion)

        songNameEditText.setText(song.title)
        songDurEditText.setText(song.duracion.toString())

        val saveButton: Button = dialog.findViewById(R.id.saveSongButton)
        saveButton.setOnClickListener {
            val updatedName = songNameEditText.text.toString()
            val updatedDur = songDurEditText.text.toString().toFloat()

            song.title = updatedName
            song.duracion = updatedDur

            dbHelperAux.updateCancion(song)
            cancionAdapter.notifyItemChanged(position)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteSong(position: Int) {
        val song = cancionAdapter.getCancion(position)
        dbHelperAux.deleteCancion(song)
        cancionAdapter.removeCancion(position)
        cancionAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "Canción eliminada", Toast.LENGTH_SHORT).show()
    }

    private fun showAddAlbumDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_cancion)

        val songNameEditText: EditText = dialog.findViewById(R.id.cancionName)
        val songDurEditText: EditText = dialog.findViewById(R.id.cancionDuracion)

        val saveButton: Button = dialog.findViewById(R.id.saveSongButton)
        saveButton.setOnClickListener {
            val songName = songNameEditText.text.toString()
            val songDur = songDurEditText.text.toString().toFloat()

            val cancion = Cancion(title = songName, duracion = songDur, albumId = albumId)
            dbHelperAux.addCancion(cancion)
            cancionAdapter.addCancion(cancion)
            cancionAdapter.notifyItemInserted(cancionAdapter.itemCount - 1)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun insertDummyData() {
        val track1 = Cancion("Fortnight", 3.2F, albumId)
        val track2 = Cancion("Down Bad", 3.5F, albumId)
        val track3 = Cancion("Guilty As Sin", 2.33F, albumId)
        val track4 = Cancion("So Long, London", 4.33F, albumId)
        dbHelperAux.addCancion(track1)
        dbHelperAux.addCancion(track2)
        dbHelperAux.addCancion(track3)
        dbHelperAux.addCancion(track4)
    }
}

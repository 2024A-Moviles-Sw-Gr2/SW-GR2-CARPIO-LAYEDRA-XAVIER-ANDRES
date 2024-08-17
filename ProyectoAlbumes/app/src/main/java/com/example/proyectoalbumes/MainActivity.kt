package com.example.proyectoalbumes

import Album
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var dbHelperAux: DatabaseHelper

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageView: ImageView
    private lateinit var selectedBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelperAux = DatabaseHelper(this)
        dbHelperAux.clearAllAlbums()
        insertDummyData()
        albumAdapter = AlbumAdapter(dbHelperAux.getAllAlbums().toMutableList()) { album ->
            val intent = Intent(this, ListaCanciones::class.java)
            intent.putExtra("albumId", album.id)
            intent.putExtra("name", album.name)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = albumAdapter

        val addAlbumButton: Button = findViewById(R.id.addSongButton)
        addAlbumButton.setOnClickListener {
            showAddAlbumDialog()
        }
    }

    private fun showAddAlbumDialog(album: Album? = null, position: Int? = null) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_album)

        val albumNameEditText: EditText = dialog.findViewById(R.id.albumName)
        val selectImageButton: Button = dialog.findViewById(R.id.selectImageButton)
        selectedImageView = dialog.findViewById(R.id.albumCoverPreview)

        var tempBitmap: Bitmap? = null

        album?.let {
            albumNameEditText.setText(it.name)
            val bitmap = it.image?.let { it1 -> BitmapFactory.decodeByteArray(it.image, 0, it1.size) }
            selectedImageView.setImageBitmap(bitmap)
            tempBitmap = bitmap
        }

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        val saveButton: Button = dialog.findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            val albumName = albumNameEditText.text.toString()

            if (tempBitmap != null) {
                selectedBitmap = tempBitmap!!
            }

            if (::selectedBitmap.isInitialized) {
                val imageByteArray = convertBitmapToByteArray(selectedBitmap)
                if (album == null) {
                    val newAlbum = Album(albumName, imageByteArray)
                    dbHelperAux.addAlbum(newAlbum)
                    albumAdapter.addAlbum(newAlbum)
                } else {
                    val updatedAlbum = album.copy(name = albumName, image = imageByteArray)
                    dbHelperAux.updateAlbum(updatedAlbum)
                    albumAdapter.updateAlbum(position!!, updatedAlbum)
                }
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Por favor, selecciona una imagen", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                selectedImageView.setImageBitmap(bitmap)
                selectedBitmap = bitmap
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun insertDummyData() {
        if (dbHelperAux.getAllAlbums().isEmpty()) {
            val drawableImage = BitmapFactory.decodeResource(resources, R.drawable.album_image)
            val dummyImage1 = convertBitmapToByteArray(drawableImage)
            val dummyImage2 = convertBitmapToByteArray(BitmapFactory.decodeResource(resources, R.drawable.folklore))
            val dummyImage3 = convertBitmapToByteArray(BitmapFactory.decodeResource(resources, R.drawable.aztlan))

            val album1 = Album("The Tortured Poets", dummyImage1)
            val album2 = Album("folklore", dummyImage2)
            val album3 = Album("Aztlán", dummyImage3)

            dbHelperAux.addAlbum(album1)
            dbHelperAux.addAlbum(album2)
            dbHelperAux.addAlbum(album3)
        }
    }

    fun showEditAlbumDialog(album: Album, position: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_album)

        val albumNameEditText: EditText = dialog.findViewById(R.id.albumName)
        val selectImageButton: Button = dialog.findViewById(R.id.selectImageButton)
        selectedImageView = dialog.findViewById(R.id.albumCoverPreview)

        albumNameEditText.setText(album.name)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        val saveButton: Button = dialog.findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            val updatedName = albumNameEditText.text.toString()

            if (::selectedBitmap.isInitialized) {
                val imageByteArray = convertBitmapToByteArray(selectedBitmap)
                val updatedAlbum = album.copy(name = updatedName,  image = imageByteArray)
                dbHelperAux.updateAlbum(updatedAlbum)
                albumAdapter.albums[position] = updatedAlbum
                albumAdapter.notifyItemChanged(position)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Por favor, selecciona una imagen", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    fun deleteAlbum(album: Album, position: Int) {
        dbHelperAux.deleteAlbum(album)
        albumAdapter.albums.removeAt(position)
        albumAdapter.notifyItemRemoved(position)
    }
}

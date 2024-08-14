package com.example.proyectoalbumes

import Album
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {


        private const val DATABASE_NAME = "mi_base_de_datos.db"
        private const val DATABASE_VERSION = 1

        // Tablas y columnas de álbumes
        const val TABLE_ALBUMS = "albums"
        const val COLUMN_ALBUM_ID = "id"
        const val COLUMN_ALBUM_NAME = "name"
        const val COLUMN_ALBUM_IMAGE = "image"

        // Tablas y columnas de canciones
        const val TABLE_CANCIONES = "canciones"
        const val COLUMN_CANCION_ID = "id"
        const val COLUMN_CANCION_TITLE = "title"
        const val COLUMN_CANCION_DURACION = "duracion"
        const val COLUMN_CANCION_ALBUM_ID = "albumId"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de álbumes
        val createAlbumsTable = ("CREATE TABLE $TABLE_ALBUMS (" +
                "$COLUMN_ALBUM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ALBUM_NAME TEXT, " +
                "$COLUMN_ALBUM_IMAGE BLOB)")
        db?.execSQL(createAlbumsTable)

        // Crear tabla de canciones
        val createCancionesTable = ("CREATE TABLE $TABLE_CANCIONES (" +
                "$COLUMN_CANCION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_CANCION_TITLE TEXT, " +
                "$COLUMN_CANCION_DURACION REAL, " +
                "$COLUMN_CANCION_ALBUM_ID INTEGER, " +
                "FOREIGN KEY($COLUMN_CANCION_ALBUM_ID) REFERENCES $TABLE_ALBUMS($COLUMN_ALBUM_ID))")
        db?.execSQL(createCancionesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Eliminar tablas existentes
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CANCIONES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ALBUMS")
        onCreate(db)
    }

    // Métodos para álbumes

    fun addAlbum(album: Album): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ALBUM_NAME, album.name)
            put(COLUMN_ALBUM_IMAGE, album.image)
        }
        return db.insert(TABLE_ALBUMS, null, values)
    }

    fun getAllAlbums(): List<Album> {
        val albumList = mutableListOf<Album>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ALBUMS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ALBUM_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALBUM_NAME))
                val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_ALBUM_IMAGE))
                albumList.add(Album(name, image, id))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return albumList
    }

    fun updateAlbum(album: Album): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ALBUM_NAME, album.name)
            put(COLUMN_ALBUM_IMAGE, album.image)
        }
        return db.update(TABLE_ALBUMS, values, "$COLUMN_ALBUM_ID = ?", arrayOf(album.id.toString()))
    }

    fun deleteAlbum(album: Album): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_ALBUMS, "$COLUMN_ALBUM_ID = ?", arrayOf(album.id.toString()))
    }

    fun clearAllAlbums() {
        val db = this.writableDatabase
        db.delete(TABLE_ALBUMS, null, null)
        db.close()
    }

    // Métodos para canciones

    fun addCancion(cancion: Cancion): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CANCION_TITLE, cancion.title)
            put(COLUMN_CANCION_DURACION, cancion.duracion)
            put(COLUMN_CANCION_ALBUM_ID, cancion.albumId)
        }
        return db.insert(TABLE_CANCIONES, null, values)
    }

    fun getCancionesByAlbumId(albumId: Int): List<Cancion> {
        val canciones = mutableListOf<Cancion>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CANCIONES,
            arrayOf(COLUMN_CANCION_ID, COLUMN_CANCION_TITLE, COLUMN_CANCION_DURACION, COLUMN_CANCION_ALBUM_ID),
            "$COLUMN_CANCION_ALBUM_ID = ?",
            arrayOf(albumId.toString()),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANCION_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CANCION_TITLE))
                val duracion = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_CANCION_DURACION))
                canciones.add(Cancion(title, duracion, albumId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return canciones
    }

    fun updateCancion(cancion: Cancion): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CANCION_TITLE, cancion.title)
            put(COLUMN_CANCION_DURACION, cancion.duracion)
            put(COLUMN_CANCION_ALBUM_ID, cancion.albumId)
        }
        return db.update(TABLE_CANCIONES, values, "$COLUMN_CANCION_ID = ?", arrayOf(cancion.id.toString()))
    }

    fun deleteCancion(cancion: Cancion): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CANCIONES, "$COLUMN_CANCION_ID = ?", arrayOf(cancion.id.toString()))
    }

    fun clearAllCanciones() {
        val db = this.writableDatabase
        db.delete(TABLE_CANCIONES, null, null)
        db.close()
    }

    fun openDatabase() {
        this.writableDatabase // Forzar la creación de la base de datos
    }
    fun getAlbumNameById(albumId: Int): String? {
        val db = this.readableDatabase
        var albumName: String? = null
        val cursor = db.query(
            TABLE_ALBUMS,
            arrayOf(COLUMN_ALBUM_NAME),
            "$COLUMN_ALBUM_ID = ?",
            arrayOf(albumId.toString()),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            albumName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALBUM_NAME))
        }
        cursor.close()
        return albumName
    }
}

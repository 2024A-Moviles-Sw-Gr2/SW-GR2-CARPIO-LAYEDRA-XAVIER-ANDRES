package com.example.proyectoalbumes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class CancionDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "canciones.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_CANCIONES = "canciones"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DURACION = "duracion"
        const val COLUMN_ALBUM_ID = "albumId"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_CANCIONES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_DURACION REAL, " +
                "$COLUMN_ALBUM_ID INTEGER, " +
                "FOREIGN KEY($COLUMN_ALBUM_ID) REFERENCES ${AlbumDatabaseHelper.TABLE_ALBUMS}(${AlbumDatabaseHelper.COLUMN_ID}))")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CANCIONES")
        val createTable = ("CREATE TABLE $TABLE_CANCIONES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_DURACION REAL, " +
                "$COLUMN_ALBUM_ID INTEGER, " +
                "FOREIGN KEY($COLUMN_ALBUM_ID) REFERENCES ${AlbumDatabaseHelper.TABLE_ALBUMS}(${AlbumDatabaseHelper.COLUMN_ID}))")
        db?.execSQL(createTable)

    }



    fun getCancionesByAlbumId(albumId: Int): List<Cancion> {
        val canciones = mutableListOf<Cancion>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CANCIONES,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_DURACION, COLUMN_ALBUM_ID),
            "$COLUMN_ALBUM_ID = ?",
            arrayOf(albumId.toString()),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val duracion = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_DURACION))
                canciones.add(Cancion(title, duracion, albumId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return canciones
    }

    fun updateCancion(cancion: Cancion): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, cancion.title)
            put(COLUMN_DURACION, cancion.duracion)
            put(COLUMN_ALBUM_ID, cancion.albumId)
        }
        return db.update(TABLE_CANCIONES, values, "$COLUMN_ID = ?", arrayOf(cancion.id.toString()))
    }

    fun deleteCancion(cancion: Cancion): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CANCIONES, "$COLUMN_ID = ?", arrayOf(cancion.id.toString()))
    }

    fun clearAllCanciones() {
        val db = this.writableDatabase
        db.delete(TABLE_CANCIONES, null, null)
        db.close()
    }
    fun addCancion(cancion: Cancion): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, cancion.title)
            put(COLUMN_DURACION, cancion.duracion)
        }
        return db.insert(TABLE_CANCIONES, null, values)
    }
    fun openDatabase() {
        Log.d("Database", "Opening database")
        this.writableDatabase // Forzar la creación de la base de datos
    }
}

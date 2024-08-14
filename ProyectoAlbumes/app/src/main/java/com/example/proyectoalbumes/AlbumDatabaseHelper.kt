package com.example.proyectoalbumes


import Album
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AlbumDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "albums.db"
        private const val DATABASE_VERSION = 3

        const val TABLE_ALBUMS = "albums"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_ALBUMS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_IMAGE BLOB)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ALBUMS")
        onCreate(db)
    }

    fun addAlbum(album: Album): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, album.name)
            put(COLUMN_IMAGE, album.image)
        }
        return db.insert(TABLE_ALBUMS, null, values)
    }

    fun getAllAlbums(): List<Album> {
        val albumList = mutableListOf<Album>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ALBUMS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                albumList.add(Album(name, image, id))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return albumList
    }

    fun updateAlbum(album: Album): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, album.name)
            put(COLUMN_IMAGE, album.image)
        }
        return db.update(TABLE_ALBUMS, values, "$COLUMN_ID = ?", arrayOf(album.id.toString()))
    }

    fun deleteAlbum(album: Album): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_ALBUMS, "$COLUMN_ID = ?", arrayOf(album.id.toString()))
    }
    fun clearAllAlbums() {
        val db = this.writableDatabase
        db.delete(TABLE_ALBUMS, null, null)
        db.close()
    }
}
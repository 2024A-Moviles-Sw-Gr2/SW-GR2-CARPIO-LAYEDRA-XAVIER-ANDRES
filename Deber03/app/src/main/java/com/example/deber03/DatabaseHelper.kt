package com.example.deber03

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "movie_database.db"
        private const val DATABASE_VERSION = 1

        // Table names
        private const val TABLE_DIRECTOR = "Director"
        private const val TABLE_PELICULA = "Pelicula"

        // Common column names
        private const val COLUMN_ID = "id"

        // Director Table - column names
        private const val COLUMN_NOMBRE_DIRECTOR = "nombre"
        private const val COLUMN_FECHA_NACIMIENTO = "fecha_nacimiento"
        private const val COLUMN_PELICULAS_DIRIGIDAS = "peliculas_dirigidas"
        private const val COLUMN_CALIFICACION_IMDB = "calificacion_imdb"
        private const val COLUMN_EN_RODAJE = "en_rodaje"

        // Pelicula Table - column names
        private const val COLUMN_NOMBRE_PELICULA = "nombre"
        private const val COLUMN_FECHA_ESTRENO = "fecha_estreno"
        private const val COLUMN_CANTIDAD_ACTORES = "cantidad_actores"
        private const val COLUMN_DURACION_MS = "duracion_ms"
        private const val COLUMN_EN_CARTELERA = "en_cartelera"
        private const val COLUMN_ID_DIRECTOR = "id_director"

        // Date format
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createDirectorTable = ("CREATE TABLE $TABLE_DIRECTOR ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NOMBRE_DIRECTOR TEXT, "
                + "$COLUMN_FECHA_NACIMIENTO TEXT, "
                + "$COLUMN_PELICULAS_DIRIGIDAS INTEGER, "
                + "$COLUMN_CALIFICACION_IMDB REAL, "
                + "$COLUMN_EN_RODAJE INTEGER)")

        val createPeliculaTable = ("CREATE TABLE $TABLE_PELICULA ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NOMBRE_PELICULA TEXT, "
                + "$COLUMN_FECHA_ESTRENO TEXT, "
                + "$COLUMN_CANTIDAD_ACTORES INTEGER, "
                + "$COLUMN_DURACION_MS REAL, "
                + "$COLUMN_EN_CARTELERA INTEGER, "
                + "$COLUMN_ID_DIRECTOR INTEGER, "
                + "FOREIGN KEY($COLUMN_ID_DIRECTOR) REFERENCES $TABLE_DIRECTOR($COLUMN_ID))")

        db.execSQL(createDirectorTable)
        db.execSQL(createPeliculaTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PELICULA")
        onCreate(db)
    }

    // CRUD Operations for Director
    fun addDirector(director: Director): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE_DIRECTOR, director.nombre)
        values.put(COLUMN_FECHA_NACIMIENTO, dateFormat.format(director.fechaNacimiento))
        values.put(COLUMN_PELICULAS_DIRIGIDAS, director.peliculasDirigidas)
        values.put(COLUMN_CALIFICACION_IMDB, director.calificacionIMDB)
        values.put(COLUMN_EN_RODAJE, if (director.enRodaje) 1 else 0)
        return db.insert(TABLE_DIRECTOR, null, values)
    }

    fun getDirector(id: Int): Director? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_DIRECTOR, null, "$COLUMN_ID=?", arrayOf(id.toString()),
            null, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val director = Director(
                idDirector = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_DIRECTOR)),
                fechaNacimiento = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_NACIMIENTO)))!!,
                peliculasDirigidas = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PELICULAS_DIRIGIDAS)),
                calificacionIMDB = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALIFICACION_IMDB)),
                enRodaje = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EN_RODAJE)) == 1
            )
            cursor.close()
            return director
        }
        cursor?.close()
        return null
    }

    fun updateDirector(director: Director): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE_DIRECTOR, director.nombre)
        values.put(COLUMN_FECHA_NACIMIENTO, dateFormat.format(director.fechaNacimiento))
        values.put(COLUMN_PELICULAS_DIRIGIDAS, director.peliculasDirigidas)
        values.put(COLUMN_CALIFICACION_IMDB, director.calificacionIMDB)
        values.put(COLUMN_EN_RODAJE, if (director.enRodaje) 1 else 0)
        return db.update(TABLE_DIRECTOR, values, "$COLUMN_ID=?", arrayOf(director.idDirector.toString()))
    }

    fun deleteDirector(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_DIRECTOR, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // CRUD Operations for Pelicula
    fun addPelicula(pelicula: Pelicula): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE_PELICULA, pelicula.nombre)
        values.put(COLUMN_FECHA_ESTRENO, dateFormat.format(pelicula.fechaEstreno))
        values.put(COLUMN_CANTIDAD_ACTORES, pelicula.cantidadActores)
        values.put(COLUMN_DURACION_MS, pelicula.duracionMs)
        values.put(COLUMN_EN_CARTELERA, if (pelicula.enCartelera) 1 else 0)
        values.put(COLUMN_ID_DIRECTOR, pelicula.id_Director)
        return db.insert(TABLE_PELICULA, null, values)
    }

    fun getPelicula(id: Int): Pelicula? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PELICULA, null, "$COLUMN_ID=?", arrayOf(id.toString()),
            null, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val pelicula = Pelicula(
                idPelicula = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_PELICULA)),
                fechaEstreno = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_ESTRENO)))!!,
                cantidadActores = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD_ACTORES)),
                duracionMs = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DURACION_MS)),
                enCartelera = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EN_CARTELERA)) == 1,
                id_Director = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_DIRECTOR))
            )
            cursor.close()
            return pelicula
        }
        cursor?.close()
        return null
    }

    fun updatePelicula(pelicula: Pelicula): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE_PELICULA, pelicula.nombre)
        values.put(COLUMN_FECHA_ESTRENO, dateFormat.format(pelicula.fechaEstreno))
        values.put(COLUMN_CANTIDAD_ACTORES, pelicula.cantidadActores)
        values.put(COLUMN_DURACION_MS, pelicula.duracionMs)
        values.put(COLUMN_EN_CARTELERA, if (pelicula.enCartelera) 1 else 0)
        values.put(COLUMN_ID_DIRECTOR, pelicula.id_Director)
        return db.update(TABLE_PELICULA, values, "$COLUMN_ID=?", arrayOf(pelicula.idPelicula.toString()))
    }

    fun deletePelicula(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_PELICULA, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // Get all Directors
    fun getAllDirectors(): List<Director> {
        val directors = mutableListOf<Director>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_DIRECTOR", null)
        if (cursor.moveToFirst()) {
            do {
                val director = Director(
                    idDirector = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_DIRECTOR)),
                    fechaNacimiento = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_NACIMIENTO)))!!,
                    peliculasDirigidas = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PELICULAS_DIRIGIDAS)),
                    calificacionIMDB = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALIFICACION_IMDB)),
                    enRodaje = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EN_RODAJE)) == 1
                )
                directors.add(director)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return directors
    }


    // Get all Movies of a Director
    fun getPeliculasByDirector(directorId: Int): List<Pelicula> {
        val peliculas = mutableListOf<Pelicula>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PELICULA WHERE $COLUMN_ID_DIRECTOR = ?", arrayOf(directorId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val pelicula = Pelicula(
                    idPelicula = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_PELICULA)),
                    fechaEstreno = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_ESTRENO)))!!,
                    cantidadActores = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD_ACTORES)),
                    duracionMs = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DURACION_MS)),
                    enCartelera = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EN_CARTELERA)) == 1,
                    id_Director = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_DIRECTOR))
                )
                peliculas.add(pelicula)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return peliculas
    }
    fun clearAllDirectors() {
        val db = this.writableDatabase
        db.delete(TABLE_DIRECTOR, null, null)
        db.close()
    }
    @SuppressLint("Range")
    fun getPeliculaById(idPelicula: Int): Pelicula? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Pelicula WHERE idPelicula = ?", arrayOf(idPelicula.toString()))
        var pelicula: Pelicula? = null
        if (cursor.moveToFirst()) {
            pelicula = Pelicula(
                idPelicula = cursor.getInt(cursor.getColumnIndex("idPelicula")),
                nombre = cursor.getString(cursor.getColumnIndex("nombre")),
                fechaEstreno = Date(cursor.getLong(cursor.getColumnIndex("fechaEstreno"))),
                cantidadActores = cursor.getInt(cursor.getColumnIndex("cantidadActores")),
                duracionMs = cursor.getDouble(cursor.getColumnIndex("duracionMs")),
                enCartelera = cursor.getInt(cursor.getColumnIndex("enCartelera")) > 0,
                id_Director = cursor.getInt(cursor.getColumnIndex("id_Director"))
            )
        }
        cursor.close()
        return pelicula
    }


}

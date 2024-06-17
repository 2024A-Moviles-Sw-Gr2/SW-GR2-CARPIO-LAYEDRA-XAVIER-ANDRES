import java.io.*
import java.text.SimpleDateFormat
import java.util.*


const val peliculaFile="src/main/resources/Peliculas_DB.txt"

data class Pelicula(
    var nombre: String,
    var fechaEstreno: Date,
    var duracionMs: Double,
    var enCartelera: Boolean,
    var cantidadActores: Int,
    var director: Director
) : Serializable

fun savePeliculas(peliculas: List<Pelicula>) {
    ObjectOutputStream(FileOutputStream(peliculaFile)).use { it.writeObject(peliculas) }
}

fun loadPeliculas(): MutableList<Pelicula> {
    return try {
        ObjectInputStream(FileInputStream(peliculaFile)).use { it.readObject() as MutableList<Pelicula> }
    } catch (e: FileNotFoundException) {
        mutableListOf()
    }
}

fun createPelicula(pelicula: Pelicula) {
    val peliculas = loadPeliculas()
    peliculas.add(pelicula)
    savePeliculas(peliculas)
}

fun readPeliculas() {
    val peliculas = loadPeliculas()
    peliculas.forEach { pelicula ->
        println("Nombre: ${pelicula.nombre}")
        println("Fecha de estreno: ${pelicula.fechaEstreno}")
        println("Duración: ${pelicula.duracionMs} ms")
        println("En cartelera: ${pelicula.enCartelera}")
        println("Cantidad de actores: ${pelicula.cantidadActores}")
        println("Director: ${pelicula.director.nombre}")
        println()  // Salto de línea entre cada película
    }
}


fun updatePelicula(nombre: String, updatedPelicula: Pelicula) {
    val peliculas = loadPeliculas()
    val index = peliculas.indexOfFirst { it.nombre == nombre }
    if (index != -1) {
        peliculas[index] = updatedPelicula
        savePeliculas(peliculas)
    }
}

fun deletePelicula(nombre: String) {
    val peliculas = loadPeliculas()
    val iterator = peliculas.iterator()
    while (iterator.hasNext()) {
        if (iterator.next().nombre == nombre) {
            iterator.remove()
            break
        }
    }
    savePeliculas(peliculas)
}
import java.io.*
import java.util.*

data class Pelicula(
    var nombre: String,
    var fechaEstreno: Date,
    var duracionMs: Double,
    var enCartelera: Boolean,
    var cantidadActores: Int,
) : Serializable

const val peliculaFile = "src/main/resources/Peliculas_DB.txt"

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
    if (peliculas.none { it.nombre.equals(pelicula.nombre, ignoreCase = true) }) {
        peliculas.add(pelicula)
        savePeliculas(peliculas)
    } else {
        println("La película con nombre ${pelicula.nombre} ya existe.")
    }
}

fun readPeliculas(nombre: String?) {
    print("\t\t ****** \n")
    val peliculas = loadPeliculas()
    val peliculaEncontrada = peliculas.find { it.nombre.equals(nombre, ignoreCase = true) }
    if (peliculaEncontrada != null) {
        println("Nombre: ${peliculaEncontrada.nombre}")
        println("Fecha de estreno: ${peliculaEncontrada.fechaEstreno}")
        println("Duración: ${peliculaEncontrada.duracionMs} ms")
        println("En cartelera: ${peliculaEncontrada.enCartelera}")
        println("Cantidad de actores: ${peliculaEncontrada.cantidadActores}")
    } else {
        println("No se encontró una película con el nombre $nombre.")
    }
}
fun readPeliculas() {
    print("\t\t ****** \n")
    val peliculas = loadPeliculas()
    peliculas.forEach { pelicula ->
        println("Nombre: ${pelicula.nombre}")
        println("Fecha de estreno: ${pelicula.fechaEstreno}")
        println("Duración: ${pelicula.duracionMs} ms")
        println("En cartelera: ${pelicula.enCartelera}")
        println("Cantidad de actores: ${pelicula.cantidadActores}")
        println() // Salto de línea entre cada película
        print("\t\t ****** \n")
    }
}



fun updatePelicula(nombre: String, updatedPelicula: Pelicula) {
    val peliculas = loadPeliculas()
    val index = peliculas.indexOfFirst { it.nombre.equals(nombre, ignoreCase = true) }
    if (index != -1) {
        peliculas[index] = updatedPelicula
        savePeliculas(peliculas)
    } else {
        println("La película con nombre $nombre no se encontró.")
    }
}

fun deletePelicula(nombre: String) {
    val peliculas = loadPeliculas()
    val iterator = peliculas.iterator()
    while (iterator.hasNext()) {
        if (iterator.next().nombre.equals(nombre, ignoreCase = true)) {
            iterator.remove()
            break
        }
    }
    savePeliculas(peliculas)
}

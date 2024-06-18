import java.io.*
import java.util.*

data class Director(
    var nombre: String,
    var fechaNacimiento: Date,
    var peliculasDirigidas: Int,
    var calificacionIMDB: Double,
    var enRodaje: Boolean
) : Serializable

const val directorFile = "src/main/resources/Directores_DB.txt"

fun saveDirectores(directores: List<Director>) {
    ObjectOutputStream(FileOutputStream(directorFile)).use { it.writeObject(directores) }
}

fun loadDirectores(): MutableList<Director> {
    return try {
        ObjectInputStream(FileInputStream(directorFile)).use { it.readObject() as MutableList<Director> }
    } catch (e: FileNotFoundException) {
        mutableListOf()
    }
}

fun createDirector(director: Director) {
    val directores = loadDirectores()
    if (directores.none { it.nombre.equals(director.nombre, ignoreCase = true) }) {
        directores.add(director)
        saveDirectores(directores)
    } else {
        println("El director con nombre ${director.nombre} ya existe.")
    }
}

fun readDirectores(nombre: String?) {
    val directores = loadDirectores()
    val directorEncontrado = directores.find { it.nombre.equals(nombre, ignoreCase = true) }
    if (directorEncontrado != null) {
        println("Nombre: ${directorEncontrado.nombre}")
        println("Fecha de nacimiento: ${directorEncontrado.fechaNacimiento}")
        println("Películas dirigidas: ${directorEncontrado.peliculasDirigidas}")
        println("Calificación IMDB: ${directorEncontrado.calificacionIMDB}")
        println("En rodaje: ${directorEncontrado.enRodaje}")
    } else {
        println("No se encontró un director con el nombre $nombre.")
    }
}

fun readDirectores() {
    print("\t\t ****** \n")
    val directores = loadDirectores()
    directores.forEach { director ->
        println("Nombre: ${director.nombre}")
        println("Fecha de nacimiento: ${director.fechaNacimiento}")
        println("Películas dirigidas: ${director.peliculasDirigidas}")
        println("Calificación IMDB: ${director.calificacionIMDB}")
        println("En rodaje: ${director.enRodaje}")
        println() // Salto de línea entre cada director
    }
    print("\t\t ****** \n")
}

fun updateDirector(nombre: String, updatedDirector: Director) {
    val directores = loadDirectores()
    val index = directores.indexOfFirst { it.nombre.equals(nombre, ignoreCase = true) }
    if (index != -1) {
        directores[index] = updatedDirector
        saveDirectores(directores)
    } else {
        println("El director con nombre $nombre no se encontró.")
    }
}

fun deleteDirector(nombre: String) {
    val directores = loadDirectores()
    val iterator = directores.iterator()
    while (iterator.hasNext()) {
        if (iterator.next().nombre.equals(nombre, ignoreCase = true)) {
            iterator.remove()
            break
        }
    }
    saveDirectores(directores)
}

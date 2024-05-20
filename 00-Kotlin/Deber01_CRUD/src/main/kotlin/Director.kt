import java.io.*
import java.util.*

const val directorFile = "src/main/resources/Directores_DB.txt"

data class Director(
    var nombre: String,
    var fechaNacimiento: Date,
    var peliculasDirigidas: Int,
    var calificacionIMDB: Double,
    var enRodaje: Boolean
) : Serializable


fun saveDirectors(directors: List<Director>) {
    val file = File(directorFile)

    ObjectOutputStream(FileOutputStream(directorFile)).use { it.writeObject(directors) }
}

fun loadDirectors(): MutableList<Director> {
    return try {
        ObjectInputStream(FileInputStream(directorFile)).use { it.readObject() as MutableList<Director> }
    } catch (e: FileNotFoundException) {
        mutableListOf()
    }
}

fun createDirector(director: Director) {
    val directors = loadDirectors()
    directors.add(director)
    saveDirectors(directors)
}

fun readDirectors() = loadDirectors().forEach { director ->
    println("Nombre: ${director.nombre}")
    println("Fecha de nacimiento: ${director.fechaNacimiento}")
    println("Películas dirigidas: ${director.peliculasDirigidas}")
    println("Calificación IMDB: ${director.calificacionIMDB}")
    println("En rodaje: ${director.enRodaje}")
    println()  // Salto de línea entre cada director
}

fun updateDirector(nombre: String, updatedDirector: Director) {
    val directors = loadDirectors()
    val index = directors.indexOfFirst { it.nombre == nombre }
    if (index != -1) {
        directors[index] = updatedDirector
        saveDirectors(directors)
    }
}

fun deleteDirector(nombre: String) {
    val directors = loadDirectors()
    val iterator = directors.iterator()
    while (iterator.hasNext()) {
        if (iterator.next().nombre == nombre) {
            iterator.remove()
            break
        }
    }
    saveDirectors(directors)
}

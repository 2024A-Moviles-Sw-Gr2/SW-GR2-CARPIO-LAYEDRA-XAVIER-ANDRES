import java.io.*
import java.text.SimpleDateFormat
import java.util.*

fun main() {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    //Creación clases ejemplo
    val director1 = Director("Quentin Tarantino",
        dateFormat.parse("27/03/1963"),
        10, 8.9, true)

    val director2 = Director("Christopher Nolan",
        dateFormat.parse("30/07/1970"),
        12, 9.1, true)

    createDirector(director1)
    createDirector(director2)

    //READ
    println("Directores después de la creación:")
    readDirectors()

    //Update
    val updatedDirector = director1.copy(calificacionIMDB = 9.0)
    updateDirector("Quentin Tarantino", updatedDirector)
    println("Directores después de la actualización:")
    readDirectors()

    // Eliminar director
    deleteDirector("Christopher Nolan")

    println("Directores después de la eliminación:")
    readDirectors()


    //Ejemplo para pelicula
    val pelicula1 = Pelicula("Pulp Fiction", dateFormat.parse("14/10/1994"),
        154.0, false, 20, director2)

    createPelicula(pelicula1)
    //READ
    println("Peliculas después de la creación:")
    readPeliculas()
}

import java.io.*
import java.text.SimpleDateFormat
import java.util.*

val scanner = Scanner(System.`in`)
fun main() {


    while (true) {
        print("----------------------------------------\n")
        println("Seleccione una opción:")
        println("1. Películas")
        println("2. Directores")
        println("3. Salir")
        print("Opción: ")
        val opcion = scanner.nextInt()
        scanner.nextLine() // Limpiar el buffer de entrada

        when (opcion) {
            1 -> menuPeliculas()
            2 -> menuDirectores()
            3 -> {
                println("Saliendo del programa...")
                return
            }
            else -> println("Opción inválida, por favor seleccione una opción válida.")
        }
    }
}

fun menuPeliculas() {
    val scanner = Scanner(System.`in`)

    while (true) {
        println("Menú Películas:")
        println("1. Crear")
        println("2. Buscar película")
        println("3. Obtener lista de películas")
        println("4. Actualizar")
        println("5. Eliminar")
        println("6. Volver al menú principal")
        print("Opción: ")
        val opcion = scanner.nextInt()
        scanner.nextLine() // Limpiar el buffer de entrada

        when (opcion) {
            1 -> {

                val pelicula1=solicitarDatosPelicula()
                createPelicula(pelicula1)
            }

            2 -> {
                println("Ingrese el nombre de la película:")
                print("Nombre: ")
                val nombre = scanner.nextLine()
                readPeliculas(nombre)
            }
            3 -> readPeliculas()
            4 -> {
                println("Ingrese el nombre de la película a actualizar:")
                print("Nombre: ")
                val nombre = scanner.nextLine()
                val peliculaAct=solicitarDatosPelicula()
                updatePelicula(nombre,peliculaAct)
            }

            5 -> {
                println("Ingrese el nombre de la película a eliminar:")
                print("Nombre: ")
                val nombre = scanner.nextLine()
                deletePelicula(nombre)
            }
        }
        print("----------------------------------------\n")
    }

}
fun solicitarDatosPelicula():Pelicula{
    println("Ingrese los datos de la nueva película:")
    print("Nombre: ")
    val nombre = scanner.nextLine()
    println("Ingrese la fecha de estreno (dd-MM-yyyy): ")
    print("Fecha de estreno: ")
    val fechaEstreno = SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine())
    println("Ingrese la duración en milisegundos: ")
    print("Duración: ")
    val duracionMs = scanner.nextDouble()
    println("¿En cartelera? (true/false): ")
    print("En cartelera: ")
    val enCartelera = scanner.nextBoolean()
    println("Ingrese la cantidad de actores: ")
    print("Cantidad de actores: ")
    val cantidadActores = scanner.nextInt()

    val Pelicula1 = Pelicula(
        nombre,
        fechaEstreno,
        duracionMs,
        enCartelera,
        cantidadActores
    )
    return Pelicula1
}
fun menuDirectores() {
    val scanner = Scanner(System.`in`)

    while (true) {
        println("Menú Directores:")
        println("1. Crear director")
        println("2. Buscar director")
        println("3. Obtener lista de directores")
        println("4. Actualizar director")
        println("5. Eliminar director")
        println("6. Volver al menú principal")
        print("Opción: ")
        val opcion = scanner.nextInt()
        scanner.nextLine() // Limpiar el buffer de entrada

        when (opcion) {
            1 -> {
                val director = solicitarDatosDirector()
                createDirector(director)
            }
            2 -> {
                println("Ingrese el nombre del director:")
                print("Nombre: ")
                val nombre = scanner.nextLine()
                readDirectores(nombre)
            }
            3 -> readDirectores()
            4 -> {
                println("Ingrese el nombre del director a actualizar:")
                print("Nombre: ")
                val nombre = scanner.nextLine()
                val directorAct = solicitarDatosDirector()
                updateDirector(nombre, directorAct)
            }
            5 -> {
                println("Ingrese el nombre del director a eliminar:")
                print("Nombre: ")
                val nombre = scanner.nextLine()
                deleteDirector(nombre)
            }
            6 -> return
            else -> println("Opción inválida, por favor seleccione una opción válida.")
        }
        print("----------------------------------------\n")
    }
}

fun solicitarDatosDirector(): Director {
    val scanner = Scanner(System.`in`)
    println("Ingrese los datos del director:")
    print("Nombre: ")
    val nombre = scanner.nextLine()
    print("Fecha de nacimiento (dd-MM-yyyy): ")
    val fechaNacimientoStr = scanner.nextLine()
    val fechaNacimiento = SimpleDateFormat("dd-MM-yyyy").parse(fechaNacimientoStr)
    print("Número de películas dirigidas: ")
    val peliculasDirigidas = scanner.nextInt()
    print("Calificación IMDB: ")
    val calificacionIMDB = scanner.nextDouble()
    print("¿En rodaje? (true/false): ")
    val enRodaje = scanner.nextBoolean()

    return Director(nombre, fechaNacimiento, peliculasDirigidas, calificacionIMDB, enRodaje)
}



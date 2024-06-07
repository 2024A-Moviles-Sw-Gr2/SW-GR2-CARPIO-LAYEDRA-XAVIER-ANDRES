import java.util.*

fun main() {
        println("Hola mundo")

        //Inmutables (No se ouede reasugbar "=")
        val inmutable: String = "Adrian";
        //inmutable = "Vicente" // ERROR!

        //Mutables
        var mutable: String = "Vicente"
        mutable = "Adrian" //OK
        // val > var

        //Duck typing
        var ejemploVariable = "Adrian Eguez"
        var edadEjemplo: Int = 12
        ejemploVariable.trim()
        //ejemploVariable = edadEjemplo // ERROR!
        // Variable primitivas
        val nombre: String = "Adrian"
        val sueldo: Double = 1.2
        val estadoCivil: Char = 'c'
        val mayorEdad: Boolean = true
        //Clases en Java
        val fechaNacimiento: Date = Date()

        //Switch -ªWhen
        val estadoCivilWhen = "C"
        when (estadoCivilWhen) {
                ("C") -> {
                        println("Casado")
                }
                "S" -> {
                        println("Soltero")
                }
                else -> {
                        println("No sabemos")
                }
        }
        val esSoltero = (estadoCivilWhen == "S")
        val coqueteo = if (esSoltero) "Si" else "No"


        calcularSueldo(10.00)
        calcularSueldo(10.00, 15.00, 20.00)
        //Named parameters
        //calcularSueldo(sueldo,tasa,bonoEspecial)
        calcularSueldo(10.00, bonoEspecial = 20.00)
        calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)

        //Uso de clases
        val sumaUno = Suma(1,1)
        val sumaDos = Suma(null,1)
        val sumaTres = Suma(1,null)
        val sumaCuatro = Suma(null,null)
        sumaUno.sumar()
        sumaDos.sumar()
        sumaTres.sumar()
        sumaCuatro.sumar()
        println(Suma.pi)
        println(Suma.elevarAlCuadrado(2))
        println(Suma.historialSumas)

        //Arreglos
        //Estáticos
        val arregloEstatico:Array<Int> = arrayOf<Int>(1,2,3,4,5)

        //Dinámicos
        val arregloDinamico:ArrayList<Int> = arrayListOf()
        arregloDinamico.add(1)
        arregloDinamico.add(2)
        println(arregloDinamico)


        //For each (iterar un arreglo)
        arregloEstatico.forEach{valor:Int ->
                        println(valor)
        }

        //it (elemento iterado)
        arregloDinamico.forEach{ println("Valor actual: ${it}")}

        //MAP -> Modifica el arreglo
        //1. Se envía el nuevo valor de la iteración
        //2. Retorna un NUEVO ARREGLO con los valores de las iteraciones

        val respuestaMap:List<Double> =arregloDinamico
                .map{
                valorAct:Int-> return@map valorAct.toDouble()+100
        }
        val respuestaMapDos = arregloDinamico.map { it+15 }
        println(respuestaMapDos)


        //Filter
        val respuestaFilter: List<Int> = arregloDinamico.filter{
                valorAct:Int ->val mayoresACinco:Boolean=valorAct>5
                return@filter mayoresACinco
        }

        val respuetaFilterDos = arregloDinamico.filter { it <= 5 }
        println(respuetaFilterDos)

        val respuestaAny: Boolean = arregloDinamico.any{
                valorAct:Int -> return@any (valorAct>2)
        }
        println(respuestaAny)

        val respuestaAll: Boolean = arregloDinamico.all{
                valorAct:Int -> return@all (valorAct>=1)
        }
        println(respuestaAll)

        //Reduce
        val respustaReduce: Int = arregloDinamico.reduce{
                acumulado: Int, valorActual: Int ->
                return@reduce (acumulado + valorActual)
        }

        println(respustaReduce)

        //Ejemplo para un carrito de compras
        //return @reduce acumulado+(itemCarrito.cantidad * itemCarrito.precio

}



//Funciones
//void-> unit

fun imprimirNombre(nombre: String): Unit {
        println("Nombre: ${nombre}") //Template Strings
}

fun calcularSueldo(
        sueldo: Double, //Requerido
        tasa: Double = 12.00, //Opcional (defecto)
        bonoEspecial: Double? = null //Opcional (nullable)
        // Variable?->"?" Es Nullable(osea que puede en algun momento ser nulo)
): Double {
        // Int ->Int? (nullable)
        // String -> String? (nullable)
        // Date -> Date? (nullable)
        if (bonoEspecial == null) {
                return sueldo * (100 / tasa)
        } else {
                return sueldo * (100 / tasa) * bonoEspecial
        }
}


abstract class NumerosJava {
        protected val numeroUno: Int
        private val numeroDos: Int

        constructor(
                uno: Int,
                dos: Int
        ) {
                this.numeroUno = uno
                this.numeroDos = dos
                println("Inicializando")
        }
}

abstract class Numeros(
        //Constructor Primario
        // Caso 1) Parametro normal
        // uno:Int, (parametro (sin modificar acceso))

        //Caso 2) Parametro y propiedad (atributo) (private
        //private var uno:Int (propiedad "instancia.uno")
        protected val numeroUno: Int, //isntancia.numeroUno
        protected val numeroDos: Int, //instancia.numeroDos
        //parametroInutil:String, //PARAMETRO
) {
        init { //bloque constructor primario (opcional)
                this.numeroUno
                this.numeroDos
                //this.parametroInutil //ERROR NO EXISTE
                println("Inicializando")
        }

}


class Suma(
        //Constructor primario
        unoParametro: Int,
        dosParametro: Int,
) : Numeros(
        unoParametro,
        dosParametro
) {
        public val soyPublicoExplicito: String = "Explicito" //Publicos
        val soyPublicoImplicito: String = "Implicito" //Publicas (propiedades, metodos)

        init {
                this.numeroUno
                this.numeroDos
                //this.unoParametro //EROR NO EXISTE
                //this.unoPara
                numeroUno //this.OPCIONAL (propiedades, metodos)
                numeroDos //this.OPCIONAL (propiedades,metodos)
                this.soyPublicoExplicito
                soyPublicoImplicito //this.opcional (propiedades,métodos)
        }

        constructor (//Constructor secundario
                uno:Int?,
                dos:Int
        ):this(
                if(uno==null) 0 else uno,
                dos
        )

        constructor (//Constructor tercero
                uno:Int,
                dos:Int?
        ):this(
                uno,
                if(dos==null) 0 else dos,
        )

        constructor (//Constructor cuarto
                uno:Int?,
                dos:Int?
        ):this(
                if(uno==null) 0 else uno,
                if(dos==null) 0 else dos,
        )


        // public fun sumar():Int (modificar "public" es OPCIONAL)
        fun sumar(): Int {
                val total = numeroUno + numeroDos
                //Suma.agregarHistorial(total) ("Suma." o "NombreClase." es OPCIONAL)
                agregarHistorial(total)
                return total
        }



        //propiedad de una clase
        companion object { //Comparte entre todos
                //funciones y variables
                val pi = 3.14
                fun elevarAlCuadrado(num: Int): Int {
                        return num * num
                }

                val historialSumas = arrayListOf<Int>()
                fun agregarHistorial(valorTotalSuma: Int) {
                        historialSumas.add(valorTotalSuma)
                }

        }
}



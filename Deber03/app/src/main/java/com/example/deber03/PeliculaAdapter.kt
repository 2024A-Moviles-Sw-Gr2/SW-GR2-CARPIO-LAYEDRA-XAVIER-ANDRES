package com.example.deber03

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat

class PeliculaAdapter(
    private val context: Context,
    private var peliculaList: MutableList<Pelicula>
) : BaseAdapter() {

    override fun getCount(): Int {
        return peliculaList.size
    }

    override fun getItem(position: Int): Pelicula {
        return peliculaList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_pelicula, parent, false)

        val pelicula = getItem(position)

        val tvNombre = view.findViewById<TextView>(R.id.tv_nombre_pelicula)
        val tvFechaEstreno = view.findViewById<TextView>(R.id.tv_fecha_estreno)

        tvNombre.text = pelicula.nombre
        tvFechaEstreno.text = SimpleDateFormat("dd/MM/yyyy").format(pelicula.fechaEstreno)
        return view
    }

    // Método para limpiar la lista de directores
    fun clear() {
        peliculaList.clear()
        notifyDataSetChanged()
    }

    // Método para añadir una lista de directores
    fun addAll(peliculas: List<Pelicula>) {
        peliculaList.addAll(peliculas)
        notifyDataSetChanged()
    }
}

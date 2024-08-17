package com.example.deber03

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat

class DirectorAdapter(
    private val context: Context,
    private var directorList: MutableList<Director>
) : BaseAdapter() {

    override fun getCount(): Int {
        return directorList.size
    }


    override fun getItem(position: Int): Director {
        return directorList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_director, parent, false)

        val director = getItem(position)

        val tvNombre = view.findViewById<TextView>(R.id.tv_nombre_director)
        val tvFechaNacimiento = view.findViewById<TextView>(R.id.tv_fecha_nacimiento)
        val tvCalificacionIMDB = view.findViewById<TextView>(R.id.tv_calificacion_imdb)

        tvNombre.text = director.nombre
        tvFechaNacimiento.text = SimpleDateFormat("dd/MM/yyyy").format(director.fechaNacimiento)
        tvCalificacionIMDB.text = "IMDB: ${director.calificacionIMDB}"

        return view
    }

    // Método para limpiar la lista de directores
    fun clear() {
        directorList.clear()
        notifyDataSetChanged()
    }

    // Método para añadir una lista de directores
    fun addAll(directores: List<Director>) {
        directorList.addAll(directores)
        notifyDataSetChanged()
    }
    fun remove(position: Int) {
        directorList.removeAt(position)
        notifyDataSetChanged()
    }

}

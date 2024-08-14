package com.example.proyectoalbumes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CancionAdapter(private val canciones: MutableList<Cancion>) : RecyclerView.Adapter<CancionAdapter.CancionViewHolder>() {

    private var onItemLongClickListener: ((View, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cancion_item, parent, false)
        return CancionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return canciones.size
    }

    override fun onBindViewHolder(holder: CancionViewHolder, position: Int) {
        val cancion = canciones[position]
        holder.bind(cancion)

        // Configurar el listener de long click
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(it, position)
            true
        }
    }

    class CancionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cancionTitle: TextView = itemView.findViewById(R.id.cancionName)
        private val cancionDur: TextView = itemView.findViewById(R.id.duracionText)

        fun bind(cancion: Cancion) {
            cancionTitle.text = cancion.title
            cancionDur.text = cancion.duracion.toString()
        }
    }

    fun addCancion(cancion: Cancion) {
        canciones.add(cancion)
        notifyItemInserted(canciones.size - 1)
    }

    fun getCancion(position: Int): Cancion {
        return canciones[position]
    }

    fun removeCancion(position: Int) {
        canciones.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setOnItemLongClickListener(listener: (View, Int) -> Unit) {
        onItemLongClickListener = listener
    }
}

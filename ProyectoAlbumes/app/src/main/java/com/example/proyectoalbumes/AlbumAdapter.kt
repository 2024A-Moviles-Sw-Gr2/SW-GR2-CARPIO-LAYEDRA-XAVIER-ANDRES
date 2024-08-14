package com.example.proyectoalbumes

import Album
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter(
    val albums: MutableList<Album>,
    private val onAlbumClick: (Album) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.albumes_layout, parent, false)
        return AlbumViewHolder(view)
    }
    fun addAlbum(album: Album) {
        albums.add(album)  // Añade el nuevo álbum a la lista
        notifyItemInserted(albums.size - 1)  // Notifica al adaptador que se ha insertado un nuevo elemento
    }

    fun updateAlbum(position: Int, updatedAlbum: Album) {
        albums[position] = updatedAlbum
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album, onAlbumClick)

        // Agregar listener para el long click
        holder.itemView.setOnLongClickListener {
            showPopupMenu(holder.itemView, album, position)
            true
        }
    }

    private fun showPopupMenu(view: View, album: Album, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.song_options_menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    // Lógica para editar el álbum
                    (view.context as MainActivity).showEditAlbumDialog(album, position)
                    true
                }
                R.id.menu_delete -> {
                    // Lógica para eliminar el álbum
                    (view.context as MainActivity).deleteAlbum(album, position)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val albumTitle: TextView = itemView.findViewById(R.id.albumName)
        private val albumImageView: ImageView = itemView.findViewById(R.id.albumCover)

        fun bind(album: Album, onAlbumClick: (Album) -> Unit) {
            val bitmap = album.image?.let { BitmapFactory.decodeByteArray(album.image, 0, it.size) }
            albumImageView.setImageBitmap(bitmap)
            albumTitle.text = album.name
            itemView.setOnClickListener {
                onAlbumClick(album)
            }
        }
    }

}
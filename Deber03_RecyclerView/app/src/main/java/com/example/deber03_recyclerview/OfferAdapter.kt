package com.example.deber03_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber03_recyclerview.R
import com.example.deber03_recyclerview.*

class OfferAdapter(private val offerList: List<Offer>) : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val offerDescription: TextView = itemView.findViewById(R.id.offer_description)
        val offerImage: ImageView = itemView.findViewById(R.id.offer_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offer_item, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offerList[position]
        holder.offerDescription.text = offer.description
        holder.offerImage.setImageResource(offer.image)
    }

    override fun getItemCount(): Int = offerList.size
}

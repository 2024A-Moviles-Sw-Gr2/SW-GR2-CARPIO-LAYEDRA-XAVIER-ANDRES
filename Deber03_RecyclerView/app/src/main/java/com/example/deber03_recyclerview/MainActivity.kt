package com.example.deber03_recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var recyclerViewOffers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewProducts = findViewById(R.id.recycler_view_products)
        recyclerViewOffers = findViewById(R.id.recycler_view_offers)

        val products = listOf(
            Product("Big Mac", R.drawable.big_mac, 5.99),
            Product("McChicken", R.drawable.mcchicken, 4.99),
            Product("French Fries", R.drawable.french_fries, 2.99)
        )

        val offers = listOf(
            Offer("Buy one Big Mac, get one free!", R.drawable.offer_big_mac),
            Offer("Free Fries with any purchase over $10", R.drawable.offer_fries)
        )

        recyclerViewProducts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewProducts.adapter = ProductAdapter(products)

        recyclerViewOffers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewOffers.adapter = OfferAdapter(offers)
    }
}

package com.principe.felipe.finango_d1.modelos

data class Crypto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String, // URL de la imagen
    val current_price: Double // Precio actual
)

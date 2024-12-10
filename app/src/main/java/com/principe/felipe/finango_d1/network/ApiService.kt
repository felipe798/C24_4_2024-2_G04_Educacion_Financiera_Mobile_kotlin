package com.principe.felipe.finango_d1.network


import com.principe.felipe.finango_d1.model.Crypto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("coins/markets")
    suspend fun getCryptocurrencies(
        @Header("x-cg-pro-api-key") apiKey: String, // Clave para autenticación
        @Query("vs_currency") currency: String,    // Moneda de referencia
        @Query("order") order: String = "market_cap_desc", // Ordenar por capitalización
        @Query("per_page") perPage: Int = 20,      // Número de resultados por página
        @Query("page") page: Int = 1,             // Número de página
        @Query("sparkline") sparkline: Boolean = false // Incluir datos de tendencia
    ): List<Crypto>
}

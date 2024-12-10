package com.principe.felipe.finango_d1.repository

import com.principe.felipe.finango_d1.modelos.Crypto
import com.principe.felipe.finango_d1.network.RetrofitClient

class CryptoRepository {
    /**
     * Obtiene la lista de criptomonedas desde la API.
     *
     * @param apiKey La clave de la API para la autenticación.
     * @param currency La moneda de referencia (por defecto es "usd").
     * @return Una lista de objetos [Crypto].
     */
    suspend fun getCryptocurrencies(apiKey: String, currency: String = "usd"): List<Crypto> {
        return RetrofitClient.instance.getCryptocurrencies(
            apiKey = apiKey,
            currency = currency
        )
    }
}

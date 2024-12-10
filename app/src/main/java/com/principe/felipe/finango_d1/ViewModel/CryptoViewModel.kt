package com.principe.felipe.finango_d1.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.principe.felipe.finango_d1.model.Crypto
import com.principe.felipe.finango_d1.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CryptoViewModel(private val repository: CryptoRepository) : ViewModel() {
    private val _cryptos = MutableStateFlow<List<Crypto>>(emptyList())
    val cryptos: StateFlow<List<Crypto>> get() = _cryptos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchCryptocurrencies(apiKey: String) {
        viewModelScope.launch {
            try {
                val data = repository.getCryptocurrencies(apiKey)
                _cryptos.value = data
                _error.value = null // Limpia errores previos
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Error al obtener datos: ${e.message}"
            }
        }
    }
}

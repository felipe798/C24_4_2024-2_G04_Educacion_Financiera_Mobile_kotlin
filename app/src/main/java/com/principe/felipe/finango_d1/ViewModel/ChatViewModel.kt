package com.principe.felipe.finango_d1.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.principe.felipe.finango_d1.network.GPTRequest
import com.principe.felipe.finango_d1.network.GPTRetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _response = MutableStateFlow<String?>(null)
    val response: StateFlow<String?> get() = _response

    fun sendMessageToGPT(message: String) {
        viewModelScope.launch {
            try {
                val request = GPTRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(mapOf("role" to "user", "content" to message))
                )
                val result = GPTRetrofitClient.instance.getGPTResponse(request)

                // Extraer el contenido del mensaje de la primera opción de la respuesta
                val messageContent = result.choices.firstOrNull()?.message?.content

                _response.value = messageContent ?: "No response from GPT"
            } catch (e: Exception) {
                _response.value = "Error: ${e.message}"
            }
        }
    }
}

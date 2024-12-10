package com.principe.felipe.finango_d1.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.principe.felipe.finango_d1.databinding.ActivityChatBinding
import com.principe.felipe.finango_d1.model.Message
import com.principe.felipe.finango_d1.ViewModel.ChatViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private val chatAdapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView
        binding.recyclerChat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }

        // Configurar acción para el botón "Enviar"
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                chatAdapter.addMessage(Message(message, isUser = true)) // Agregar mensaje del usuario
                viewModel.sendMessageToGPT(message)
                binding.etMessage.text.clear()
            }
        }

        // Observador para respuestas desde el ViewModel
        lifecycleScope.launch {
            viewModel.response.collectLatest { response ->
                response?.let {
                    chatAdapter.addMessage(Message(it, isUser = false)) // Agregar respuesta de GPT
                }
            }
        }
    }
}

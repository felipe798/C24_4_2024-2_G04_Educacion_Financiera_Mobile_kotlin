package com.principe.felipe.finango_d1.model

data class Message(
    val text: String,
    val isUser: Boolean // `true` si el mensaje es del usuario, `false` si es de GPT.
)

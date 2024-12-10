package com.principe.felipe.finango_d1.model

data class Question(
    val id: String = "",
    val question: String = "",
    val options: Map<String, String>? = null, // Opciones en formato clave-valor (A, B, C, D)
    val correctAnswer: String = "" // La respuesta correcta
)

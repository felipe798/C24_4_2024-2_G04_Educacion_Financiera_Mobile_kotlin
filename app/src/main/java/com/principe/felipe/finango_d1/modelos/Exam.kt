package com.principe.felipe.finango_d1.modelos

data class Exam(
    val questions: Map<String, Question>? = null // Relación con preguntas
)

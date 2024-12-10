package com.principe.felipe.finango_d1.model

data class Exam(
    val questions: Map<String, Question>? = null // Relación con preguntas
)

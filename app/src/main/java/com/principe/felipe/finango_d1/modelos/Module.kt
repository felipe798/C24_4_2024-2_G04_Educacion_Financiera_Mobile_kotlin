package com.principe.felipe.finango_d1.modelos

data class Module(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val topics: Map<String, Topic>? = null, // Relación con temas
    val exam: Exam? = null // Relación con exámenes
)

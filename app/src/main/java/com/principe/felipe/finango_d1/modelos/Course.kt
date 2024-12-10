package com.principe.felipe.finango_d1.modelos

data class Course(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val modules: Map<String, Module>? = null, // Relación con módulos
    val comments: Map<String, Comment>? = null // Relación con comentarios
)

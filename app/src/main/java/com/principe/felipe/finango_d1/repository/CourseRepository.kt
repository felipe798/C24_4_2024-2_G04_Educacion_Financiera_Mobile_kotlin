package com.principe.felipe.finango_d1.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.principe.felipe.finango_d1.Modelos.Comment
import com.principe.felipe.finango_d1.Modelos.Course
import com.principe.felipe.finango_d1.Modelos.Exam
import com.principe.felipe.finango_d1.Modelos.Module
import com.principe.felipe.finango_d1.Modelos.Topic
import com.principe.felipe.finango_d1.Utils.Constants
import kotlinx.coroutines.tasks.await

class CourseRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("courses")

    /**
     * Obtiene la lista de cursos desde Firebase.
     * @return Lista de objetos `Course`.
     */
    suspend fun getCourses(): List<Course> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { it.getValue(Course::class.java) }
    }

    /**
     * Inscribe a un usuario en un curso.
     * @param courseId El ID del curso en el que el usuario se inscribirá.
     * @param userId El ID del usuario que se inscribe.
     */
    suspend fun enrollInCourse(courseId: String, userId: String) {
        database.child(courseId).child("enrollments").child(userId).setValue(true).await()
    }

    /**
     * Obtiene los módulos de un curso específico.
     * @param courseId El ID del curso.
     * @return Los módulos del curso en un mapa.
     */
    suspend fun getModules(courseId: String): Map<String, Module>? {
        val snapshot = database.child(courseId).child("modules").get().await()
        return snapshot.children.associate { it.key!! to it.getValue(Module::class.java)!! }
    }

    /**
     * Obtiene los comentarios de un curso específico.
     * @param courseId El ID del curso.
     * @return Los comentarios del curso en un mapa.
     */
    suspend fun getComments(courseId: String): Map<String, Comment>? {
        val snapshot = database.child(courseId).child("comments").get().await()
        return snapshot.children.associate { it.key!! to it.getValue(Comment::class.java)!! }
    }

    /**
     * Agrega un comentario a un curso.
     * @param courseId El ID del curso.
     * @param userId El ID del usuario que comenta.
     * @param content El contenido del comentario.
     */
    suspend fun addComment(courseId: String, userId: String, content: String) {
        val commentId = database.child(courseId).child("comments").push().key!!
        val newComment = Comment(
            id = commentId,
            content = content,
            createdAt = System.currentTimeMillis().toString()
        )
        database.child(courseId).child("comments").child(userId).setValue(newComment).await()
    }

    /**
     * Obtiene los topics de un módulo específico.
     * @param moduleId El ID del módulo.
     * @return Los topics del módulo en un mapa.
     */
    suspend fun getTopics(moduleId: String): Map<String, Topic>? {
        val snapshot = database.child(Constants.MODULES_PATH).child(moduleId).child(Constants.TOPICS_PATH).get().await()
        return snapshot.children.associate { it.key!! to it.getValue(Topic::class.java)!! }
    }

    /**
     * Obtiene los topics de un módulo específico de Firebase usando el curso y el módulo.
     * @param courseId El ID del curso.
     * @param moduleId El ID del módulo.
     * @return Los topics del módulo en un mapa.
     */
    suspend fun getTopicsFromFirebase(courseId: String, moduleId: String): Map<String, Topic>? {
        return try {
            val snapshot = database.child(courseId).child("modules").child(moduleId).child("topics").get().await()
            if (snapshot.exists()) {
                snapshot.children.associate { it.key!! to it.getValue(Topic::class.java)!! }
            } else {
                Log.e("CourseRepository", "No topics found for moduleId: $moduleId")
                emptyMap()
            }
        } catch (e: Exception) {
            Log.e("CourseRepository", "Error fetching topics: ${e.message}", e)
            emptyMap()
        }
    }





}

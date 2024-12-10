package com.principe.felipe.finango_d1.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.principe.felipe.finango_d1.modelos.Comment
import com.principe.felipe.finango_d1.modelos.Course
import com.principe.felipe.finango_d1.modelos.Module
import com.principe.felipe.finango_d1.modelos.Topic
import com.principe.felipe.finango_d1.utils.Constants
import kotlinx.coroutines.tasks.await

class CourseRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.COURSES_PATH)

    suspend fun getCourses(): List<Course> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { it.getValue(Course::class.java) }
    }

    suspend fun enrollInCourse(courseId: String, userId: String) {
        database.child(courseId).child("enrollments").child(userId).setValue(true).await()
    }

    suspend fun getModules(courseId: String): Map<String, Module>? {
        val snapshot = database.child(courseId).child(Constants.MODULES_PATH).get().await()
        return snapshot.children.associate { it.key!! to it.getValue(Module::class.java)!! }
    }

    suspend fun getComments(courseId: String): Map<String, Comment>? {
        val snapshot = database.child(courseId).child(Constants.COMMENTS_PATH).get().await()
        return snapshot.children.associate { it.key!! to it.getValue(Comment::class.java)!! }
    }

    suspend fun addComment(courseId: String, userId: String, content: String) {
        val commentId = database.child(courseId).child(Constants.COMMENTS_PATH).push().key!!
        val newComment = Comment(
            id = commentId,
            content = content,
            createdAt = System.currentTimeMillis().toString()
        )
        database.child(courseId).child(Constants.COMMENTS_PATH).child(commentId).setValue(newComment).await()
    }

    suspend fun getTopics(moduleId: String): Map<String, Topic>? {
        val snapshot = database.child(Constants.MODULES_PATH).child(moduleId).child(Constants.TOPICS_PATH).get().await()
        return snapshot.children.associate { it.key!! to it.getValue(Topic::class.java)!! }
    }

    suspend fun getTopicsFromFirebase(courseId: String, moduleId: String): Map<String, Topic>? {
        return try {
            val snapshot = database.child(courseId).child(Constants.MODULES_PATH).child(moduleId).child(Constants.TOPICS_PATH).get().await()
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

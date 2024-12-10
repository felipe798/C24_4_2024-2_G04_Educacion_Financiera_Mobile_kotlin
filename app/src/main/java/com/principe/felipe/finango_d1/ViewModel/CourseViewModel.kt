package com.principe.felipe.finango_d1.ViewModel


import androidx.lifecycle.viewModelScope
import com.principe.felipe.finango_d1.repository.CourseRepository
import com.principe.felipe.finango_d1.model.Course
import com.principe.felipe.finango_d1.model.Module
import com.principe.felipe.finango_d1.model.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.ViewModel
import com.principe.felipe.finango_d1.modelo.Comment

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> get() = _courses

    private val _comments = MutableStateFlow<Map<String, Comment>>(emptyMap())
    val comments: StateFlow<Map<String, Comment>> get() = _comments

    private val _modules = MutableStateFlow<Map<String, Module>>(emptyMap())
    val modules: StateFlow<Map<String, Module>> get() = _modules

    private val _topics = MutableStateFlow<Map<String, Topic>>(emptyMap())
    val topics: StateFlow<Map<String, Topic>> get() = _topics

    /**
     * Carga todos los cursos desde el repositorio.
     */
    fun loadCourses() {
        viewModelScope.launch {
            try {
                _courses.value = repository.getCourses()
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error loading courses: ${e.message}", e)
                _courses.value = emptyList()
            }
        }
    }

    /**
     * Inscribe a un usuario en un curso específico.
     * @param courseId El ID del curso.
     * @param userId El ID del usuario.
     */
    fun enrollInCourse(courseId: String, userId: String) {
        viewModelScope.launch {
            try {
                repository.enrollInCourse(courseId, userId)
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error enrolling user: ${e.message}", e)
            }
        }
    }

    /**
     * Carga los módulos de un curso específico.
     * @param courseId El ID del curso.
     */
    fun loadModules(courseId: String) {
        viewModelScope.launch {
            try {
                val modules = repository.getModules(courseId)
                _modules.value = modules ?: emptyMap()
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error loading modules: ${e.message}", e)
                _modules.value = emptyMap()
            }
        }
    }

    /**
     * Carga los comentarios de un curso específico.
     * @param courseId El ID del curso.
     */
    fun loadComments(courseId: String) {
        viewModelScope.launch {
            try {
                val comments = repository.getComments(courseId)
                _comments.value = comments ?: emptyMap()
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error loading comments: ${e.message}", e)
                _comments.value = emptyMap()
            }
        }
    }

    /**
     * Carga los topics de un módulo específico desde Firebase.
     * @param courseId El ID del curso.
     * @param moduleId El ID del módulo.
     */
    fun loadTopicsFromFirebase(courseId: String, moduleId: String) {
        viewModelScope.launch {
            try {
                val topics = repository.getTopicsFromFirebase(courseId, moduleId)
                _topics.value = topics ?: emptyMap()
                Log.d("CourseViewModel", "Topics loaded successfully: $topics")
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error loading topics: ${e.message}", e)
                _topics.value = emptyMap()
            }
        }
    }

    /**
     * Agrega un comentario a un curso.
     * @param courseId El ID del curso.
     * @param userId El ID del usuario que comenta.
     * @param content El contenido del comentario.
     */
    fun addComment(courseId: String, userId: String, content: String) {
        viewModelScope.launch {
            try {
                repository.addComment(courseId, userId, content)
                loadComments(courseId) // Recarga los comentarios después de agregar uno nuevo
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error adding comment: ${e.message}", e)
            }
        }
    }





}

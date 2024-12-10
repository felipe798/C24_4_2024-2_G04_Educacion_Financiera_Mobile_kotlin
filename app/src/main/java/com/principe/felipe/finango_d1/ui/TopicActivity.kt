package com.principe.felipe.finango_d1.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.principe.felipe.finango_d1.databinding.ActivityTopicBinding
import com.principe.felipe.finango_d1.ViewModel.CourseViewModel
import com.principe.felipe.finango_d1.ViewModel.CourseViewModelFactory
import com.principe.felipe.finango_d1.repository.CourseRepository
import kotlinx.coroutines.launch

class TopicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTopicBinding
    private val viewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(CourseRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener COURSE_ID y MODULE_ID desde el intent
        val courseId = intent.getStringExtra("COURSE_ID") ?: run {
            Log.e("TopicActivity", "No COURSE_ID provided in intent")
            finish()
            return
        }

        val moduleId = intent.getStringExtra("MODULE_ID") ?: run {
            Log.e("TopicActivity", "No MODULE_ID provided in intent")
            finish()
            return
        }

        Log.d("TopicActivity", "Received COURSE_ID: $courseId, MODULE_ID: $moduleId")

        // Configurar el adaptador para la lista de topics
        val adapter = TopicAdapter()
        binding.recyclerTopics.layoutManager = LinearLayoutManager(this)
        binding.recyclerTopics.adapter = adapter

        // Observa los cambios en los módulos y actualiza el encabezado
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.modules.collect { modules ->
                    val module = modules[moduleId]
                    binding.tvModuleName.text = module?.name ?: "Módulo no encontrado"
                    binding.tvModuleDescription.text = module?.description ?: ""
                }
            }
        }

        // Observa los cambios en los topics y actualiza la lista
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topics.collect { topics ->
                    adapter.submitList(topics.values.toList())
                }
            }
        }

        // Cargar datos del módulo y los topics
        viewModel.loadModules(courseId)
        viewModel.loadTopicsFromFirebase(courseId, moduleId)
    }
}

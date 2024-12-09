package com.principe.felipe.finango_d1.UI

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
    private val viewModel: CourseViewModel by viewModels { CourseViewModelFactory(CourseRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        // Configura el RecyclerView
        val adapter = TopicAdapter()
        binding.recyclerTopics.layoutManager = LinearLayoutManager(this)
        binding.recyclerTopics.adapter = adapter

        // Observa los datos del módulo y actualiza la interfaz
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.modules.collect { modules ->
                    val module = modules[moduleId]
                    if (module != null) {
                        binding.tvModuleName.text = module.name
                        binding.tvModuleDescription.text = module.description
                    } else {
                        Log.e("TopicActivity", "Module not found for moduleId: $moduleId")
                        binding.tvModuleName.text = "Módulo no encontrado"
                        binding.tvModuleDescription.text = ""
                    }
                }
            }
        }

        // Observa los datos de los temas y actualiza el adaptador
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topics.collect { topics ->
                    val moduleTopics = topics.values.toList()
                    if (moduleTopics.isNotEmpty()) {
                        adapter.submitList(moduleTopics)
                    } else {
                        Log.e("TopicActivity", "No topics found for moduleId: $moduleId")
                        adapter.submitList(emptyList())
                    }
                }
            }
        }

        // Carga los datos necesarios desde Firebase
        Log.d("TopicActivity", "Loading module and topics from Firebase for courseId: $courseId, moduleId: $moduleId")
        viewModel.loadModules(courseId) // Carga los módulos
        viewModel.loadTopicsFromFirebase(courseId, moduleId) // Carga los temas del módulo
    }
}

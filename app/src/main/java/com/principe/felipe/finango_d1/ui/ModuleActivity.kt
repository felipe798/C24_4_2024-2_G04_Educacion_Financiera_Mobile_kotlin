package com.principe.felipe.finango_d1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.principe.felipe.finango_d1.ViewModel.CourseViewModel
import com.principe.felipe.finango_d1.ViewModel.CourseViewModelFactory
import com.principe.felipe.finango_d1.databinding.ActivityModuleBinding
import com.principe.felipe.finango_d1.repository.CourseRepository
import kotlinx.coroutines.flow.collectLatest

class ModuleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModuleBinding
    private val viewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(CourseRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el ID del curso desde el intent
        val courseId = intent.getStringExtra("COURSE_ID")
        if (courseId.isNullOrEmpty()) {
            Log.e("ModuleActivity", "COURSE_ID is null or empty")
            finish()
            return
        }

        // Configurar el adaptador para la lista de módulos
        val adapter = ModuleAdapter { moduleId ->
            // Iniciar TopicActivity con el ID del curso y módulo seleccionados
            val intent = Intent(this, TopicActivity::class.java).apply {
                putExtra("COURSE_ID", courseId)
                putExtra("MODULE_ID", moduleId)
            }
            startActivity(intent)
        }

        binding.recyclerModules.layoutManager = LinearLayoutManager(this)
        binding.recyclerModules.adapter = adapter

        // Observar los cambios en los módulos y actualizar la lista
        lifecycleScope.launchWhenStarted {
            viewModel.modules.collectLatest { modules ->
                adapter.submitList(modules.values.toList())
            }
        }

        // Cargar los módulos del curso
        viewModel.loadModules(courseId)
    }
}

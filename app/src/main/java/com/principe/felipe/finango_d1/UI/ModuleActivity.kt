package com.principe.felipe.finango_d1.UI

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.principe.felipe.finango_d1.databinding.ActivityModuleBinding
import com.principe.felipe.finango_d1.ViewModel.CourseViewModel
import com.principe.felipe.finango_d1.ViewModel.CourseViewModelFactory
import com.principe.felipe.finango_d1.repository.CourseRepository
import android.content.Intent

class ModuleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModuleBinding
    private val viewModel: CourseViewModel by viewModels { CourseViewModelFactory(CourseRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val courseId = intent.getStringExtra("COURSE_ID") ?: return.also {
            Log.e("ModuleActivity", "COURSE_ID is null")
        }

        val adapter = ModuleAdapter { moduleId ->
            Log.d("ModuleActivity", "Navigating to topics for moduleId: $moduleId")
            startActivity(Intent(this, TopicActivity::class.java).apply {
                putExtra("COURSE_ID", courseId)
                putExtra("MODULE_ID", moduleId)
            })
        }


        binding.recyclerModules.layoutManager = LinearLayoutManager(this)
        binding.recyclerModules.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.modules.collect { modules ->
                Log.d("ModuleActivity", "Modules loaded: $modules")
                adapter.submitList(modules.values.toList())
            }
        }



        viewModel.loadModules(courseId)
    }
}
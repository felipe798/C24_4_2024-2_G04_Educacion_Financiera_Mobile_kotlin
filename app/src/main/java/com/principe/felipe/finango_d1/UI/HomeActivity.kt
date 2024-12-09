package com.principe.felipe.finango_d1.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.principe.felipe.finango_d1.LoginActivity
import com.principe.felipe.finango_d1.repository.CourseRepository
import com.principe.felipe.finango_d1.ViewModel.CourseViewModel
import com.principe.felipe.finango_d1.ViewModel.CourseViewModelFactory
import com.principe.felipe.finango_d1.databinding.ActivityHomeBinding
import com.principe.felipe.finango_d1.UI.ModuleActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: CourseViewModel by viewModels { CourseViewModelFactory(CourseRepository()) }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CourseAdapter { courseId ->
            startActivity(Intent(this, ModuleActivity::class.java).apply {
                putExtra("COURSE_ID", courseId)
            })
        }

        binding.recyclerCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerCourses.adapter = adapter

        lifecycleScope.launch {
            viewModel.courses.collect { courses ->
                adapter.submitList(courses)
            }
        }

        viewModel.loadCourses()

        // Cerrar sesión
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}

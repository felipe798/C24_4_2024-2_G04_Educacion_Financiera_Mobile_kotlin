package com.principe.felipe.finango_d1.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.principe.felipe.finango_d1.LoginActivity
import com.principe.felipe.finango_d1.ViewModel.CourseViewModel
import com.principe.felipe.finango_d1.ViewModel.CourseViewModelFactory
import com.principe.felipe.finango_d1.databinding.ActivityHomeBinding
import com.principe.felipe.finango_d1.repository.CourseRepository
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(CourseRepository())
    }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el adaptador de cursos
        val adapter = CourseAdapter(this) { courseId ->
            val intent = Intent(this, ModuleActivity::class.java)
            intent.putExtra("COURSE_ID", courseId)
            startActivity(intent)
        }

        binding.recyclerCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerCourses.adapter = adapter

        // Observar los cursos y actualizar la lista
        lifecycleScope.launch {
            viewModel.courses.collect { courses ->
                adapter.submitList(courses)
            }
        }

        // Cargar la lista de cursos
        viewModel.loadCourses()

        // Botón de cierre de sesión
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Botón para ir a la vista de criptomonedas
        binding.btnViewCryptos.setOnClickListener {
            val intent = Intent(this, CryptoListActivity::class.java)
            startActivity(intent)
        }
    }
}

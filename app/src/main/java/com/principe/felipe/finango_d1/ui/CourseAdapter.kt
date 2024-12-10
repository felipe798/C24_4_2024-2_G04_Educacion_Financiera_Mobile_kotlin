package com.principe.felipe.finango_d1.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.principe.felipe.finango_d1.modelos.Course
import com.principe.felipe.finango_d1.databinding.ItemCourseBinding

class CourseAdapter(
    private val context: Context,
    private val onEnrollClick: (String) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private val courses = mutableListOf<Course>()

    fun submitList(newCourses: List<Course>) {
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size

    inner class CourseViewHolder(private val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course) {
            binding.tvCourseName.text = course.name
            binding.tvCourseDescription.text = course.description
            binding.btnEnroll.setOnClickListener { onEnrollClick(course.id) }
        }
    }
}

package com.principe.felipe.finango_d1.UI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.principe.felipe.finango_d1.databinding.ItemModuleBinding
import com.principe.felipe.finango_d1.Modelos.Module

class ModuleAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>() {

    private val modules = mutableListOf<Module>()

    fun submitList(newModules: List<Module>) {
        modules.clear()
        modules.addAll(newModules)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val binding = ItemModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ModuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        holder.bind(modules[position])
    }

    override fun getItemCount(): Int = modules.size

    inner class ModuleViewHolder(private val binding: ItemModuleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(module: Module) {
            binding.tvModuleName.text = module.name
            binding.tvModuleDescription.text = module.description
            binding.root.setOnClickListener { onClick(module.id) }
        }
    }
}
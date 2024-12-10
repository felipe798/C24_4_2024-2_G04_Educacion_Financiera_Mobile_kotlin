package com.principe.felipe.finango_d1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.principe.felipe.finango_d1.modelos.Topic
import com.principe.felipe.finango_d1.databinding.ItemTopicBinding

class TopicAdapter : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private val topics = mutableListOf<Topic>()

    fun submitList(newTopics: List<Topic>) {
        topics.clear()
        topics.addAll(newTopics)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(topics[position])
    }

    override fun getItemCount(): Int = topics.size

    inner class TopicViewHolder(private val binding: ItemTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(topic: Topic) {
            binding.tvTopicName.text = topic.name
            binding.tvTopicContent.text = topic.content // Usa 'content' como texto a mostrar
        }
    }
}

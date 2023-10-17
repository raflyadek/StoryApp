package com.example.storyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.Story
import com.example.storyapp.databinding.ItemUserBinding

class ListStoryAdapter(private var response: List<Story>):
    RecyclerView.Adapter<ListStoryAdapter.ListStoryViewHolder>() {

    private lateinit var onItemClickCallback : OnItemClickCallback
    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
            class ListStoryViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
                fun bind(storyResponse: Story) {
                    with(binding) {
                        binding.tvItemDesc.text = storyResponse.description
                        binding.tvItemName.text = storyResponse.name
                        Glide.with(binding.root.context)
                            .load(storyResponse.photoUrl)
                            .into(imgItemUser)

                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        val itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryViewHolder(itemUserBinding)
    }

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        val story = response[position]
        holder.bind(story)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(story)
        }
    }
}
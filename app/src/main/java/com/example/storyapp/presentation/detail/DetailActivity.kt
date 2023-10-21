package com.example.storyapp.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.data.remote.Story
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.util.Constant.EXTRA_DETAIL

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val story = intent.getStringExtra(EXTRA_DETAIL)
        showDetailStory(story)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun observeViewModel

    private fun showDetailStory(story: Story) {
        if (story != null) {
            binding.apply {
                tvNameDetail.text = story.name
                tvDescDetail.text = story.description
                Glide.with(this@DetailActivity)
                    .load(story.photoUrl)
                    .into(imgDetail)
            }
        }
    }
}
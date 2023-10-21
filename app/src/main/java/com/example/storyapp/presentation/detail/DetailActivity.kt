package com.example.storyapp.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.DetailStory
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.data.remote.Story
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.util.Constant.EXTRA_DETAIL
import com.example.storyapp.util.Result

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_DETAIL) ?: ""
        observeViewModel(id)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun observeViewModel(id: String) {
        val factory = ViewModelFactory.getInstance(this)
        val viewModel: DetailViewModel by viewModels { factory }
        viewModel.detailStories(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        showDetailStory(result.data.story)
                    }
                    is Result.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showDetailStory(story: DetailStory) {
            binding.apply {
                tvNameDetail.text = story.name
                tvDescDetail.text = story.description
                Glide.with(this@DetailActivity)
                    .load(story.photoUrl)
                    .into(imgDetail)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
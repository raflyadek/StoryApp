package com.example.storyapp.presentation.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.Story
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.util.Constant.EXTRA_DETAIL

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DETAIL, Story::class.java)
        } else {
            @Suppress("DEPRECIATION")
            intent.getParcelableExtra(EXTRA_DETAIL)
        }
        if (story != null) {
            showDetailStory(story)
        }

        setSupportActionBar(binding.toolbar)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    private fun showDetailStory(story: Story) {
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
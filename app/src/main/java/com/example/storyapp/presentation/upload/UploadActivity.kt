package com.example.storyapp.presentation.upload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityUploadBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.presentation.login.LoginViewModel
import com.example.storyapp.presentation.main.MainViewModel
import com.example.storyapp.util.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUploadBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun observeViewModel(
        file: MultipartBody.Part, description: RequestBody
    ) {
        viewModel.uploadStories(file, description).observe(this) { result ->
            if (result != null) {
                when (result) {
                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(this, "Story Uploaded", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
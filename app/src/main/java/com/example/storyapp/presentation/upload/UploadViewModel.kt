package com.example.storyapp.presentation.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.UploadResponse
import com.example.storyapp.util.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: Repository) : ViewModel() {
    fun uploadStories(
        file: MultipartBody.Part, description: RequestBody
    ): LiveData<Result<UploadResponse>> =
        repository.uploadStories(file, description)
}
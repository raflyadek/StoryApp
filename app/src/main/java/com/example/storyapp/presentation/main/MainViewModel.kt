package com.example.storyapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.util.Result

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun logout() {
        repository.clearSession()
    }

    fun getStories(): LiveData<Result<ListStoryResponse>> =
        repository.getStories()

}
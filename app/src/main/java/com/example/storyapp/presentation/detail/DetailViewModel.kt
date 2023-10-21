package com.example.storyapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.util.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    fun getStories(): LiveData<Result<ListStoryResponse>> =
        repository.getStories()

}
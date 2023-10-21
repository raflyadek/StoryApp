package com.example.storyapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.DetailResponse
import com.example.storyapp.data.remote.DetailStory
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.data.remote.Story
import com.example.storyapp.util.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    fun detailStories(id: String): LiveData<Result<DetailResponse>> =
        repository.detailStories(id)

}
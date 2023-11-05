package com.example.storyapp.presentation.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.Story

class StoryPagingSourceViewModel(private val repository: Repository) : ViewModel() {
    fun getStories(): LiveData<PagingData<Story>> =
        repository.getStories().cachedIn(viewModelScope)
}
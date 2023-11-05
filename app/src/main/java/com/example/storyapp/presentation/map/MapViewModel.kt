package com.example.storyapp.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.util.Result

class MapViewModel(private val repository: Repository): ViewModel() {

    fun getStoriesWithLocation(location: Int): LiveData<Result<ListStoryResponse>> =
        repository.getStoriesWithLocation(location)
}
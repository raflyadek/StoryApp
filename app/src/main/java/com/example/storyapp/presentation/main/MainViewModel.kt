package com.example.storyapp.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.data.remote.Story
import com.example.storyapp.util.Result
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.clearSession()
        }
    }

    fun getStories(): LiveData<Result<List<Story>>> =
        repository.getStories()

}
package com.example.storyapp.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository

class SplashViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<String?> = repository.getSession()
}
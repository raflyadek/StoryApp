package com.example.storyapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Repository
import com.example.storyapp.data.Result
import com.example.storyapp.data.remote.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    suspend fun userLogin(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = repository.userLogin(email, password)
}
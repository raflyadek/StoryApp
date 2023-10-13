package com.example.storyapp.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository
import com.example.storyapp.util.Result
import com.example.storyapp.data.remote.RegisterResponse

class SignupViewModel(private val repository: Repository): ViewModel() {

    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ) : LiveData<Result<RegisterResponse>> =
        repository.userRegister(name, email, password)
}
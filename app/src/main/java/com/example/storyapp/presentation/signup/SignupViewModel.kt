package com.example.storyapp.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.RegisterResponse
import com.example.storyapp.util.Result

class SignupViewModel(private val repository: Repository): ViewModel() {

    fun userRegister(
        name: String,
        email: String,
        password: String
    ) : LiveData<Result<RegisterResponse>> =
        repository.userRegister(name, email, password)
}
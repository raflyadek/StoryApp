package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.preference.PrefManager
import com.example.storyapp.data.remote.DetailResponse
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.data.remote.LoginResponse
import com.example.storyapp.data.remote.RegisterResponse
import com.example.storyapp.data.remote.UploadResponse
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.util.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val apiService: ApiService,
    ) {

    fun userRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        try {
            val client = apiService.createUser(name, email, password)
            emit(Result.Success(client))
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "userRegister: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun userLogin(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.loginUser(email, password)
            emit(Result.Success(client))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "userLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(): LiveData<Result<ListStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getStories()
            emit(Result.Success(client))
        } catch (e: Exception) {
            Log.e("MainViewModel", "getStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun detailStories(id: String): LiveData<Result<DetailResponse>> = liveData {
        emit(Result.Loading)
        try{
            val client = apiService.detailStories(id)
            emit(Result.Success(client))
        } catch (e: Exception) {
            Log.e("DetailViewModel", "detailStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStories(
        file: MultipartBody.Part, description: RequestBody
    ): LiveData<Result<UploadResponse>> = liveData {
            emit(Result.Loading)
        try {
            val client = apiService.uploadStory(file, description)
            emit(Result.Success(client))
        } catch (e: Exception) {
            Log.e("UploadViewModel", "uploadStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun clearSession() {
        return PrefManager.clearAllData()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}
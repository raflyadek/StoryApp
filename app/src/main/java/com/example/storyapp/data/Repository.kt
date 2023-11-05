package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.preference.PrefManager
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.data.remote.LoginResponse
import com.example.storyapp.data.remote.RegisterResponse
import com.example.storyapp.data.remote.Story
import com.example.storyapp.data.remote.UploadResponse
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.presentation.paging.StoryPagingSource
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

    fun getStoriesWithLocation(
        location: Int
    ): LiveData<Result<ListStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getStoriesWithLocation(location)
            emit(Result.Success(client))
        } catch (e: Exception) {
            Log.e("MapViewModel", "map error: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
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
package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.preference.UserPreference
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.data.remote.LoginResponse
import com.example.storyapp.data.remote.RegisterResponse
import com.example.storyapp.data.remote.Story
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class Repository private constructor(
    private val apiService: ApiService,
    private val userPref: UserPreference
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
            val token = client.loginResult.token
            saveSession(token)
            emit(Result.Success(client))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "userLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(): LiveData<Result<List<Story>>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getStories()
            emit(Result.Success(client))
        } catch (e: Exception) {
            Log.e("MainViewModel", "getStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
    suspend fun clearSession() {
        return userPref.clearSession()
    }

    fun getSession() : LiveData<String?> {
        return userPref.getSession().asLiveData()
    }

    suspend fun saveSession(userToken: String) {
        return userPref.saveSession(userToken)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPref: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPref)
            }.also { instance = it }
    }
}
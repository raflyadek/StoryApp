package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.Repository
import com.example.storyapp.data.preference.UserPreference
import com.example.storyapp.data.preference.dataStore
import com.example.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService(context)
        return Repository.getInstance(apiService)
    }
}
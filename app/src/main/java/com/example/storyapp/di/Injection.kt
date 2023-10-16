package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.Repository
import com.example.storyapp.data.preference.UserPreference
import com.example.storyapp.data.preference.dataStore
import com.example.storyapp.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return Repository.getInstance(apiService)
    }
}
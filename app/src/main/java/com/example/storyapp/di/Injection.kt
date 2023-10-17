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
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.)
        return Repository.getInstance(apiService, pref)
    }
}
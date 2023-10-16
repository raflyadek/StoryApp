package com.example.storyapp.data.retrofit

import android.content.Context
import com.example.storyapp.data.preference.UserPreference
import com.loopj.android.http.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private fun getOkHttpClient(token: String) : OkHttpClient {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthIntercept(token))
                .build()
        }
        fun getApiService(context: Context) : ApiService {
            val sharedPref = UserPreference.initPref(context, "saveSession")
            val token = sharedPref.getString("token", null).toString()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(token))
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
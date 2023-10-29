package com.example.storyapp.data.retrofit

import android.content.Context
import android.util.Log
import com.example.storyapp.data.preference.PrefManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        PrefManager.init(context)
        Log.d("DEBUG", "intercept: token: ${PrefManager.TOKEN}")

        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${PrefManager.TOKEN}")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
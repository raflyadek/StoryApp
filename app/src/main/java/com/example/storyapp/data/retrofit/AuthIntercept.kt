package com.example.storyapp.data.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthIntercept(private var token: String) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val response = chain.proceed(request)

        request = if (request.header("Unauthorize") == null && token.isNotEmpty()) {
            val generateToken = "Bearer $token"
            request.newBuilder().addHeader("Authorization", generateToken)
                .build()
        } else {
            request.newBuilder()
                .build()
        }
        return response
    }
}
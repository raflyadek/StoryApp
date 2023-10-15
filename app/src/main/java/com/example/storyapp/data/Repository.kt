package com.example.storyapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.preference.UserPreference
import com.example.storyapp.data.remote.LoginResponse
import com.example.storyapp.data.remote.RegisterResponse
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class Repository private constructor(
    private val apiService: ApiService
) {

    private val resultRegister = MediatorLiveData<Result<RegisterResponse>>()
    private val resultLogin = MediatorLiveData<Result<LoginResponse>>()
    fun userRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> {
        resultRegister.value = Result.Loading
        val client = apiService.createUser(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.code() == 201) {
                    resultRegister.value = Result.Success(RegisterResponse())
                } else {
                    resultRegister.value = Result.Error("Registration Failed")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                resultRegister.value = Result.Error(t.message.toString())
            }

        })
        return resultRegister
    }

    fun userLogin(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData{
        resultLogin.value = Result.Loading
        val client = apiService.loginUser(email, password)

        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    resultLogin.value = Result.Success(LoginResponse())
                } else {
                    resultLogin.value = Result.Error("Login failed")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                resultLogin.value = Result.Error(t.message.toString())
            }

        })
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}
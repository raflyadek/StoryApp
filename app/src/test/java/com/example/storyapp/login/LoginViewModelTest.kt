package com.example.storyapp.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.LoginResponse
import com.example.storyapp.presentation.login.LoginViewModel
import com.example.storyapp.util.DummyData
import com.example.storyapp.util.Result
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DummyData.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `when login not null and return success`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)
        val email = "rafly@gmail.com"
        val password = "rafly123"

        Mockito.`when`(repository.userLogin(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.userLogin(email, password).getOrAwaitValue()
        Mockito.verify(repository).userLogin(email, password)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when login network error and return error`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Error("Network error")
        val email = "rafly@gmail.com"
        val password = "rafly123"

        Mockito.`when`(repository.userLogin(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.userLogin(email, password).getOrAwaitValue()
        Mockito.verify(repository).userLogin(email, password)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
        }
}
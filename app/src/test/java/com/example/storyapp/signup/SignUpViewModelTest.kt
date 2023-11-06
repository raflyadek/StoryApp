package com.example.storyapp.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.RegisterResponse
import com.example.storyapp.presentation.signup.SignupViewModel
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
class SignUpViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    private lateinit var signUpViewModel: SignupViewModel
    private val dummyRegisterResponse = DummyData.generateDummyRegisterResponse()

    @Before
    fun setUp() {
        signUpViewModel= SignupViewModel(repository)
    }

    @Test
    fun `when register not null and return success` () {
        val expectedRegisterResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedRegisterResponse.value = Result.Success(dummyRegisterResponse)
        val name = "rafly"
        val email = "rafly@gmail.com"
        val password = "rafly"

        Mockito.`when`(repository.userRegister(name, email, password)).thenReturn(expectedRegisterResponse)

        val actualResponse = signUpViewModel.userRegister(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).userRegister(name, email, password)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when register network error and return error` () {
        val expectedRegisterResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedRegisterResponse.value = Result.Error("Network error")
        val name = "rafly"
        val email = "rafly@gmail.com"
        val password = "rafly"

        Mockito.`when`(repository.userRegister(name, email, password)).thenReturn(expectedRegisterResponse)

        val actualResponse = signUpViewModel.userRegister(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).userRegister(name, email, password)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }
}
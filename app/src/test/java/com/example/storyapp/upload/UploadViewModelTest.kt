package com.example.storyapp.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.UploadResponse
import com.example.storyapp.presentation.upload.UploadViewModel
import com.example.storyapp.util.DummyData
import com.example.storyapp.util.Result
import com.example.storyapp.utils.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class UploadViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    private lateinit var uploadViewModel: UploadViewModel
    private val dummyUploadStoryResponse = DummyData.generateUploadStoryResponse()

    @Before
    fun setUp() {
        uploadViewModel = UploadViewModel(repository)
    }

    @Test
    fun `when postStory not null and return success` () {
        val descriptionText = "description text"
        val description = descriptionText.toRequestBody("text/plain".toMediaType())

        val file = Mockito.mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo name",
            "photo.jpg",
            requestImageFile
        )

        val expectedPostResponse = MutableLiveData<Result<UploadResponse>>()
        expectedPostResponse.value = Result.Success(dummyUploadStoryResponse)

        Mockito.`when`(repository.uploadStories(imageMultipart, description)).thenReturn(expectedPostResponse)

        val actualResponse = uploadViewModel.uploadStories(imageMultipart, description).getOrAwaitValue()
        Mockito.verify(repository).uploadStories(imageMultipart, description)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        assertEquals(dummyUploadStoryResponse.error, (actualResponse as Result.Success).data.error)
    }

    @Test
    fun `when postStory network error and return error` () {
        val descriptionText = "description text"
        val description = descriptionText.toRequestBody("text/plain".toMediaType())

        val file = Mockito.mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo name",
            "photo.jpg",
            requestImageFile
        )

        val expectedPostResponse = MutableLiveData<Result<UploadResponse>>()
        expectedPostResponse.value = Result.Error("network error")

        Mockito.`when`(repository.uploadStories(imageMultipart, description)).thenReturn(expectedPostResponse)

        val actualResponse = uploadViewModel.uploadStories(imageMultipart, description).getOrAwaitValue()
        Mockito.verify(repository).uploadStories(imageMultipart, description)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
        }
}
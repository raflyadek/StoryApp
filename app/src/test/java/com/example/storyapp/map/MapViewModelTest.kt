package com.example.storyapp.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.util.Result
import com.example.storyapp.presentation.map.MapViewModel
import com.example.storyapp.util.DummyData
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
class MapViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    private lateinit var mapViewModel: MapViewModel
    private val dummyStoryMapResponse = DummyData.generateDummyStories()

    @Before
    fun setUp() {
        mapViewModel = MapViewModel(repository)
    }

    @Test
    fun `when getStoriesWithLocation not null and return success`() {
        val expectedStoryResponse = MutableLiveData<Result<ListStoryResponse>>()
        expectedStoryResponse.value = Result.Success(dummyStoryMapResponse)

        Mockito.`when`(repository.getStoriesWithLocation(1)).thenReturn(expectedStoryResponse)

        val actualStories = mapViewModel.getStoriesWithLocation(1).getOrAwaitValue()
        Mockito.verify(repository).getStoriesWithLocation(1)
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Success)
        Assert.assertEquals(dummyStoryMapResponse.listStory.size, (actualStories as Result.Success).data.listStory.size)
    }

    @Test
    fun `when network error should return error` () {
        val expectedStoryResponse = MutableLiveData<Result<ListStoryResponse>>()
        expectedStoryResponse.value = Result.Error("network error")

        Mockito.`when`(repository.getStoriesWithLocation(1)).thenReturn(expectedStoryResponse)

        val actualStories = mapViewModel.getStoriesWithLocation(1).getOrAwaitValue()
        Mockito.verify(repository).getStoriesWithLocation(1)
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Error)
    }
}
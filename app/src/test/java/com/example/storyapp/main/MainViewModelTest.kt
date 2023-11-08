package com.example.storyapp.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.data.Repository
import com.example.storyapp.data.remote.Story
import com.example.storyapp.presentation.adapter.ListStoryAdapter
import com.example.storyapp.presentation.main.MainViewModel
import com.example.storyapp.util.DummyData
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var repository: Repository
    private lateinit var mainViewModel: MainViewModel

    private val dummyStoriesResponse = DummyData.generateDummyStoriess()

    @Before
    fun setup() {
        mainViewModel = MainViewModel(repository)
    }

    @Test
    fun `when getStories should not null and return success`() = runTest {
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStoriesResponse.listStory)
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        Mockito.`when`(repository.getStories()).thenReturn(expectedStories)

        val listStoryViewModel = MainViewModel(repository)
        val actualStories: PagingData<Story> = listStoryViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStoriesResponse.listStory, differ.snapshot())
        Assert.assertEquals(dummyStoriesResponse.listStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStoriesResponse.listStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when getStories empty should return no data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        Mockito.`when`(repository.getStories()).thenReturn(expectedStories)

        val listStoryViewModel = MainViewModel(repository)
        val actualStories: PagingData<Story> = listStoryViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        Assert.assertEquals(0, differ.snapshot().size)
    }

    @Test
    fun `User logout should call logout in Repository`() = runTest {
        mainViewModel.logout()
        Mockito.verify(repository).clearSession()
    }
}


class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload:Any?){}
}
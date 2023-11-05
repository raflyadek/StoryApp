package com.example.storyapp.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.remote.Story
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.presentation.adapter.ListStoryAdapter
import com.example.storyapp.presentation.adapter.LoadingStateStoryAdapter
import com.example.storyapp.presentation.detail.DetailActivity
import com.example.storyapp.presentation.map.MapActivity
import com.example.storyapp.presentation.upload.UploadActivity
import com.example.storyapp.presentation.welcome.WelcomeActivity
import com.example.storyapp.util.Constant.EXTRA_DETAIL

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = ListStoryAdapter()
        observeViewModel()
        setupToolbar()
    }


    private fun setupToolbar(){
        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.logout_menu -> {
                    viewModel.logout()
                    Toast.makeText(this, "Berhasil keluar", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.map_menu -> {
                    startActivity(Intent(this, MapActivity::class.java))
                    true
                }
                R.id.upload_menu -> {
                    val intent = Intent(this, UploadActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.adapter = adapter
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateStoryAdapter()
        )
        binding.rvStory.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
    }

    private fun observeViewModel() {
        viewModel.getStories().observe(this) { result ->
            adapter.submitData(this.lifecycle, result)
            adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Story?) {
                    val intentToDetail = Intent (this@MainActivity, DetailActivity::class.java)
                    intentToDetail.putExtra(EXTRA_DETAIL, data)
                    startActivity(intentToDetail)
                }

            })
        }
        setupAdapter()
    }
}
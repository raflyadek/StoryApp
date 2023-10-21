package com.example.storyapp.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.storyapp.util.Constant.EXTRA_DETAIL
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.remote.Story
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.presentation.adapter.ListStoryAdapter
import com.example.storyapp.presentation.detail.DetailActivity
import com.example.storyapp.presentation.map.MapActivity
import com.example.storyapp.presentation.signup.SignupViewModel
import com.example.storyapp.presentation.upload.UploadActivity
import com.example.storyapp.util.Result

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout_menu -> {
                viewModel.logout()
            }
            R.id.map_menu -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
            R.id.upload_menu -> {
                val intent = Intent(this, UploadActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun observeViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels { factory }
        viewModel.getStories().observe(this) { result ->
            if (result != null) {
                when(result) {
                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        showStory(result.data.listStory)
                    }
                    is Result.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showStory(listStory: List<Story>) {
        if (listStory != null) {
            setupRecyclerView()

            val adapter = ListStoryAdapter(listStory)
            binding.rvStory.adapter = adapter
            adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback{
                override fun onItemClicked(data: Story) {
                    val intentToDetail = Intent (this@MainActivity, DetailActivity::class.java)
                    intentToDetail.putExtra(EXTRA_DETAIL, data.id)
                    startActivity(intentToDetail)
                }
            })
        }

    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
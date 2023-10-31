package com.example.storyapp.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.remote.Story
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.presentation.adapter.ListStoryAdapter
import com.example.storyapp.presentation.detail.DetailActivity
import com.example.storyapp.presentation.upload.UploadActivity
import com.example.storyapp.presentation.welcome.WelcomeActivity
import com.example.storyapp.util.Constant.EXTRA_DETAIL
import com.example.storyapp.util.Result

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                    Toast.makeText(this, "Feature map untuk submission 2", Toast.LENGTH_SHORT).show()
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

    private fun observeViewModel() {
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
        setupRecyclerView()

        val adapter = ListStoryAdapter(listStory)
        binding.rvStory.adapter = adapter
        adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Story) {
                val intentToDetail = Intent (this@MainActivity, DetailActivity::class.java)
                intentToDetail.putExtra(EXTRA_DETAIL, data)
                startActivity(intentToDetail)
            }
        })

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
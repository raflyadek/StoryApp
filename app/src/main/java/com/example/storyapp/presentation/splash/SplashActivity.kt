package com.example.storyapp.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivitySplashBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.presentation.login.LoginViewModel
import com.example.storyapp.presentation.main.MainActivity
import com.example.storyapp.presentation.welcome.WelcomeActivity
import com.example.storyapp.util.Constant.EXTRA_TOKEN

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeViewModel()
    }

    private fun observeViewModel() {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getSession().observe(this) { token ->
                if (token.isNullOrEmpty()) {
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 500)
    }
}
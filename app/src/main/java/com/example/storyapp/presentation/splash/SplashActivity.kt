package com.example.storyapp.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.data.preference.PrefManager
import com.example.storyapp.databinding.ActivitySplashBinding
import com.example.storyapp.presentation.main.MainActivity
import com.example.storyapp.presentation.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        action()
    }

    private fun action() {
        PrefManager.init(this)
        Handler(Looper.getMainLooper()).postDelayed({
                if (PrefManager.TOKEN == "") {
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 500)
        }
    }

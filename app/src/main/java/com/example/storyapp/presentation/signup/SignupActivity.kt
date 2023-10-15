package com.example.storyapp.presentation.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.util.Result
import com.example.storyapp.databinding.ActivitySignUpBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.presentation.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private lateinit var etPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeViewModel(email = "", name = "", password = "")
        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isEmpty()){
                binding.emailEditText.error = "Email required"
                binding.emailEditText.requestFocus()
                return@setOnClickListener
            }

            if(name.isEmpty()) {
                binding.nameEditText.error = "Name required"
                binding.nameEditText.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()) {
                binding.passwordEditText.error = "password required"
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }
            observeViewModel(name, email, password)
        }
        etPassword = binding.passwordEditText
        val maxLength = 8
        val inputFilterArray = arrayOfNulls<InputFilter>(1)
        inputFilterArray[0] = InputFilter.LengthFilter(maxLength)
        etPassword.filters = inputFilterArray
        etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                val password = p0.toString().trim()
                if (password.length < maxLength) {
                    etPassword.error = getString(R.string.password_8_character)
                } else {
                    etPassword.error = null
                }
            }

        })
    }

    private fun observeViewModel(
        name: String,
        email: String,
        password: String
    ) {
        val factory = ViewModelFactory.getInstance(this)
        val viewModel: SignupViewModel by viewModels { factory }
        viewModel.userRegister(name, email, password).observe(this) { result ->
            if (result != null) {
                when(result) {
                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Berhasil!")
                            setMessage("Akun $email kamu sudah jadi.")
                            setPositiveButton("Next") { _, _ ->
                                val intent = Intent (context, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextView =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View. ALPHA, 1f).setDuration(100)
        val emailEditTextView =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextView =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup =
            ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextView,
                emailTextView,
                emailEditTextView,
                passwordTextView,
                passwordEditTextView,
                signup
            )
            startDelay = 100
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
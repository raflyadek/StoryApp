package com.example.storyapp.presentation.upload

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityUploadBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.presentation.login.LoginViewModel
import com.example.storyapp.presentation.main.MainViewModel
import com.example.storyapp.util.Constant
import com.example.storyapp.util.Helper
import com.example.storyapp.util.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UploadActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUploadBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null
    private var getFile: File? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            uploadStories()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = Helper.uriToFile(selectedImg, this)
            getFile = myFile
            binding.imgUpload.setImageURI(selectedImg)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadStories() {
        if (getFile != null) {
            showLoading(true)
            val file = Helper.reduceFile(getFile as File)
            val description = binding.edDescription.text
            if (!description.isNullOrEmpty()) {
                val descriptionText = description.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                observeViewModel(imageMultiPart, descriptionText)
            }
        }
    }
    private fun observeViewModel(
        file: MultipartBody.Part, description: RequestBody
    ) {
        viewModel.uploadStories(file, description).observe(this) { result ->
            if (result != null) {
                when (result) {
                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(this, "Story Uploaded", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
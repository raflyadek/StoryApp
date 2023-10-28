package com.example.storyapp.presentation.upload

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.storyapp.databinding.ActivityUploadBinding
import com.example.storyapp.presentation.ViewModelFactory
import com.example.storyapp.presentation.main.MainActivity
import com.example.storyapp.util.Constant.EXTRA_PHOTO
import com.example.storyapp.util.Constant.REQUIRED_PERMISSION
import com.example.storyapp.util.Helper
import com.example.storyapp.util.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUploadBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var gotFiles: File? = null
    private var imgCurrentUri: Uri? = null


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(!permissionGranted()) {
            reqPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnGallery.setOnClickListener {
            galleryLauncher()
        }
        binding.btnUpload.setOnClickListener {
            storiesUpload()
        }
        binding.btnCam.setOnClickListener {
            startCamX()
        }

    }

    private fun permissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startCamX() {
        val intent = Intent(this, CameraActivity::class.java)
        intentLauncherCam.launch(intent)
    }

    private val intentLauncherCam = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == 200) {
            imgCurrentUri = it.data?.getStringExtra(EXTRA_PHOTO)?.toUri()
            if (imgCurrentUri != null) {
                gotFiles = Helper.convertUriToFile(imgCurrentUri!!, applicationContext)
            }
            binding.imgUpload.setImageURI(imgCurrentUri)
        }
    }

    private val reqPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
            }
        }


    private fun galleryLauncher() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val createChooser = Intent.createChooser(intent, "Pilih Gambar")
        intentLauncherGallery.launch(createChooser)
    }

    private val intentLauncherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imgSelected: Uri = result.data?.data as Uri
            val galleryFile = Helper.convertUriToFile(imgSelected, this)
            gotFiles = galleryFile
            binding.imgUpload.setImageURI(imgSelected)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun storiesUpload() {
        if (gotFiles != null) {
            showLoading(true)
            val fileStories = Helper.reduceFileSize(gotFiles as File)
            val descriptionStories = binding.edDescription.text
            if (!descriptionStories.isNullOrEmpty()) {
                val descriptionText = descriptionStories.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = fileStories.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    fileStories.name,
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
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
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
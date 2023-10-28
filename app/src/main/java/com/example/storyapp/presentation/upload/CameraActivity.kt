package com.example.storyapp.presentation.upload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.storyapp.databinding.ActivityCameraBinding
import com.example.storyapp.util.Constant.EXTRA_PHOTO
import com.example.storyapp.util.Constant.TAG
import com.example.storyapp.util.Helper

class CameraActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }
    private var camSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imgCapture: ImageCapture? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startCamX()

        binding.ivSwitchCam.setOnClickListener {
            camSelector =
                if (camSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            startCamX()
        }
        binding.ivCapture.setOnClickListener {
            takeImgX()
        }
    }

    private fun startCamX() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val camProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val previewImg = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewCam.surfaceProvider)
                }

            imgCapture = ImageCapture.Builder().build()

            try {
                camProvider.unbindAll()
                camProvider.bindToLifecycle(
                    this,
                    camSelector,
                    previewImg,
                    imgCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal membuka kamera",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takeImgX() {
        val imgCapture = imgCapture ?: return
        val imgFile = Helper.customTempFile(applicationContext)
        val imgFileOptions = ImageCapture.OutputFileOptions.Builder(imgFile).build()

        imgCapture.takePicture(
            imgFileOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Success to take picture.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent()
                    intent.putExtra(EXTRA_PHOTO, output.savedUri.toString())
                    setResult(200, intent)
                    finish()
                }
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Failed to take picture.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
            )
        }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imgCapture?.targetRotation = rotation
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }
}
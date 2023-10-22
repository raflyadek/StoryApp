package com.example.storyapp.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat
import com.example.storyapp.util.Constant.FILE_FORMAT
import java.io.File
import java.security.Permissions
import java.text.SimpleDateFormat
import java.util.Locale

object Helper {

    fun permissionGranted(context: Context, permissions: String) =
        ContextCompat.checkSelfPermission(
            context, permissions
        ) == PackageManager.PERMISSION_GRANTED

    val timeStampFormat: String = SimpleDateFormat(
        FILE_FORMAT,
        Locale.ENGLISH
    ).format(System.currentTimeMillis())

    fun customTempFile(context: Context): File {
        val storageDirect: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStampFormat, "jpg", storageDirect)
    }

}
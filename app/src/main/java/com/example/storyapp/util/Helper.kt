package com.example.storyapp.util

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.renderscript.ScriptGroup.Input
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.storyapp.util.Constant.FILE_FORMAT
import com.example.storyapp.util.Constant.MAXIMAL_SIZE
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
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

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = customTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also {
                length = it } > 0) outputStream.write(
            buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun reduceFile(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(
            Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun Bitmap.getRotatedBitmap(file: File): Bitmap {
        val orientation = ExifInterface(file).getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
            ExifInterface.ORIENTATION_NORMAL -> this
            else -> this
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }

}
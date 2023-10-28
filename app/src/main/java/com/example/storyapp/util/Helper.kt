package com.example.storyapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.storyapp.util.Constant.FILENAME_FORMAT
import com.example.storyapp.util.Constant.MAXIMAL_SIZE
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {

    val timeStampFormat: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(Date())

    fun customTempFile(context: Context): File {
        val fileDir = context.externalCacheDir
        return File.createTempFile(timeStampFormat, "jpg", fileDir)
    }

    fun convertUriToFile(imageUri: Uri, context: Context): File {
        val file = customTempFile(context)
        val streamInput = context.contentResolver.openInputStream(imageUri) as InputStream
        val streamOutput = FileOutputStream(file)
        val bufferSize = ByteArray(1024)
        var length: Int
        while (streamInput.read(bufferSize).also {
                length = it } > 0) streamOutput.write(
            bufferSize, 0, length)
        streamOutput.close()
        streamInput.close()
        return file
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun reduceFileSize(file: File): File {
        val bitmapFile = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
        var qualityCompress = 100
        var lengthStream: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmapFile.compress(
                Bitmap.CompressFormat.JPEG, qualityCompress, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            lengthStream = bmpPicByteArray.size
            qualityCompress -= 5
        } while (lengthStream > MAXIMAL_SIZE)
        bitmapFile.compress(
            Bitmap.CompressFormat.JPEG, qualityCompress, FileOutputStream(file))
        return file
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun Bitmap.getRotatedBitmap(file: File): Bitmap {
        val orientat = ExifInterface(file).getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
        )
        return when (orientat) {
            ExifInterface.ORIENTATION_ROTATE_90 -> imageRotate(this, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> imageRotate(this, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> imageRotate(this, 270F)
            ExifInterface.ORIENTATION_NORMAL -> this
            else -> this
        }
    }

    fun imageRotate(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }

}
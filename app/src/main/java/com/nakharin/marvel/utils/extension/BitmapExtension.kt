package com.nakharin.marvel.utils.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.media.MediaScannerConnection
import android.os.Environment
import com.nakharin.marvel.utils.util.CoroutineUtils
import java.io.File
import java.io.FileOutputStream

fun Bitmap.saveToGallery(
    context: Context,
    filePath: String,
    onCompleted: (path: String) -> Unit,
    onException: (e: Exception) -> Unit
) {
    val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        .toString()
    val myDir = File(root)
    myDir.mkdirs()
    val file = File(myDir, filePath)

    if (file.exists()) {
        file.delete()
    }

    try {
        val out = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        e.printStackTrace()
        CoroutineUtils.main {
            onException(e)
        }
    }

    // Tell the media scanner about the new file so that it is
    // immediately available to the user.
    MediaScannerConnection.scanFile(
        context,
        arrayOf(file.toString()),
        null
    ) { path, _ ->
        CoroutineUtils.main {
            onCompleted(path)
        }
    }
}

operator fun Bitmap?.plus(bitmap: Bitmap?): Bitmap? {

    if (this == null) return bitmap
    if (bitmap == null) return this

    val result = Bitmap.createBitmap(
        width,
        height,
        config
    )

    val canvas = Canvas(result)

    canvas.drawBitmap(this, 0f, 0f, null)
    canvas.drawBitmap(bitmap, null, Rect(0, 0, width, height), null)

    return result
}

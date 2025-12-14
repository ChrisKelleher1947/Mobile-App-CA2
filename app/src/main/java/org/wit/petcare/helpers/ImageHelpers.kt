package org.wit.petcare.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import org.wit.petcare.R
import java.io.File
import java.io.FileOutputStream

fun showImagePicker(context: Context, intentLauncher: ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    val chooserTitle = context.getString(R.string.select_pet_image)
    chooseFile = Intent.createChooser(chooseFile, chooserTitle)
    intentLauncher.launch(chooseFile)
}

fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "pet_${System.currentTimeMillis()}.jpg")

    FileOutputStream(file).use { output ->
        inputStream?.copyTo(output)
    }

    return file.absolutePath
}

package org.wit.petcare.helpers

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.petcare.R

fun showImagePicker(context: Context, intentLauncher: ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    val chooserTitle = context.getString(R.string.select_pet_image)
    chooseFile = Intent.createChooser(chooseFile, chooserTitle)
    intentLauncher.launch(chooseFile)
}

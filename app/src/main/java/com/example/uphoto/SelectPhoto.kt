package com.example.uphoto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.MediaStore

import android.content.Intent
import android.view.View
import android.os.Build

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.Button
import java.io.IOException

import android.widget.ImageView


public const val PICK_PHOTO_CODE = 1046
private const val TAG = "editscreen"
class SelectPhoto : AppCompatActivity() {
    private lateinit var ivPreview: ImageView
    private lateinit var nextButton: Button
    private lateinit var imageBit: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_photo)
        ivPreview = findViewById(R.id.ivPreview)
        nextButton = findViewById(R.id.next_button)
        onPickPhoto(ivPreview)
        nextButton.setOnClickListener{
            Log.d(TAG, "next clicked")
            val Intent = EditScreen.newIntent(this, imageBit)
            Log.d(TAG, "intent created")
            startActivity(Intent)
            Log.d(TAG, "editscreen started")
        }
    }
    fun onPickPhoto(view: View?) {
        // Create intent for picking a photo from the gallery
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(packageManager) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE)
        }
    }
    fun loadFromUri(photoUri: Uri): Bitmap? {
        var image: Bitmap? = null
        try {
            // check version of Android on device
            image =    if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (image != null) {
            imageBit=image
        }
        return image
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == PICK_PHOTO_CODE) {
            val photoUri = data.data

            // Load the image located at photoUri into selectedImage
            val selectedImage = loadFromUri(photoUri!!)

            // Load the selected image into a preview

            ivPreview.setImageBitmap(selectedImage)
        }
    }


}
package com.uphoto


import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment

import com.theartofdev.edmodo.cropper.CropImage
import java.io.ByteArrayOutputStream
import java.io.File


class Dimensions : Fragment() {

    val PIC_CROP = 2
    private lateinit var image: ImageView
    private lateinit var ogImage: Bitmap
    private lateinit var bitmap: Bitmap
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var xflip: ImageButton
    private lateinit var yflip: ImageButton
    private  lateinit var right: ImageButton
    private  lateinit var left: ImageButton
    private lateinit var crop: Button
    private lateinit var revert: Button
    private lateinit var urisave: Uri
    private var height = 0
    private var width = 0
    private var dataPasser: OnDataPass? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dimensions, container, false)
        image = view.findViewById(R.id.imageView)
        xflip = view.findViewById(R.id.horizontalFlip)
        yflip = view.findViewById(R.id.verticalFlip)
        right = view.findViewById(R.id.rotateRight)
        left = view.findViewById(R.id.rotateLeft)
        crop = view.findViewById(R.id.crop)
        revert = view.findViewById(R.id.revert)
        revert.setVisibility(INVISIBLE)
        saveButton = view.findViewById(R.id.save_button)
        cancelButton = view.findViewById(R.id.cancel_button)
        val activity: MainActivity? = activity as MainActivity?
        if (activity != null) {
            ogImage = activity.getbit()!!
        }
        bitmap = ogImage
        xflip.setOnClickListener{
            xFlip()
        }
        yflip.setOnClickListener{
            yFlip()
        }
        right.setOnClickListener{
            RotateBitmap(90F)
        }
        left.setOnClickListener{
            RotateBitmap(-90F)
        }
        crop.setOnClickListener{
            crop()

        }
        revert.setOnClickListener{
            revert()
        }
        saveButton.setOnClickListener {
            dataPasser?.passBit(bitmap)
            returnToEditMain()
        }
        cancelButton.setOnClickListener {
            returnToEditMain()
        }

        height = bitmap.height
        width = bitmap.width
        if (height.toDouble()/width >= 1.75)
        {
            setDimensions(image, 1150)
        }
        if (height > 1150)
        {
            setDimensions(image, 1150)
        }
        image.setImageBitmap(bitmap)


        return view
    }
    private fun setDimensions(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.layoutParams = params
    }



    private fun xFlip() {
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)
        val newBit: Bitmap = Bitmap.createBitmap(
            bitmap, 0, 0,
            bitmap.width, bitmap.height, matrix, false
        )
        bitmap = newBit
        image.setImageBitmap(bitmap)
    }
    private fun yFlip() {
        val matrix = Matrix()
        matrix.preScale(1.0f, -1.0f)
        val newBit: Bitmap = Bitmap.createBitmap(
            bitmap, 0, 0,
            bitmap.width, bitmap.height, matrix, false
        )
        bitmap = newBit
        image.setImageBitmap(bitmap)
    }
    fun RotateBitmap( angle: Float) {
        val matrix = Matrix()
        matrix.postRotate(angle)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        image.setImageBitmap(bitmap)
    }

    private fun bitmapToUri():Uri
    {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            requireContext().getContentResolver(),
            bitmap,
            "Title",
            null
        )
        return Uri.parse(path)

    }

    private fun crop()
    {
        urisave = bitmapToUri()
        context?.let {
            CropImage.activity(urisave)
                .start(it, this)
        }



    }
    private fun revert()
    {
        image.setImageBitmap(ogImage)
        bitmap = ogImage
        revert.setVisibility(INVISIBLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                revert.setVisibility(VISIBLE)
                bitmap = UriToBit(resultUri)
                image.setImageBitmap(bitmap)
                Log.d("main_activity", "file deletion try")
                context?.getContentResolver()?.delete(urisave, null,
                    null);
                height = bitmap.height
                width = bitmap.width
                if (height.toDouble()/width >= 1.75)
                {
                    setDimensions(image, 1150)
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun returnToEditMain()
    {
        dataPasser?.onDataPass("edit main")
    }

    fun UriToBit(photoUri: Uri): Bitmap {

        val newbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri)
        return newbitmap
    }

    private fun getContentResolver(): ContentResolver? {
        val activity: MainActivity? = activity as MainActivity?

        if (activity != null) {
          return activity.contentResolver
        }
        else
        {
            return null
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(): Dimensions
        {
            return Dimensions()
        }

    }
}
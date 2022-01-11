package com.example.uphoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.os.Build
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import android.graphics.Bitmap
import android.provider.MediaStore

import android.content.ContentValues

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.widget.Toast
import java.io.OutputStream
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [EditMain.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "main_activity"
class EditMain : Fragment() {


    private lateinit var image: ImageView
    //private lateinit var brighten: Button
    private lateinit var bitmap: Bitmap

    private lateinit var ogImage: Bitmap
    private lateinit var basicFiltersButton: Button
    private lateinit var finishButton: Button
    private lateinit var drawButton: Button
    private var dataPasser: OnDataPass? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_main, container, false)
        image = view.findViewById(R.id.imageView)

        finishButton = view.findViewById(R.id.finish_button)
        basicFiltersButton = view.findViewById(R.id.basic_filters)
        drawButton = view.findViewById(R.id.drawables)
        finishButton.setOnClickListener {
            context?.let { it1 -> saveImage(bitmap, it1, "Uphoto")
            newFrag("main menu")}

        }
        basicFiltersButton.setOnClickListener {
            newFrag("basic filters")
        }
        drawButton.setOnClickListener {
            newFrag("draw")
        }
        val activity: MainActivity? = activity as MainActivity?


        if (activity != null) {
            ogImage = activity.getbit()!!
        }
        bitmap = ogImage

        image.setImageBitmap(bitmap)
        return view
    }
    private fun newFrag(name: String)
    {
        dataPasser?.passBit(bitmap)
        dataPasser?.onDataPass(name)
    }
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {

        val values = contentValues()
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        // RELATIVE_PATH and IS_PENDING are introduced in API 29.

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
        }
        Log.d(TAG, "save action complete?")

    }
    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment EditMain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance():EditMain{
            return EditMain()
        }


    }
}
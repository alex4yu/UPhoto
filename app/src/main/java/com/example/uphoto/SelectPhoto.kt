package com.example.uphoto

import android.content.Context
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
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import java.io.IOException

import android.widget.ImageView
import androidx.fragment.app.Fragment



private const val TAG = "main_activity"
class SelectPhoto : Fragment() {
    private lateinit var ivPreview: ImageView
    private lateinit var nextButton: Button
    private lateinit var bitmap: Bitmap
    private var dataPasser: OnDataPass? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "select photo created")

    }
    fun passData(data: String){
        dataPasser?.onDataPass(data)
    }
    fun passBit()
    {
        dataPasser?.passBit(bitmap)
    }
    fun onClick(view: View)
    {
        Log.d(TAG, "next clicked")
        passData("edit main")
        passBit()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select_photo, container, false)

        ivPreview = view.findViewById(R.id.ivPreview)
        nextButton = view.findViewById(R.id.next_button)
        nextButton.setOnClickListener{
            onClick(nextButton)
        }
        Log.d(TAG, "select view returned")
        val activity: MainActivity? = activity as MainActivity?
        Log.d(TAG, "activity set")

        if (activity != null) {
            bitmap = activity.getbit()!!
        }
        ivPreview.setImageBitmap(bitmap)

        Log.d(TAG, "bitmap value set")
        return view
    }
    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance():SelectPhoto{
            return SelectPhoto()
        }
    }


}
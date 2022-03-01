package com.example.uphoto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.graphics.rotationMatrix
import androidx.fragment.app.Fragment


class Dimensions : Fragment() {
    private lateinit var image: ImageView
    private lateinit var ogImage: Bitmap
    private lateinit var bitmap: Bitmap
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var xflip: ImageButton
    private lateinit var yflip: ImageButton
    private  lateinit var right: ImageButton
    private  lateinit var left: ImageButton
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
        saveButton.setOnClickListener {
            dataPasser?.passBit(bitmap)
            returnToEditMain()
        }
        cancelButton.setOnClickListener {
            returnToEditMain()
        }

        height = bitmap.height
        width = bitmap.width
        image.setImageBitmap(bitmap)


        return view
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

    private fun returnToEditMain()
    {
        dataPasser?.onDataPass("edit main")
    }
    companion object {

        @JvmStatic
        fun newInstance(): Dimensions
        {
            return Dimensions()
        }

    }
}
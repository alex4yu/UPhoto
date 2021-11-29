package com.example.uphoto

import android.graphics.*
import android.graphics.Color.rgb
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.graphics.ColorMatrixColorFilter

import android.graphics.ColorMatrix
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.google.android.material.slider.Slider
import kotlin.math.roundToInt
import android.graphics.Bitmap
import kotlin.math.ln
import android.R.color
import android.provider.MediaStore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [EditMain.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "main_activity"
class EditMain : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var image: ImageView
    //private lateinit var brighten: Button
    private lateinit var bitmap: Bitmap
    private lateinit var ogImage: Bitmap
    private lateinit var slider: Slider
    private lateinit var contrastbutton: ImageButton
    private lateinit var brightnessbutton: ImageButton
    private lateinit var savebutton: Button
    private var brightnessSelected: Boolean = true
    private var height: Int = 0
    private var width: Int = 0

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
        slider = view.findViewById(R.id.slider)
        savebutton = view.findViewById(R.id.save_button)
        slider.addOnChangeListener{slider, value, fromUser->
            editimage(value)
        }
        contrastbutton = view.findViewById(R.id.contrast)
        brightnessbutton = view.findViewById(R.id.brightness)

        contrastbutton.setOnClickListener{
            brightnessSelected = false
        }
        brightnessbutton.setOnClickListener{
            brightnessSelected = true
        }
        savebutton.setOnClickListener {

        }
        
        /*brighten = view.findViewById(R.id.brightenbutton)
        brighten.setOnClickListener {
            brighten()
        }
        */

        val activity: MainActivity? = activity as MainActivity?


        if (activity != null) {
            ogImage = activity.getbit()!!
        }
        bitmap = ogImage
        Log.d(TAG, "${bitmap.height}")
        height = bitmap.height
        width = bitmap.width
        image.setImageBitmap(bitmap)
        return view

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun editimage(value: Float)
    {
        if(brightnessSelected)
        {
            brightIt(value)
        }
        else
        {
            Log.d(TAG, "contrast change try")
            contrastedit(value)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun contrastedit(fb:Float)
    {
        Log.d(TAG, "enter contrastedit")
        var c: Double = 0.0
        if(fb <= 0)
        {
            c = (50.0+fb.toDouble())/50.0
        }
        else if(fb > 0)
        {
            c = ln(fb.toDouble()+1)/4.0+1
        }
        var y = 0


        var bitcopy: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        while (y<height)
        {

            var x = 0
            while (x<width)
            {

                var color: Int
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {

                   color = bitcopy.getColor(x, y).toArgb()


                }
                else {
                    TODO("VERSION.SDK_INT < Q")

                }

                val r = color shr 16 and 0xFF
                val g = color shr 8 and 0xFF
                val b = color shr 0 and 0xFF
                val newRed = truncate(c*(r-128)+128)
                val newBlue = truncate(c*(b-128)+128)
                val newGreen = truncate(c*(g-128)+128)



                bitcopy.setPixel(x, y, rgb(newRed, newGreen, newBlue))

                x++
            }
            y++
        }
        image.setImageBitmap(bitcopy)
        bitmap = bitcopy
    }
    fun truncate(value: Double): Int
    {
        if (value<0)
        {
            return 0
        }
        else if (value > 255)
        {
            return 255
        }
        else
        {
            return value.roundToInt()
        }
    }
    fun brightIt(fb: Float)
    {
        val cmB = ColorMatrix()
        cmB.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, fb,
                0f, 1f, 0f, 0f, fb,
                0f, 0f, 1f, 0f, fb,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val colorMatrix = ColorMatrix()
        colorMatrix.set(cmB)
        image.setColorFilter(ColorMatrixColorFilter(colorMatrix))

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
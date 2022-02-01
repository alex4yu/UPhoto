package com.example.uphoto

import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.*
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.slider.Slider
import kotlin.math.ln
import kotlin.math.roundToInt




private const val TAG = "main_activity"
class basic_filters : Fragment() {
    private lateinit var image: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var bitcheckpoint: Bitmap
    private lateinit var ogImage: Bitmap
    private lateinit var brightnessSlider: Slider
    private lateinit var contrastSlider: Slider
    private lateinit var contrastbutton: ImageButton
    private lateinit var brightnessbutton: ImageButton
    private lateinit var savebutton: Button
    private lateinit var cancelbutton: Button
    private lateinit var option_text: TextView


    private var brightnessSelected: Boolean = true
    private var height: Int = 0
    private var width: Int = 0

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
        val view = inflater.inflate(R.layout.fragment_basic_filters, container, false)
        image = view.findViewById(R.id.imageView)
        brightnessSlider = view.findViewById(R.id.brightnessslider)
        contrastSlider = view.findViewById(R.id.contrastslider)
        savebutton = view.findViewById(R.id.save_button)
        cancelbutton = view.findViewById(R.id.canel_button)

        option_text = view.findViewById(R.id.option_text)

        brightnessSlider.addOnChangeListener{Slider, value, fromUser->
            editimage(value)
        }
        contrastSlider.addOnChangeListener{Slider, value, fromUser->
            editimage(value)
        }
        savebutton.setOnClickListener {
            dataPasser?.passBit(bitmap)
            returnToEditMain()
        }
        cancelbutton.setOnClickListener {
            returnToEditMain()
        }
        contrastSlider.setVisibility(View.INVISIBLE)
        contrastbutton = view.findViewById(R.id.contrast)
        brightnessbutton = view.findViewById(R.id.brightness)

        contrastbutton.setOnClickListener{
            if(brightnessSelected)
            {
                bitcheckpoint = bitmap
            }
            brightnessSelected = false
            brightnessSlider.setVisibility(View.INVISIBLE)
            option_text.setText("Contrast")
            contrastSlider.setVisibility(View.VISIBLE)

        }
        brightnessbutton.setOnClickListener{
            if(!brightnessSelected)
            {
                bitcheckpoint = bitmap
            }
            brightnessSelected = true
            contrastSlider.setVisibility(View.INVISIBLE)
            brightnessSlider.setVisibility(View.VISIBLE)
            option_text.setText("Brightness")
        }

        val activity: MainActivity? = activity as MainActivity?


        if (activity != null) {
            ogImage = activity.getbit()!!
        }
        bitmap = ogImage
        bitcheckpoint = ogImage
        Log.d(TAG, "${bitmap.height}")
        height = bitmap.height
        width = bitmap.width
        image.setImageBitmap(bitmap)
        Log.d(TAG, "basic filter view set")
        return view
    }

    private fun returnToEditMain()
    {
        dataPasser?.onDataPass("edit main")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun editimage(value: Float)
    {
        if(brightnessSelected)
        {
            brightIt(value*2)
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
            c = ln(fb.toDouble()+1) /4.0+1
        }
        var y = 0


        var bitcopy: Bitmap = bitcheckpoint.copy(Bitmap.Config.ARGB_8888, true)
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
                else {TODO("VERSION.SDK_INT < Q")}

                val r = color shr 16 and 0xFF
                val g = color shr 8 and 0xFF
                val b = color shr 0 and 0xFF
                val newRed = truncate(c*(r-128)+128)
                val newBlue = truncate(c*(b-128)+128)
                val newGreen = truncate(c*(g-128)+128)



                bitcopy.setPixel(x, y, Color.rgb(newRed, newGreen, newBlue))

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

        val cm = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, fb,
                0f, 1f, 0f, 0f, fb,
                0f, 0f, 1f, 0f, fb,
                0f, 0f, 0f, 1f, 0f
            )
        )


        var ret: Bitmap = bitcheckpoint.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(ret)

        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(ret, 0.toFloat(), 0.toFloat(), paint)

        image.setImageBitmap(ret)
        bitmap = ret

    }

    companion object {

        @JvmStatic
        fun newInstance(): basic_filters
        {
            return basic_filters()
        }

    }
}
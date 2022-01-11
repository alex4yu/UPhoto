package com.example.uphoto

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.graphics.Color
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.view.MotionEvent
import android.view.View.OnTouchListener
import com.google.android.material.slider.Slider
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [draw.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "main_activity"
class draw : Fragment() {
    private lateinit var image: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var bitcopy: Bitmap
    private lateinit var canvas: Canvas
    private lateinit var ogImage: Bitmap
    private lateinit var savebutton: Button
    private lateinit var cancelbutton: Button
    private lateinit var colorbutton: Button
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var thicknessSlider: Slider
    private var height = 0
    private var width = 0
    private var dataPasser: OnDataPass? = null
    private var mDefaultColor = 0
    private var proportion = 1.0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw, container, false)
        image = view.findViewById(R.id.imageView)

        savebutton = view.findViewById(R.id.save_button)
        cancelbutton = view.findViewById(R.id.cancel_button)
        colorbutton = view.findViewById(R.id.color_button)
        thicknessSlider = view.findViewById(R.id.thickness)
        savebutton.setOnClickListener {
            dataPasser?.passBit(bitmap)
            returnToEditMain()
        }
        cancelbutton.setOnClickListener {
            returnToEditMain()
        }
        colorbutton.setOnClickListener {
            chooseColor()
        }
        thicknessSlider.addOnChangeListener { Slider, value, fromUser ->
            changethickness(value)
        }
        val activity: MainActivity? = activity as MainActivity?


        if (activity != null) {
            ogImage = activity.getbit()!!
        }
        bitmap = ogImage
        bitcopy = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvas = Canvas(bitcopy)

        height = bitmap.height
        width = bitmap.width
        if (width > height)
        {
            proportion=4.0/3.0
        }
        Log.d(TAG, "$proportion")
        image.setImageBitmap(bitmap)

        image.setOnTouchListener(handleTouch)
        paint = Paint()
        path = Path()
        paint.setAntiAlias(true)
        paint.setStrokeWidth(5f)
        paint.setColor(Color.BLACK)
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeJoin(Paint.Join.ROUND)
        Log.d(TAG, "width: $width height: $height")
        return view

    }
    private fun returnToEditMain()
    {
        dataPasser?.onDataPass("edit main")
    }
    @SuppressLint("ClickableViewAccessibility")
    private val handleTouch = OnTouchListener { v, event ->

        when (event.action) {
            MotionEvent.ACTION_DOWN-> {
                path = Path()
                path.moveTo((event.x*proportion).toFloat(), (event.y*proportion).toFloat())
                var xcord = event.x
                var ycord = event.y
                Log.d(TAG, "start $xcord, $ycord")
            }
            MotionEvent.ACTION_MOVE-> path.lineTo((event.x*proportion).toFloat(), (event.y*proportion).toFloat())
            MotionEvent.ACTION_UP-> {
                var xcord = event.x
                var ycord = event.y
                Log.d(TAG, "end $xcord, $ycord")
            }
        }
        draw()
        true
    }
    private fun changethickness(value: Float)
    {
        paint.setStrokeWidth(value)
    }
    private fun chooseColor()
    {
        ColorPickerPopup.Builder(context)
            .initialColor(Color.RED)
            .enableBrightness(true)
            .enableAlpha(true)
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(colorbutton,
                object : ColorPickerObserver() {
                    override fun onColorPicked(color: Int) {
                        // set the color
                        // which is returned
                        // by the color
                        // picker
                        mDefaultColor = color

                        // now as soon as
                        // the dialog closes
                        // set the preview
                        // box to returned
                        // color
                        paint.setColor(color)
                    }
                }
                )
    }
    private fun draw()
    {
        canvas.drawPath(path, paint)
        image.setImageBitmap(bitcopy)
        bitmap = bitcopy


    }

    companion object {
       
        @JvmStatic
        fun newInstance(): draw{
            return draw()
        }

    }
}
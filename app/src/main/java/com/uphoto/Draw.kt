package com.uphoto

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
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
class Draw : Fragment() {
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
    private lateinit var penButton: ImageButton
    private lateinit var brushButton: ImageButton
    private lateinit var revert: Button
    private var pen = true
    private var brush = false
    private var height = 0
    private var width = 0
    private var layoutH = 0
    private var layoutW = 0
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
        penButton = view.findViewById(R.id.pen)
        brushButton = view.findViewById(R.id.brush)
        revert = view.findViewById(R.id.revert)
        revert.setVisibility(View.INVISIBLE)
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
        penButton.setOnClickListener{
            allFalse()
            pen = true
            thicknessSlider.setValue(5f)
        }
        revert.setOnClickListener{
            revert()
        }
        brushButton.setOnClickListener{
            allFalse()
            brush = true
            thicknessSlider.setValue(15f)
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
        if (height.toDouble()/width >= 1.75)
        {
            setDimensions(image, 1150)
        }
        if (height > 1150)
        {
            setDimensions(image, 1150)
        }
        image.setImageBitmap(bitmap)

        image.setImageBitmap(bitmap)
        image.setOnTouchListener(handleTouch)
        paint = Paint()
        path = Path()
        paint.setAntiAlias(true)
        paint.setStrokeWidth(5f)
        paint.setColor(Color.BLACK)
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeJoin(Paint.Join.ROUND)
        setColorButton()
        Log.d(TAG, "width: $width height: $height")
        return view

    }

    private fun allFalse()
    {
        pen = false
        brush = false
    }
    private fun setDimensions(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.layoutParams = params
    }
    private fun returnToEditMain()
    {
        dataPasser?.onDataPass("edit main")
    }
    @SuppressLint("ClickableViewAccessibility")
    private val handleTouch = OnTouchListener { v, event ->
        if (layoutH == 0)
        {
            layoutH = image.measuredHeight
            layoutW = image.measuredWidth
            Log.d(TAG, "width: $layoutW height: $layoutH")
            proportion = height.toDouble()/layoutH.toDouble()
        }
        if(brush)
        {
            paint.setAlpha(10)

        }
        if(pen)
        {
            paint.setAlpha(255)
        }
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
        revert.setVisibility(View.VISIBLE)
        true
    }
    private fun revert()
    {
        image.setImageBitmap(ogImage)
        bitmap = ogImage
        revert.setVisibility(View.INVISIBLE)
        bitcopy = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvas = Canvas(bitcopy)
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
                        setColorButton()
                    }
                }
                )

     }
    private fun setColorButton()
    {
        colorbutton.setBackgroundColor(paint.color)
        val total = paint.color.red + paint.color.blue + paint.color.green
        Log.d("main_activity", "$total")
        if (total>382)
        {
            colorbutton.setTextColor(Color.rgb(0,0,0))
        }
        else
        {
            colorbutton.setTextColor(Color.rgb(255,255,255))
        }
    }
    private fun draw()
    {

        canvas.drawPath(path, paint)
        image.setImageBitmap(bitcopy)
        bitmap = bitcopy


    }

    companion object {
       
        @JvmStatic
        fun newInstance(): Draw{
            return Draw()
        }

    }
}
package com.uphoto

import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.*
import android.content.Context
import android.graphics.*
import android.media.Image
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

import jp.co.cyberagent.android.gpuimage.GPUImage





private const val TAG = "main_activity"
class BasicAdjust : Fragment() {
    private lateinit var image: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var bitcheckpoint: Bitmap
    private lateinit var ogImage: Bitmap
    private lateinit var brightnessSlider: Slider
    private lateinit var contrastSlider: Slider
    private lateinit var saturationSlider: Slider
    private lateinit var vibranceSlider: Slider
    private lateinit var hueSlider: Slider
    private lateinit var sharpenSlider: Slider
    private lateinit var contrastbutton: ImageButton
    private lateinit var brightnessbutton: ImageButton
    private lateinit var saturationbutton: ImageButton
    private lateinit var vibrancebutton: ImageButton
    private lateinit var sharpenButton: ImageButton
    private lateinit var savebutton: Button
    private lateinit var cancelbutton: Button
    private lateinit var revert: Button
    private lateinit var option_text: TextView
    private lateinit var hueButton: ImageButton



    private var selectedfilter = "brightness"
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
        val view = inflater.inflate(R.layout.fragment_basic_adjustments, container, false)
        image = view.findViewById(R.id.imageView)
        brightnessSlider = view.findViewById(R.id.brightnessslider)
        contrastSlider = view.findViewById(R.id.contrastslider)
        saturationSlider = view.findViewById(R.id.saturationslider)
        vibranceSlider = view.findViewById(R.id.vibranceslider)
        hueSlider = view.findViewById(R.id.hueSlider)
        sharpenSlider = view.findViewById(R.id.sharpSlider)
        savebutton = view.findViewById(R.id.save_button)
        cancelbutton = view.findViewById(R.id.cancel_button)
        revert = view.findViewById(R.id.revert)
        revert.setVisibility(View.INVISIBLE)
        option_text = view.findViewById(R.id.option_text)
        startup()

        brightnessSlider.addOnChangeListener{Slider, value, fromUser->
            editimage(value)
        }
        contrastSlider.addOnChangeListener{Slider, value, fromUser->
            editimage(value)
        }
        saturationSlider.addOnChangeListener{Slider, value, fromUser->
            editimage(value)
        }
        vibranceSlider.addOnChangeListener{Slider, value, fromUser->
            editimage(value)
        }
        hueSlider.addOnChangeListener{Slider, value, fromUser->
            editimage(value)
        }
        sharpenSlider.addOnChangeListener{Slider, value, fromUser->
            editimage(value)
        }
        savebutton.setOnClickListener {
            dataPasser?.passBit(bitmap)
            returnToEditMain()
        }
        cancelbutton.setOnClickListener {
            returnToEditMain()
        }
        revert.setOnClickListener{
            revert()
        }
        contrastSlider.setVisibility(View.INVISIBLE)
        contrastbutton = view.findViewById(R.id.contrast)
        brightnessbutton = view.findViewById(R.id.brightness)
        saturationbutton = view.findViewById(R.id.saturation)
        vibrancebutton = view.findViewById(R.id.vibrance)
        hueButton = view.findViewById(R.id.hue)
        sharpenButton = view.findViewById(R.id.sharp)
        contrastbutton.setOnClickListener{
            if(!selectedfilter.equals("contrast"))
            {
                bitcheckpoint = bitmap
                selectedfilter = "contrast"
                allSliderInvisible()
                option_text.setText("Contrast")
                contrastSlider.setVisibility(View.VISIBLE)
            }
        }
        brightnessbutton.setOnClickListener{
            if(!selectedfilter.equals("brightness"))
            {
                bitcheckpoint = bitmap
                selectedfilter = "brightness"
                allSliderInvisible()
                brightnessSlider.setVisibility(View.VISIBLE)
                option_text.setText("Brightness")
            }
        }
        saturationbutton.setOnClickListener{
            if(!selectedfilter.equals("saturation"))
            {
                bitcheckpoint = bitmap
                selectedfilter = "saturation"
                allSliderInvisible()
                option_text.setText("Saturation")
                saturationSlider.setVisibility(View.VISIBLE)
            }
        }
        vibrancebutton.setOnClickListener{
            if(!selectedfilter.equals("vibrance"))
            {
                bitcheckpoint = bitmap
                selectedfilter = "vibrance"
                allSliderInvisible()
                option_text.setText("Vibrance")
                vibranceSlider.setVisibility(View.VISIBLE)
            }
        }
        hueButton.setOnClickListener{
            if(!selectedfilter.equals("hue"))
            {
                bitcheckpoint = bitmap
                selectedfilter = "hue"
                allSliderInvisible()
                hueSlider.setVisibility(View.VISIBLE)
                option_text.setText("Hue")
            }
        }
        sharpenButton.setOnClickListener{
            if(!selectedfilter.equals("sharpen"))
            {
                bitcheckpoint = bitmap
                selectedfilter = "sharpen"
                allSliderInvisible()
                sharpenSlider.setVisibility(View.VISIBLE)
                option_text.setText("Sharpen")
            }
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
        if (height.toDouble()/width >= 1.75)
        {
            setDimensions(image, 1150)
        }
        if (height > 1150)
        {
            setDimensions(image, 1150)
        }
        image.setImageBitmap(bitmap)
        Log.d(TAG, "basic filter view set")
        return view
    }
    private fun setDimensions(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.layoutParams = params
    }
    private fun startup()
    {
        allSliderInvisible()
        brightnessSlider.setVisibility(View.VISIBLE)
    }
    private fun allSliderInvisible()
    {
        saturationSlider.setVisibility(View.INVISIBLE)
        contrastSlider.setVisibility(View.INVISIBLE)
        brightnessSlider.setVisibility(View.INVISIBLE)
        vibranceSlider.setVisibility(View.INVISIBLE)
        hueSlider.setVisibility(View.INVISIBLE)
        sharpenSlider.setVisibility(View.INVISIBLE)
    }
    private fun returnToEditMain()
    {
        dataPasser?.onDataPass("edit main")
    }
    private fun revert()
    {
        image.setImageBitmap(ogImage)
        bitmap = ogImage
        revert.setVisibility(View.INVISIBLE)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun editimage(value: Float)
    {
        revert.setVisibility(View.VISIBLE)
        if(selectedfilter.equals("brightness"))
        {
            brightIt(value*2)
        }
        else if (selectedfilter.equals("contrast"))
        {
            contrastedit(value)
        }
        else if(selectedfilter.equals("saturation"))
        {
            saturationEdit(value)
        }
        else if(selectedfilter.equals("vibrance"))
        {
            vibranceEdit(value)
        }
        else if(selectedfilter.equals("hue"))
        {
            hueEdit(value)
        }
        else if(selectedfilter.equals("sharpen"))
        {
            sharpen(value)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun contrastedit(fb:Float)
    {
        var change = 1.toFloat()
        if (fb == 0.toFloat()) {
        }
        else if (fb < 0)
        {
            change = (fb+50)/50
        }
        else
        {
            change = fb/20+1
        }
        val gpuImage = GPUImage(context)
        val contrastfilter = GPUImageContrastFilter(change)
        gpuImage.setFilter(contrastfilter)

        bitmap = gpuImage.getBitmapWithFilterApplied(bitcheckpoint)
        image.setImageBitmap(bitmap)

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


        val ret: Bitmap = bitcheckpoint.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(ret)

        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(ret, 0.toFloat(), 0.toFloat(), paint)

        image.setImageBitmap(ret)
        bitmap = ret

    }

    fun saturationEdit(fb: Float)
    {
        val ret: Bitmap = bitcheckpoint.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val saturationFilter = GPUImageSaturationFilter((fb*0.02).toFloat()+1)
        gpuImage.setFilter(saturationFilter)

        bitmap = gpuImage.getBitmapWithFilterApplied(ret)
        image.setImageBitmap(bitmap)
    }

    fun vibranceEdit(fb:Float)
    {
        val ret: Bitmap = bitcheckpoint.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val vibranceFilter = GPUImageVibranceFilter(fb/10)
        gpuImage.setFilter(vibranceFilter)

        bitmap = gpuImage.getBitmapWithFilterApplied(ret)
        image.setImageBitmap(bitmap)
    }
    fun hueEdit(fb:Float)
    {
        val ret: Bitmap = bitcheckpoint.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val hueFilter = GPUImageHueFilter(fb)
        gpuImage.setFilter(hueFilter)

        bitmap = gpuImage.getBitmapWithFilterApplied(ret)
        image.setImageBitmap(bitmap)
    }
    fun sharpen(fb:Float)
    {
        val ret: Bitmap = bitcheckpoint.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val sharpFilter = GPUImageSharpenFilter(fb/12.5f)
        gpuImage.setFilter(sharpFilter)

        bitmap = gpuImage.getBitmapWithFilterApplied(ret)
        image.setImageBitmap(bitmap)
    }
    companion object {

        @JvmStatic
        fun newInstance(): BasicAdjust
        {
            return BasicAdjust()
        }

    }
}
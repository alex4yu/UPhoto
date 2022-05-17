package com.uphoto

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*


class BasicFilter : Fragment() {
    private lateinit var image: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var bitcheckpoint: Bitmap
    private lateinit var ogImage: Bitmap
    private lateinit var savebutton: Button
    private lateinit var cancelbutton: Button
    private lateinit var revert: Button
    private lateinit var brilliant: ImageButton
    private lateinit var brilliantCool: ImageButton
    private lateinit var brilliantWarm: ImageButton
    private lateinit var cinematic: ImageButton
    private lateinit var cinematicCool: ImageButton
    private lateinit var cinematicWarm: ImageButton
    private lateinit var vignette: ImageButton
    private lateinit var monochrome: ImageButton
    private lateinit var silverLuster: ImageButton
    private lateinit var filterName: TextView
    private lateinit var stackFilter: CheckBox
    private var stack = false
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_basic_filter, container, false)
        image = view.findViewById(R.id.imageView)
        brilliant = view.findViewById(R.id.brilliant)
        brilliantCool = view.findViewById(R.id.brilliantCool)
        brilliantWarm = view.findViewById(R.id.brilliantWarm)
        cinematic = view.findViewById(R.id.cinematic)
        cinematicCool = view.findViewById(R.id.cinematicCool)
        cinematicWarm = view.findViewById(R.id.cinematicWarm)
        vignette = view.findViewById(R.id.vignette)
        monochrome = view.findViewById(R.id.monochrome)
        silverLuster = view.findViewById(R.id.silverLuster)
        savebutton = view.findViewById(R.id.save_button)
        cancelbutton = view.findViewById(R.id.cancel_button)
        stackFilter = view.findViewById(R.id.checkBox)
        filterName = view.findViewById(R.id.filterName)
        revert = view.findViewById(R.id.revert)
        revert.setVisibility(View.INVISIBLE)
        allTransparent()
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
        stackFilter.setOnClickListener {
            stack = stackFilter.isChecked
        }
        brilliant.setOnClickListener{
            if(stack)
            {
                image.setImageBitmap(brilliant(bitmap))
                bitmap = brilliant(bitmap)
                filterName.setText("Brilliant")
                revert.setVisibility(VISIBLE)
            }
            else
            {
                image.setImageBitmap(brilliant(ogImage))
                bitmap = brilliant(ogImage)
                filterName.setText("Brilliant")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            brilliant.setBackgroundColor(Color.BLUE)

        }
        brilliantCool.setOnClickListener {
            if(stack)
            {
                image.setImageBitmap(cool(brilliant(bitmap)))
                bitmap = cool(brilliant(bitmap))
                filterName.setText("Brilliant Cool")
                revert.setVisibility(VISIBLE)
            }
            else
            {
                image.setImageBitmap(cool(brilliant(ogImage)))
                bitmap = cool(brilliant(ogImage))
                filterName.setText("Brilliant Cool")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            brilliantCool.setBackgroundColor(Color.BLUE)

        }

        brilliantWarm.setOnClickListener {
            if(stack)
            {
                image.setImageBitmap(warm(brilliant(bitmap)))
                bitmap = brilliant(bitmap)
                filterName.setText("Brilliant Warm")
                revert.setVisibility(VISIBLE)
            }
            else
            {
                image.setImageBitmap(warm((brilliant(ogImage))))
                bitmap = warm(brilliant(ogImage))
                filterName.setText("Brilliant Warm")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            brilliantWarm.setBackgroundColor(Color.BLUE)
        }
        cinematic.setOnClickListener {
            if(stack)
            {
                image.setImageBitmap(cinematic(bitmap))
                bitmap = cinematic(bitmap)
                filterName.setText("Cinematic")
                revert.setVisibility(VISIBLE)
            }
            else
            {
                image.setImageBitmap(cinematic(ogImage))
                bitmap = cinematic(ogImage)
                filterName.setText("Cinematic")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            cinematic.setBackgroundColor(Color.BLUE)
        }
        cinematicCool.setOnClickListener {
            if (stack) {
                image.setImageBitmap(cool(cinematic(bitmap)))
                bitmap = cool(cinematic(bitmap))
                filterName.setText("Cinematic Cool")
                revert.setVisibility(VISIBLE)
            } else {
                image.setImageBitmap(cool(cinematic(ogImage)))
                bitmap = cool(cinematic(ogImage))
                filterName.setText("Cinematic Cool")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            cinematicCool.setBackgroundColor(Color.BLUE)
        }
        cinematicWarm.setOnClickListener {
            if (stack) {
                image.setImageBitmap(warm(cinematic(bitmap)))
                bitmap = warm(cinematic(bitmap))
                filterName.setText("Cinematic Warm")
                revert.setVisibility(VISIBLE)
            } else {
                image.setImageBitmap(warm(cinematic(ogImage)))
                bitmap = warm(cinematic(ogImage))
                filterName.setText("Cinematic Warm")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            cinematicWarm.setBackgroundColor(Color.BLUE)
        }
        vignette.setOnClickListener {
            if(stack)
            {
                image.setImageBitmap(vignette(bitmap))
                bitmap = vignette(bitmap)
                filterName.setText("Vignette")
                revert.setVisibility(VISIBLE)
            }
            else
            {
                image.setImageBitmap(vignette(ogImage))
                bitmap = vignette(ogImage)
                filterName.setText("Vignette")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            vignette.setBackgroundColor(Color.BLUE)
        }
        monochrome.setOnClickListener {
            if (stack) {
                image.setImageBitmap(monochrome(bitmap))
                bitmap = monochrome(bitmap)
                filterName.setText("Monochrome")
                revert.setVisibility(VISIBLE)
            } else {
                image.setImageBitmap(monochrome(ogImage))
                bitmap = monochrome(ogImage)
                filterName.setText("Monochrome")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            monochrome.setBackgroundColor(Color.BLUE)
        }
        silverLuster.setOnClickListener {
            if (stack) {
                image.setImageBitmap(silverLuster(bitmap))
                bitmap = silverLuster(bitmap)
                filterName.setText("Monochrome")
                revert.setVisibility(VISIBLE)
            } else {
                image.setImageBitmap(silverLuster(ogImage))
                bitmap = silverLuster(ogImage)
                filterName.setText("Monochrome")
                revert.setVisibility(VISIBLE)
            }
            allTransparent()
            silverLuster.setBackgroundColor(Color.BLUE)
        }

        val activity: MainActivity? = activity as MainActivity?
        if (activity != null) {
            ogImage = activity.getbit()!!
        }
        bitmap = ogImage
        bitcheckpoint = ogImage
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
        brilliant.setImageBitmap(brilliant(bitmap))
        brilliantCool.setImageBitmap(cool(brilliant(bitmap)))
        brilliantWarm.setImageBitmap(warm(brilliant(bitmap)))
        cinematic.setImageBitmap(cinematic(bitmap))
        cinematicCool.setImageBitmap(cool(cinematic(bitmap)))
        cinematicWarm.setImageBitmap(warm(cinematic(bitmap)))
        vignette.setImageBitmap(vignette(bitmap))
        monochrome.setImageBitmap(monochrome(bitmap))
        silverLuster.setImageBitmap(monochrome(bitmap))

        return view
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
    private fun revert()
    {
        image.setImageBitmap(ogImage)
        bitmap = ogImage
        revert.setVisibility(View.INVISIBLE)
        filterName.setText("No Filter")
    }
    private fun allTransparent()
    {

        brilliant.setBackgroundColor(0x00000000)
        brilliantWarm.setBackgroundColor(0x00000000)
        brilliantCool.setBackgroundColor(0x00000000)
        cinematicWarm.setBackgroundColor(0x00000000)
        cinematic.setBackgroundColor(0x00000000)
        cinematicCool.setBackgroundColor(0x00000000)
        vignette.setBackgroundColor(0x00000000)
        monochrome.setBackgroundColor(0x00000000)
        silverLuster.setBackgroundColor(0x00000000)


    }
    private fun brilliant(input: Bitmap): Bitmap
    {
        var ret: Bitmap = input.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        var bitchange: Bitmap
        val vibranceFilter = GPUImageVibranceFilter(.5f)
        gpuImage.setFilter(vibranceFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)

        val saturationFilter = GPUImageSaturationFilter(1.1f)
        gpuImage.setFilter(saturationFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)

        val cm = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 10f,
                0f, 1f, 0f, 0f, 10f,
                0f, 0f, 1f, 0f, 10f,
                0f, 0f, 0f, 1f, 0f
            )
        )


        val canvas = Canvas(ret)

        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(ret, 0.toFloat(), 0.toFloat(), paint)
        bitchange = ret
        return bitchange
    }

    private fun cool(input: Bitmap):Bitmap
    {
        var bitchange = brilliant(input)
        val cm = ColorMatrix(
            floatArrayOf(
                0.9f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1.2f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )


        val ret: Bitmap = bitchange.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(ret)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(ret, 0.toFloat(), 0.toFloat(), paint)
        bitchange = ret
        return bitchange
    }
    private fun warm(input: Bitmap):Bitmap
    {
        var bitchange = brilliant(input)
        val cm = ColorMatrix(
            floatArrayOf(
                1.2f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 0.9f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )


        val ret: Bitmap = bitchange.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(ret)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(ret, 0.toFloat(), 0.toFloat(), paint)
        bitchange = ret
        return bitchange
    }
    private fun cinematic(input: Bitmap): Bitmap
    {

        val gpuImage = GPUImage(context)

        var ret: Bitmap = input.copy(Bitmap.Config.ARGB_8888, true)
        val contrastFilter = GPUImageContrastFilter(1.15f)
        gpuImage.setFilter(contrastFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)

        val vibranceFilter = GPUImageVibranceFilter(.2f)
        gpuImage.setFilter(vibranceFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)

        val sharpenFilter = GPUImageSharpenFilter(0.5f)
        gpuImage.setFilter(sharpenFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)


        val bitchange = ret
        return bitchange
    }
    private fun vignette(input: Bitmap): Bitmap
    {
        val gpuImage = GPUImage(context)

        var ret: Bitmap = input.copy(Bitmap.Config.ARGB_8888, true)
        val vignetteFilter = GPUImageVignetteFilter(
            PointF(0.5f, 0.5f),
            floatArrayOf(0.0f, 0.0f, 0.0f),
            .3f,
            .7f)
        gpuImage.setFilter(vignetteFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)
        val contrastFilter = GPUImageContrastFilter(1.1f)
        gpuImage.setFilter(contrastFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)
        val brightnessFilter = GPUImageBrightnessFilter(-0.04f)
        gpuImage.setFilter(brightnessFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)


        return ret
    }
    private fun monochrome(input: Bitmap): Bitmap
    {
        var ret: Bitmap = input.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val monochromefilter = GPUImageMonochromeFilter(1f,  floatArrayOf(.3f,.3f,.3f,.3f))
        gpuImage.setFilter(monochromefilter)

        val bitchange = gpuImage.getBitmapWithFilterApplied(ret)
        return bitchange
    }
    private fun silverLuster(input: Bitmap): Bitmap
    {
        var bitchange = monochrome(input)
        var ret: Bitmap = bitchange.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val brightnessFilter = GPUImageBrightnessFilter(.1f)
        gpuImage.setFilter(brightnessFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)
        val contrastFilter = GPUImageContrastFilter(1.6f)
        gpuImage.setFilter(contrastFilter)
        bitchange = gpuImage.getBitmapWithFilterApplied(ret)
        return bitchange
    }
    companion object {
        fun newInstance(): BasicFilter
        {
            return BasicFilter()
        }
    }
}
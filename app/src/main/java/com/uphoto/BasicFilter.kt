package com.uphoto

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.slider.Slider
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter


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
    private lateinit var dramatic: ImageButton
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
        dramatic = view.findViewById(R.id.dramatic)
        savebutton = view.findViewById(R.id.save_button)
        cancelbutton = view.findViewById(R.id.cancel_button)
        stackFilter = view.findViewById(R.id.checkBox)
        filterName = view.findViewById(R.id.filterName)
        revert = view.findViewById(R.id.revert)
        revert.setVisibility(View.INVISIBLE)
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

        }
        brilliantCool.setOnClickListener {
            if(stack)
            {
                image.setImageBitmap(brilliantCool(bitmap))
                bitmap = brilliant(bitmap)
                filterName.setText("Brilliant Cool")
                revert.setVisibility(VISIBLE)
            }
            else
            {
                image.setImageBitmap(brilliantCool(ogImage))
                bitmap = brilliant(ogImage)
                filterName.setText("Brilliant Cool")
                revert.setVisibility(VISIBLE)
            }

        }

        brilliantWarm.setOnClickListener {
            if(stack)
            {
                image.setImageBitmap(brilliantWarm(bitmap))
                bitmap = brilliant(bitmap)
                filterName.setText("Brilliant Warm")
                revert.setVisibility(VISIBLE)
            }
            else
            {
                image.setImageBitmap(brilliantWarm(ogImage))
                bitmap = brilliant(ogImage)
                filterName.setText("Brilliant Warm")
                revert.setVisibility(VISIBLE)
            }

        }
        dramatic.setOnClickListener {
            if(stack)
            {
                image.setImageBitmap(dramatic(bitmap))
                bitmap = brilliant(bitmap)
                filterName.setText("Dramatic")
                revert.setVisibility(VISIBLE)
            }
            else
            {
                image.setImageBitmap(dramatic(ogImage))
                bitmap = brilliant(ogImage)
                filterName.setText("Dramatic")
                revert.setVisibility(VISIBLE)
            }

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
        brilliantCool.setImageBitmap(brilliantCool(bitmap))
        brilliantWarm.setImageBitmap(brilliantWarm(bitmap))
        dramatic.setImageBitmap(dramatic(bitmap))
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

    private fun brilliantCool(input: Bitmap):Bitmap
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
    private fun brilliantWarm(input: Bitmap):Bitmap
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
    private fun dramatic(input: Bitmap): Bitmap
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
    companion object {
        fun newInstance(): BasicFilter
        {
            return BasicFilter()
        }
    }
}
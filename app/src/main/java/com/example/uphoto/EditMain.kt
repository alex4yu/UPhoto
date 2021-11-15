package com.example.uphoto

import android.graphics.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.graphics.ColorMatrixColorFilter

import android.graphics.ColorMatrix
import android.widget.Button


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [EditMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditMain : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var image: ImageView
    private lateinit var brighten: Button
    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_main, container, false)
        image = view.findViewById(R.id.imageView)
        brighten = view.findViewById(R.id.brightenbutton)
        brighten.setOnClickListener {
            brighten()
        }
        val activity: MainActivity? = activity as MainActivity?


        if (activity != null) {
            bitmap = activity.getbit()!!
        }
        image.setImageBitmap(bitmap)
        return view

    }
    fun brighten(){
        image.setColorFilter(brightIt(100))
    }
    fun brightIt(fb: Int): ColorMatrixColorFilter? {
        val cmB = ColorMatrix()
        cmB.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, fb.toFloat(),
                0f, 1f, 0f, 0f, fb.toFloat(),
                0f, 0f, 1f, 0f, fb.toFloat(),
                0f, 0f, 0f, 1f, 0f
            )
        )
        val colorMatrix = ColorMatrix()
        colorMatrix.set(cmB)

        return ColorMatrixColorFilter(colorMatrix)
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
package com.uphoto

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceLandmark


class FaceFilter : Fragment() {
    private lateinit var image: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var bitcheckpoint: Bitmap
    private lateinit var ogImage: Bitmap
    private lateinit var detectButton: Button
    private lateinit var savebutton: Button
    private lateinit var cancelbutton: Button
    private lateinit var revert: Button


    private var height = 0
    private var width = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_face_filter, container, false)
        image = view.findViewById(R.id.imageView)
        detectButton = view.findViewById(R.id.detect)
        detectButton.setOnClickListener {
            detectFace(bitmap)

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
        return view
    }
    private fun setDimensions(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.layoutParams = params
    }
    private fun processFace(bounds: Rect, left: PointF, right: PointF)
    {
        val y = bounds.centerY() - bounds.height()/2f
        val x = bounds.centerX() - bounds.width()/2f
        val h = bounds.height()
        val w = bounds.width()
        val leftX = left.x
        val leftY = left.y
        Log.d("Main_activity", "$leftY")
        val rightX = right.x
        val rightY = right.y
        //glasses(leftY, w, h, x)
        flowerBorder(x, y, h, w)
        sombrero(x, y, h, w)

    }
    private fun glasses(eye: Float, w: Int, h: Int, x: Float)
    {
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val matrix1 = Matrix()
        val glasses = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.glasses
        )
        val scale = 1f*w/glasses.width
        matrix1.postScale(scale, scale)
        val resizedGlasses = Bitmap.createBitmap(
            glasses, 0, 0, glasses.width, glasses.height, matrix1, false
        )
        val canvas = Canvas(ret)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(resizedGlasses, x, eye-resizedGlasses.height/2, paint)
        image.setImageBitmap(ret)
        bitmap = ret
    }
    private fun sombrero(x1: Float,y1: Float, h: Int, w: Int )
    {
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val sombreroImage = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.sombrero
        )
        val matrix1 = Matrix()
        val scale = 2f*w/sombreroImage.width
        matrix1.postScale(scale, scale)
        val resizedSombrero = Bitmap.createBitmap(
            sombreroImage, 0, 0, sombreroImage.width, sombreroImage.height, matrix1, false
        )
        val canvas = Canvas(ret)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(resizedSombrero, x1-w/2f, y1-resizedSombrero.height, paint)
        image.setImageBitmap(ret)
        bitmap = ret
    }
    private fun flowerBorder(x1: Float,y1: Float, h: Int, w: Int )
    {
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val flower1 = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.flower1
        )
        val flower2 = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.flower2
        )
        val matrix1 = Matrix()
        val scale1w = w/5f/flower1.width
        val scale1h = w/5f/flower1.height
        matrix1.postScale(scale1w, scale1h)
        val resizedflower1 = Bitmap.createBitmap(
            flower1, 0, 0, flower1.width, flower1.height, matrix1, false
        )
        val rfw = resizedflower1.width
        val matrix2 = Matrix()
        val scale2w = w/5f/flower2.width
        val scale2h = w/5f/flower2.height
        matrix2.postScale(scale2w, scale2h)
        val resizedflower2 = Bitmap.createBitmap(
            flower2, 0, 0, flower2.width, flower2.height, matrix2, false
        )
        val canvas = Canvas(ret)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        var x = x1
        var y = y1
        var flower_1 = true
        while (x+rfw<x1+w)
        {
            if (flower_1)
            {
                canvas.drawBitmap(resizedflower1, x.toFloat(), y.toFloat(), paint)
                flower_1 = false
            }
            else
            {
                canvas.drawBitmap(resizedflower2, x.toFloat(), y.toFloat(), paint)
                flower_1 = true
            }
            x=x+rfw
        }
        while (y+rfw<y1+h)
        {
            if (flower_1)
            {
                canvas.drawBitmap(resizedflower1, x.toFloat(), y.toFloat(), paint)
                flower_1 = false
            }
            else
            {
                canvas.drawBitmap(resizedflower2, x.toFloat(), y.toFloat(), paint)
                flower_1 = true
            }
            y=y+rfw
        }
        while (x>x1)
        {
            if (flower_1)
            {
                canvas.drawBitmap(resizedflower1, x.toFloat(), y.toFloat(), paint)
                flower_1 = false
            }
            else
            {
                canvas.drawBitmap(resizedflower2, x.toFloat(), y.toFloat(), paint)
                flower_1 = true
            }
            x=x-rfw
        }
        while (y>y1)
        {
            if (flower_1)
            {
                canvas.drawBitmap(resizedflower1, x.toFloat(), y.toFloat(), paint)
                flower_1 = false
            }
            else
            {
                canvas.drawBitmap(resizedflower2, x.toFloat(), y.toFloat(), paint)
                flower_1 = true
            }
            y=y-rfw
        }

        image.setImageBitmap(ret)
        bitmap = ret
    }

    private fun detectFace(input: Bitmap)
    {
        val pic = InputImage.fromBitmap(input, 0)
        val detector = FaceDetection.getClient()
        val result = detector.process(pic)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                // ...
                for (face in faces) {
                    val bounds = face.boundingBox


                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                    leftEar?.let {
                        val leftEarPos = leftEar.position
                    }
                    var rightEyePos = PointF()
                    var leftEyePos = PointF()
                    val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)
                    rightEye?.let {
                        rightEye.position.set(rightEyePos)
                    }
                    val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
                    leftEye?.let {
                        val test = leftEye.position.y
                        leftEye.position.set(leftEyePos)
                        Log.d("Main_activity", "249 $test")
                    }
                    val test2 = leftEyePos.y
                    Log.d("Main_activity", "253 $test2")
                    processFace(bounds, leftEyePos, rightEyePos)

                    // If contour detection was enabled:
                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                    val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                    // If classification was enabled:
                    if (face.smilingProbability != null) {
                        val smileProb = face.smilingProbability
                    }
                    if (face.rightEyeOpenProbability != null) {
                        val rightEyeOpenProb = face.rightEyeOpenProbability
                    }


                    // If face tracking was enabled:
                    if (face.trackingId != null) {
                        val id = face.trackingId
                    }
                }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }


    }

    companion object {
        fun newInstance(): FaceFilter
        {
            return FaceFilter()
        }

    }
}
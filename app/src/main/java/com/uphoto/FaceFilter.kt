package com.uphoto

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.io.OutputStream
import java.lang.Exception


class FaceFilter : Fragment() {
    private lateinit var image: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var bitcheckpoint: Bitmap
    private lateinit var ogImage: Bitmap
    private lateinit var sunglasses: ImageButton
    private lateinit var glasses: ImageButton
    private lateinit var sombrero: Button
    private lateinit var flowerBorder: ImageButton
    private lateinit var savebutton: Button
    private lateinit var cancelbutton: Button
    private lateinit var revert: Button
    private lateinit var finishButton: Button
    private lateinit var home:ImageButton
    private var accurate_mode: Boolean = true
    private var second = false
    private var cycle = 0
    private var filter = ""
    private var dataPasser: OnDataPass? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }


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
        sunglasses = view.findViewById(R.id.sunglasss)
        glasses = view.findViewById(R.id.glasses)
        flowerBorder = view.findViewById(R.id.flowerBorder)
        sombrero = view.findViewById(R.id.sombrero)
        revert = view.findViewById(R.id.revert)
        home = view.findViewById(R.id.home)
        home.setOnClickListener{
            newFrag("main menu alt")
        }
        revert.setOnClickListener {
            revert()
        }
        sunglasses.setOnClickListener {
            detectFace(bitmap, true)
            filter = "sunglasses"
        }
        glasses.setOnClickListener {
            detectFace(bitmap, true)
            filter = "glasses"
        }
        sombrero.setOnClickListener {
            detectFace(bitmap, true)
            filter = "sombrero"
        }
        flowerBorder.setOnClickListener {
            detectFace(bitmap, true)
            filter = "flower border"
        }
        val activity: MainActivity? = activity as MainActivity?
        if (activity != null) {
            ogImage = activity.getbit()!!
        }
        finishButton = view.findViewById(R.id.finish_button)
        finishButton.setOnClickListener {
            context?.let { it1 -> saveImage(bitmap, it1, "Uphoto")
                newFrag("main menu")}

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
    private fun revert()
    {
        image.setImageBitmap(ogImage)
        bitmap = ogImage
    }
    private fun processFace(bounds: Rect, left: PointF, right: PointF)
    {
        val y = bounds.centerY() - bounds.height()/2f
        val x = bounds.centerX() - bounds.width()/2f
        val h = bounds.height()
        val w = bounds.width()
        val leftX = left.x
        val leftY = left.y
        val rightX = right.x
        val rightY = right.y
        if (filter.equals("glasses"))
        {
            glasses(leftY, w, h, x)
        }
        else if (filter.equals("sunglasses"))
        {
            sunglasses(leftY, w, h, x)
        }
        else if (filter.equals("sombrero"))
        {
            sombrero(x, y, h, w)
        }
        else if (filter.equals("flower border"))
        {
            if(accurate_mode)
            {
                flowerBorderAccurate(x, y, h, w)
            }
            else
            {
                flowerBorderFast(x, y, h, w)
            }

        }

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
        val test = x+w/2f-resizedGlasses.width/2f
        canvas.drawBitmap(resizedGlasses, x+w/20f, eye-resizedGlasses.height/2, paint)
        image.setImageBitmap(ret)
        bitmap = ret
    }
    private fun sunglasses(eye: Float, w: Int, h: Int, x: Float)
    {
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val matrix1 = Matrix()
        val glasses = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.sunglasses
        )
        val scale = 1f*w/glasses.width
        matrix1.postScale(scale, scale)
        val resizedGlasses = Bitmap.createBitmap(
            glasses, 0, 0, glasses.width, glasses.height, matrix1, false
        )
        val canvas = Canvas(ret)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(resizedGlasses, x+w/20f, eye-resizedGlasses.height/2, paint)
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
    private fun flowerBorderAccurate(x1: Float,y1: Float, h: Int, w: Int )
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
        var x = x1-rfw
        var y = y1-rfw
        var flower_1 = true
        while (x<x1+w)
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
        while (y<y1+h)
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
        while (x>x1-rfw)
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
        while (y>y1-rfw)
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
    private fun flowerBorderFast(x1: Float,y1: Float, h: Int, w: Int )
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
        var x = x1-rfw
        var y = y1-rfw
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
        while (x>x1-rfw)
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
        while (y>y1-rfw)
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
    private fun detectFace(input: Bitmap, accurateMode: Boolean)
    {
        val pic = InputImage.fromBitmap(input, 0)
        var options = FaceDetectorOptions.Builder().build()

        if (accurateMode)
        {
            options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .build()
        }
        else
        {
            accurate_mode = false
            options = FaceDetectorOptions.Builder()
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .build()
        }

        val detector = FaceDetection.getClient(options)
        val result = detector.process(pic)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                // ...
                val size = faces.size
                cycle++
                if (size == 0 && cycle < 2)
                {
                    detectFace(bitmap, false)
                }
                Log.d("Main_act", "$size")
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
                        rightEyePos = PointF(rightEye.position.x, rightEye.position.y)
                    }
                    val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
                    leftEye?.let {
                        val test = leftEye.position.y
                        leftEyePos = PointF(leftEye.position.x, leftEye.position.y)

                    }

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
    private fun newFrag(name: String)
    {
        dataPasser?.passBit(bitmap)
        dataPasser?.onDataPass(name)
    }
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {

        val values = contentValues()
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        // RELATIVE_PATH and IS_PENDING are introduced in API 29.

        val uri: Uri? =
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
        }
    }
    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }
    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun newInstance(): FaceFilter
        {
            return FaceFilter()
        }

    }
}
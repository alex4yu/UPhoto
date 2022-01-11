package com.example.uphoto



import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import java.io.IOException
import android.os.Environment
import android.view.Gravity
import android.widget.Toast
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception


private const val TAG = "main_activity"
private const val PICK_PHOTO_CODE = 1
class MainActivity : AppCompatActivity(), OnDataPass {

    private lateinit var nextScreen : String
    private lateinit var currentScreen: String
    private  var currentimage: Bitmap? = null
    private lateinit var image: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "before set layout")
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
        Log.d(TAG, "set current frag")
        if (currentFragment == null) {
            val fragment = MainMenu.newInstance()
            currentScreen = "Main Menu"
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()

        }

    }
    override fun onDataPass(data: String) {
        nextScreen = data
        newScreen(data)
        Log.d(TAG,"hello " + data)
    }

    override fun passBit(bitmap: Bitmap) {
        currentimage = bitmap
    }


    fun newScreen(new: String){
        if(new.equals("select screen"))
        {
            choosePhoto()
        }
        else if(new.equals("edit main"))
        {
            val fragment = EditMain.newInstance()
            currentScreen="edit main"
            swapFrag(fragment)
            Log.d(TAG, "edit main frag")
        }
        else if (new.equals("basic filters"))
        {
            val fragment = basic_filters.newInstance()
            currentScreen="basic filters"
            swapFrag(fragment)
            Log.d(TAG, "basic filters frag")
        }
        else if (new.equals("main menu"))
        {
            val toast = Toast.makeText(this,"photo saved", Toast.LENGTH_LONG )
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
            val fragment = MainMenu.newInstance()
            currentScreen="main menu"
            swapFrag(fragment)

        }
        else if(new.equals("draw"))
        {
            val fragment = draw.newInstance()
            currentScreen="draw"
            swapFrag(fragment)
        }
    }
    fun swapFrag(fragment: Fragment)
    {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    fun getbit(): Bitmap? {
        return currentimage
    }
    fun choosePhoto(){
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.

         startActivityForResult(intent, PICK_PHOTO_CODE)


    }

    fun loadFromUri(photoUri: Uri): Bitmap {

        try {
            // check version of Android on device
            image =    if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d(TAG, "image return")
        return image
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == PICK_PHOTO_CODE) {
            Log.d(TAG, "image data retrieved")
            val photoUri = data.data

            // Load the image located at photoUri into selectedImage
            currentimage = loadFromUri(photoUri!!)


            val fragment = SelectPhoto.newInstance()
            currentScreen = "select screen"
            swapFrag(fragment)


        }
    }

}
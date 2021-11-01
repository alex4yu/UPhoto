package com.example.uphoto

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.uphoto.databinding.ActivityEditScreenBinding

private const val BITMAP = "com.alex_.android.UPhoto.picture"
private const val TAG = "editscreen"
class EditScreen : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityEditScreenBinding
    private lateinit var imageBit: Bitmap
    private lateinit var imagePreview: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityEditScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imagePreview = findViewById(R.id.ivPreview)
        imageBit = intent.getParcelableExtra<Bitmap>(BITMAP)!!
        Log.d(TAG, "imagebit set")
        //setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_edit_screen)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        imagePreview.setImageBitmap(imageBit)
        Log.d(TAG, "image displayed")
        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_edit_screen)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    companion object {
        fun newIntent(packageContext: Context, picture: Bitmap): Intent {
            Log.d(TAG, "right before intent return")
            return Intent(packageContext, EditScreen::class.java).apply {
                putExtra(BITMAP, picture)
            }
        }
    }
}
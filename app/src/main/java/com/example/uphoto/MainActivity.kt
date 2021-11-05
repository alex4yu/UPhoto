package com.example.uphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var selectButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectButton = findViewById(R.id.select_button)

        selectButton.setOnClickListener{
            val intent = Intent(this@MainActivity, SelectPhoto::class.java)
            startActivity(intent)
        }


    }

}
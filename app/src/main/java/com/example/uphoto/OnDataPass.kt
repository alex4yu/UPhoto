package com.example.uphoto

import android.graphics.Bitmap

interface OnDataPass {
    fun onDataPass(data: String)
    fun passBit(bitmap: Bitmap)

}
package com.example.skripsi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ToggleButton

class deteksi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deteksi)

        val cameraSwitch = findViewById<ToggleButton>(R.id.facing_switch)

        cameraSwitch.setOnClickListener {

        }
    }
}
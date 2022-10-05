package com.example.skripsi

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.action_bar_layout);

        val textPose = findViewById<TextView>(R.id.namaPose)
        val detailPose = findViewById<TextView>(R.id.detailPose)
        val bundle = intent.extras
        if (bundle != null){
            Log.d("test data diterima",bundle.toString())
            textPose.text = bundle.getString("nama")
            detailPose.text = bundle.getString("des")
        }
    }
}
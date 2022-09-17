package com.example.skripsi

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.action_bar_layout);

        val textPose = findViewById<TextView>(R.id.pose1)
        val textPose2 = findViewById<TextView>(R.id.pose2)
        val textPose3 = findViewById<TextView>(R.id.pose3)
        val card = findViewById<CardView>(R.id.card_view)
        val card2 = findViewById<CardView>(R.id.card_view2)
        val card3 = findViewById<CardView>(R.id.card_view3)

        val NamaPose = textPose.text
        val NamaPose2 = textPose.text
        val NamaPose3 = textPose.text

        card.setOnClickListener {
            val i = Intent(this@MainActivity, detail::class.java)
            i.putExtra("KEY_NAME",NamaPose )
            startActivity(i)
        }
        if (card.isClickable) {

        }
    }
}
package com.example.skripsi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.skripsi.databaseLokal.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DBHelper(this, null)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.action_bar_layout);

        val textPose = findViewById<TextView>(R.id.pose1)
        val textPose2 = findViewById<TextView>(R.id.pose2)
        val textPose3 = findViewById<TextView>(R.id.pose3)
        val card = findViewById<CardView>(R.id.card_view)
        val card2 = findViewById<CardView>(R.id.card_view2)
        val card3 = findViewById<CardView>(R.id.card_view3)

        val NamaPose = textPose.text.toString()
        val NamaPose2 = textPose2.text.toString()
        val NamaPose3 = textPose3.text.toString()

        db.getAll()
        card.setOnLongClickListener {
            val name = NamaPose
            val des =  "Pose benefits\n"
            val temp = db.getByName(name)
            if (temp != null) {
                if(temp.moveToFirst()) {
                    db.replace(name,des)
                    val i = Intent(this@MainActivity, detail::class.java)
                    i.putExtra("KEY_NAME",NamaPose)
                    startActivity(i)
                }else {
                    db.addName(name, des)
                    val i = Intent(this@MainActivity, detail::class.java)
                    i.putExtra("KEY_NAME",NamaPose)
                    startActivity(i)
                }
            }
            true
        }
        card2.setOnLongClickListener {
            val name = NamaPose2
            val des =  "Pose benefits\n"
            val temp = db.getByName(name)
            if (temp != null) {
                if(temp.moveToFirst()) {
                    db.replace(name,des)
                    val i = Intent(this@MainActivity, detail::class.java)
                    i.putExtra("KEY_NAME",NamaPose2)
                    startActivity(i)
                }else {
                    db.addName(name, des)
                    val i = Intent(this@MainActivity, detail::class.java)
                    i.putExtra("KEY_NAME",NamaPose2)
                    startActivity(i)
                }
            }
            true
        }
        card3.setOnLongClickListener {
            val name = NamaPose3
            val des =  "Pose benefits\n"
            val temp = db.getByName(name)
            if (temp != null) {
                if(temp.moveToFirst()) {
                    db.replace(name,des)
                    val i = Intent(this@MainActivity, detail::class.java)
                    i.putExtra("KEY_NAME",NamaPose2)
                    startActivity(i)
                }else {
                    db.addName(name, des)
                    val i = Intent(this@MainActivity, detail::class.java)
                    i.putExtra("KEY_NAME",NamaPose2)
                    startActivity(i)
                }
            }
            true
        }

        card.setOnClickListener {
            val i = Intent(this@MainActivity, deteksi::class.java)
            i.putExtra("KEY_NAME",NamaPose)
            startActivity(i)
        }
        card2.setOnClickListener{
            val i = Intent(this@MainActivity, deteksi::class.java)
            i.putExtra("KEY_NAME",NamaPose2)
            startActivity(i)
        }
        card3.setOnClickListener{
            val i = Intent(this@MainActivity, deteksi::class.java)
            i.putExtra("KEY_NAME",NamaPose3)
            startActivity(i)
        }

    }
}
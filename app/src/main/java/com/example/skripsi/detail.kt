package com.example.skripsi

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class detail : AppCompatActivity() {

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val cekTema = isUsingNightModeResources()
        if (cekTema==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            val actionBar: ActionBar?
            actionBar = supportActionBar
            val colorDrawable = ColorDrawable(Color.parseColor("#0F9D58"))

            actionBar!!.setBackgroundDrawable(colorDrawable)
            delegate.applyDayNight()
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        }
        val actionBar = getSupportActionBar()

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.acbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val textPose = findViewById<TextView>(R.id.namaPose)
        val detailPose = findViewById<TextView>(R.id.detailPose)
        val bundle = intent.extras
        if (bundle != null){
            Log.d("test data diterima",bundle.toString())
            textPose.text = bundle.getString("nama")
            detailPose.text = bundle.getString("des")
        }
        val pict= findViewById<ImageView>(R.id.image)
        when (textPose.text) {
            "Pose Plank" -> {
                pict.setImageResource(R.drawable.poseplank)
            }
            "Pose Tree" -> {
                pict.setImageResource(R.drawable.treeani)
            }
            else -> {
                pict.setImageResource(R.drawable.cobraani)
            }
        }

    }
}
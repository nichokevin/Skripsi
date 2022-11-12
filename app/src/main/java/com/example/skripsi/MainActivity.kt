package com.example.skripsi

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.example.skripsi.databaseLokal.DBHelper


class MainActivity : AppCompatActivity() {

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DBHelper(this, null)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.action_bar_layout);

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

        val textPose = findViewById<TextView>(R.id.pose1)
        val textPose2 = findViewById<TextView>(R.id.pose2)
        val textPose3 = findViewById<TextView>(R.id.pose3)
        val card = findViewById<CardView>(R.id.card_view)
        val card2 = findViewById<CardView>(R.id.card_view2)
        val card3 = findViewById<CardView>(R.id.card_view3)

        val NamaPose = textPose.text.toString()
        val NamaPose2 = textPose2.text.toString()
        val NamaPose3 = textPose3.text.toString()

        card.setOnLongClickListener {
            val name = NamaPose
            val des =  "Pose benefits\n"
            val temp = db.getByName(name)
            if (temp != null) {
                db.replace(name,des)
                val bundle = Bundle()
                bundle.putString("nama",temp[0])
                bundle.putString("des",temp[1])
                Log.d("isi bundle", bundle.getString("nama").toString())
                val intent = Intent(this, detail::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }else {
                db.addName(name,des)
                val temp = db.getByName(name)
                if (temp != null) {
                    val bundle = Bundle()
                    bundle.putString("nama",temp[0])
                    bundle.putString("des",temp[1])
                    Log.d("isi bundle", bundle.getString("nama").toString())
                    val intent = Intent(this, detail::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
            true
        }
        card2.setOnLongClickListener {
            val name = NamaPose2
            val des =  "Pose benefits\n"
            val temp = db.getByName(name)
            if (temp != null) {
                db.replace(name,des)
                val bundle = Bundle()
                bundle.putString("nama",temp[0])
                bundle.putString("des",temp[1])
                Log.d("isi bundle", bundle.getString("nama").toString())
                val intent = Intent(this, detail::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }else {
                db.addName(name,des)
                val temp = db.getByName(name)
                if (temp != null) {
                    val bundle = Bundle()
                    bundle.putString("nama",temp[0])
                    bundle.putString("des",temp[1])
                    Log.d("isi bundle", bundle.getString("nama").toString())
                    val intent = Intent(this, detail::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
            true
        }

        card3.setOnLongClickListener {
            val name = NamaPose3
            val des =  "Postur kobra (bhujangasana) adalah postur melengkungkan punggung ke " +
                    "belakang yang dilakukan dengan meregangkan otot dada, lengan, dan bahu. Postur ini " +
                    "sangat baik untuk meningkatkan kelenturan tulang belakang dan mengurangi nyeri punggung."
            val temp = db.getByName(name)
            if (temp != null) {
                db.replace(name,des)
                val bundle = Bundle()
                bundle.putString("nama",temp[0])
                bundle.putString("des",temp[1])
                Log.d("isi bundle", bundle.getString("nama").toString())
                val intent = Intent(this, detail::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }else {
                db.addName(name,des)
                val temp = db.getByName(name)
                if (temp != null) {
                    val bundle = Bundle()
                    bundle.putString("nama",temp[0])
                    bundle.putString("des",temp[1])
                    Log.d("isi bundle", bundle.getString("nama").toString())
                    val intent = Intent(this, detail::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
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
package com.example.skripsi

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.example.skripsi.databaseLokal.DBHelper
import kotlinx.android.synthetic.main.action_bar_layout.*


@Suppress("NAME_SHADOWING")
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
    private val MY_CAMERA_REQUEST_CODE = 100

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DBHelper(this, null)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
        }

        btnHelp.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)

            alertDialog.apply {
                setTitle("Petunjuk penggunaan aplikasi")
                setMessage("- Touch 1 kali -> halaman deteksi\n- Long tap -> halaman detail" +
                        "\n- Plank & Cobra : pilih badan yang paling terlihat oleh kamera")
                setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
                }
            }.create().show()
        }

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
            val des =  "    Plank merupakan salah satu gerakan olahraga yang cukup mudah dilakukan. Meski demikian, " +
                    "memahami gerakan dan manfaat plank penting dilakukan sebelum melakukan gerakan ini agar Anda " +
                    "terhindar dari cedera.\n" +
                    "    Gerakan plank hampir mirip dengan push-up tetapi menggunakan lengan bawah sebagai tumpuan. " +
                    "Latihan ini sangat efektif untuk memperkuat seluruh otot tubuh, terutama otot-otot perut serta bagian inti tubuh."
            var temp = db.getByName(name)
            if (temp != null) {
                db.replace(name,des)
                temp=db.getByName(name)
                val bundle = Bundle()
                bundle.putString("nama", temp!![0])
                bundle.putString("des", temp[1])
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
            val des =  "    Tree pose merupakan pose keseimbangan berdiri pertama yang umumnya " +
                    "diajarkan kepada pemula karena cukup sederhana. Pose ini menargetkan kaki (paha, betis, " +
                    "pergelangan kaki), otot inti (core), dan keseimbangan yang dilakukan dengan berdiri satu kaki. " +
                    "\n    Selain memperkuat kaki dan otot inti, pose ini juga akan melatih anggota tubuh lain seperti paha " +
                    "bagian dalam, dada, hingga bahu. Pose ini pun dapat membantu dalam meredakan linu di bagian pinggul."
            var temp = db.getByName(name)
            if (temp != null) {
                db.replace(name,des)
                temp=db.getByName(name)
                val bundle = Bundle()
                bundle.putString("nama",temp!![0])
                bundle.putString("des",temp!![1])
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
            val des =  "    Postur kobra (bhujangasana) adalah postur melengkungkan punggung ke " +
                    "belakang yang dilakukan dengan meregangkan otot dada, lengan, dan bahu. Postur ini " +
                    "sangat baik untuk meningkatkan kelenturan tulang belakang dan mengurangi nyeri punggung. \n    Bergerak " +
                    "dengan pose ini sangat membantu untuk melawan postur membungkuk dan bentuk tubuh yang membulat. Postur " +
                    "yang tidak baik ini dialami oleh banyak orang, khususnya para pekerja kantoran yang sebagian besar " +
                    "waktunya dihabiskan dengan duduk di depan komputer."
            var temp = db.getByName(name)
            if (temp != null) {
                db.replace(name,des)
                temp=db.getByName(name)
                val bundle = Bundle()
                bundle.putString("nama",temp!![0])
                bundle.putString("des",temp!![1])
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
            val alertDialog = AlertDialog.Builder(this)

            alertDialog.apply {
                setTitle("Plank (left/right)")
                setPositiveButton("Kiri") { _: DialogInterface?, _: Int ->
                    val i = Intent(this@MainActivity, deteksi::class.java)
                    i.putExtra("KEY_NAME",NamaPose)
                    i.putExtra("arah","kiri")
                    startActivity(i)
                }
                setNegativeButton("Kanan") { _, _ ->
                    val i = Intent(this@MainActivity, deteksi::class.java)
                    i.putExtra("KEY_NAME",NamaPose)
                    i.putExtra("arah","kanan")
                    startActivity(i)
                }
            }.create().show()

        }
        card2.setOnClickListener{
            val i = Intent(this@MainActivity, deteksi::class.java)
            i.putExtra("KEY_NAME",NamaPose2)
            startActivity(i)
        }
        card3.setOnClickListener{
            val alertDialog = AlertDialog.Builder(this)

            alertDialog.apply {
                setTitle("Plank (left/right)")
                setPositiveButton("Kiri") { _: DialogInterface?, _: Int ->
                    val i = Intent(this@MainActivity, deteksi::class.java)
                    i.putExtra("KEY_NAME",NamaPose3)
                    i.putExtra("arah","kiri")
                    startActivity(i)
                }
                setNegativeButton("Kanan") { _, _ ->
                    val i = Intent(this@MainActivity, deteksi::class.java)
                    i.putExtra("KEY_NAME",NamaPose3)
                    i.putExtra("arah","kanan")
                    startActivity(i)
                }
            }.create().show()
        }

    }
}

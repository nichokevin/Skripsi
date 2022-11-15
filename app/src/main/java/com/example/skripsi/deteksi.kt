package com.example.skripsi

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.android.synthetic.main.activity_deteksi.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.schedule
import kotlin.math.atan2

private var drawColor = Color.GREEN


private class PoseAnalyzer(private val poseFoundListener: (Pose) -> Unit) : ImageAnalysis.Analyzer {

    private val options = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
        .build()

    private val poseDetector = PoseDetection.getClient(options)



    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Accurate pose detector on static images, when depending on the pose-detection-accurate sdk

            poseDetector
                .process(image)
                .addOnSuccessListener { pose ->
                    poseFoundListener(pose)
                    imageProxy.close()
                }
                .addOnFailureListener { error ->
                    Log.d(ContentValues.TAG, "Failed to process the image")
                    error.printStackTrace()
                    imageProxy.close()
                }
        }
    }
}

class RectOverlay constructor(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val STROKE_WIDTH = 3f // has to be float

    // Set up the paint with which to draw.
    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    fun clear() {
        extraCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }

    internal fun drawLine(
        startLandmark: PoseLandmark?,
        endLandmark: PoseLandmark?,
        paint: Paint
    ) {
        val start = startLandmark!!.position
        val end = endLandmark!!.position


        val xmul = 3.3f
        val ymul = 3.3f

        extraCanvas.drawLine(
            (start.x * xmul) - 250, start.y* ymul, (end.x* xmul) -250, end.y* ymul, paint
        )
        invalidate()
    }

    internal fun drawNeck(
        _mataKiri: PoseLandmark?,
        _mataKanan: PoseLandmark?,
        _bahuKiri: PoseLandmark?,
        _bahuKanan: PoseLandmark?
    ) {

        val xmul = 3.3f
        val ymul = 3.3f


        val mataKiri = _mataKiri!!.position
        val mataKanan = _mataKanan!!.position
        val bahuKiri = _bahuKiri!!.position
        val bahuKanan = _bahuKanan!!.position


        val fineColloX =  mataKanan.x +  ((mataKiri.x - mataKanan.x) / 2)
        val fineColloY = mataKanan.y + ((mataKiri.y - mataKanan.y) / 2)

        val inizioColloX = bahuKanan.x + ((bahuKiri.x - bahuKanan.x ) / 2)
        val inizioColloY = bahuKanan.y + ((bahuKiri.y - bahuKanan.y) / 2)

        extraCanvas.drawLine(
            (fineColloX * xmul) - 250, fineColloY* ymul, (inizioColloX* xmul) -250, inizioColloY* ymul, paint
        )

        extraCanvas.drawLine(
            (mataKiri.x * xmul) - 250, mataKiri.y* ymul, (mataKanan.x* xmul) -250, mataKanan.y* ymul, paint
        )
        invalidate()
    }


}

class deteksi : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var imageCapture: ImageCapture? = null
    private var tts: TextToSpeech? = null

    private lateinit var cameraExecutor: ExecutorService
    private var lensFacing = CameraSelector.LENS_FACING_FRONT
    private var bKanan=true
    private var bKiri=true
    private var tKanan=true
    private var tKiri=true
    private var kKanan=true
    private var kKiri=true
    private val handler = Handler()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deteksi)
        val text= findViewById<TextView>(R.id.txtName)
        text.setText(intent.extras?.get("KEY_NAME").toString())
        val cameraSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        tts = TextToSpeech(this, this)

        // Request camera permissions
        if (allPermissionsGranted()) {
            Timer().schedule(10000){
                startCamera()
            }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraSwitch.setOnClickListener {

            if (lensFacing == CameraSelector.LENS_FACING_FRONT) lensFacing = CameraSelector.LENS_FACING_BACK
            else if (lensFacing == CameraSelector.LENS_FACING_BACK) lensFacing = CameraSelector.LENS_FACING_FRONT
            startCamera()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
                finish()
            }
        }
    }

    fun getAngle(firstPoint: PoseLandmark, midPoint: PoseLandmark, lastPoint: PoseLandmark): Double {



        var result = Math.toDegrees(
            atan2( lastPoint.getPosition().y.toDouble() - midPoint.getPosition().y,
            lastPoint.getPosition().x.toDouble() - midPoint.getPosition().x)
                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
            firstPoint.getPosition().x - midPoint.getPosition().x)
        )
        result = Math.abs(result) // Angle should never be negative
        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
    }

    fun getNeckAngle(
        ear: PoseLandmark, shoulder: PoseLandmark
    ): Double {

        var result = Math.toDegrees(
            atan2( shoulder.getPosition().y.toDouble() - shoulder.getPosition().y,
            (shoulder.getPosition().x + 100 ).toDouble() - shoulder.getPosition().x)
                - atan2(ear.getPosition().y - shoulder.getPosition().y,
            ear.getPosition().x - shoulder.getPosition().x)
        )

        result = Math.abs(result) // Angle should never be negative

        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onTextFound(pose: Pose)  {
        val namaPose = intent.extras?.get("KEY_NAME")
        Log.d("menerimadata",namaPose.toString())
        var headLine = Paint()
        headLine.color=Color.GREEN
        headLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        headLine.isDither = true
        headLine.style = Paint.Style.STROKE // default: FILL
        headLine.strokeJoin = Paint.Join.ROUND // default: MITER
        headLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        headLine.strokeWidth = 10f

        var lhandLine = Paint()
        lhandLine.color=Color.GREEN
        lhandLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        lhandLine.isDither = true
        lhandLine.style = Paint.Style.STROKE // default: FILL
        lhandLine.strokeJoin = Paint.Join.ROUND // default: MITER
        lhandLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        lhandLine.strokeWidth = 10f

        var rhandLine = Paint()
        rhandLine.color=Color.GREEN
        rhandLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        rhandLine.isDither = true
        rhandLine.style = Paint.Style.STROKE // default: FILL
        rhandLine.strokeJoin = Paint.Join.ROUND // default: MITER
        rhandLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        rhandLine.strokeWidth = 10f

        var lbodyLine = Paint()
        lbodyLine.color=Color.GREEN
        lbodyLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        lbodyLine.isDither = true
        lbodyLine.style = Paint.Style.STROKE // default: FILL
        lbodyLine.strokeJoin = Paint.Join.ROUND // default: MITER
        lbodyLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        lbodyLine.strokeWidth = 10f

        var rbodyLine = Paint()
        rbodyLine.color=Color.GREEN
        rbodyLine.color=Color.GREEN
        rbodyLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        rbodyLine.isDither = true
        rbodyLine.style = Paint.Style.STROKE // default: FILL
        rbodyLine.strokeJoin = Paint.Join.ROUND // default: MITER
        rbodyLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        rbodyLine.strokeWidth = 10f

        var lfootLine = Paint()
        lfootLine.color=Color.GREEN
        lfootLine.color=Color.GREEN
        lfootLine.color=Color.GREEN
        lfootLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        lfootLine.isDither = true
        lfootLine.style = Paint.Style.STROKE // default: FILL
        lfootLine.strokeJoin = Paint.Join.ROUND // default: MITER
        lfootLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        lfootLine.strokeWidth = 10f

        var rfootLine = Paint()
        rfootLine.color=Color.GREEN
        rfootLine.color=Color.GREEN
        rfootLine.color=Color.GREEN
        rfootLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        rfootLine.isDither = true
        rfootLine.style = Paint.Style.STROKE // default: FILL
        rfootLine.strokeJoin = Paint.Join.ROUND // default: MITER
        rfootLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        rfootLine.strokeWidth = 10f

        try {

            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
            val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
            val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
            val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
            val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
            val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
            val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
            val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
            val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
            val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)

            val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
            val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)

            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR) //telinga Kanan
            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)

            val builder = StringBuilder()
            rect_overlay.clear()

            // menggambar leher sebagai rata-rata antara mata dan telinga
            if( leftEye != null && rightEye != null && leftShoulder != null && rightShoulder != null  ){
                rect_overlay.drawNeck(leftEye, rightEye, leftShoulder, rightShoulder)
            }

            // menggambar leher yang terlihat menyamping dari kiri
            if(leftEar != null && leftShoulder != null){
                rect_overlay.drawLine(leftEar, leftShoulder,headLine)
                val sudutLeher = getNeckAngle(leftEar, leftShoulder)
            }

            // Menggambar leher yang terlihat menyamping dari kanan
            if(rightEar != null && rightShoulder != null){
                rect_overlay.drawLine(rightEar, rightShoulder, headLine)
                val sudutLeher = getNeckAngle(rightEar, rightShoulder)
            }

            // badan kanan
            if(rightShoulder != null && rightHip != null && rightKnee != null){
                val sudutBK = getAngle(rightShoulder, rightHip, rightKnee)
                Log.d("sudutbkk", sudutBK.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(sudutBK in 174.0..179.0){
                        Log.d("cekposebkk","benar")
                        rbodyLine.color=Color.GREEN
                        bKanan=true
                    }else{
                        Log.d("cekposebkk","salah")
                        rbodyLine.color=Color.RED
                        bKanan=false
                    }
                }else if(namaPose=="Pose Tree"){
                    if(sudutBK in 171.0..177.0){
                        Log.d("cekposebkk","benar")
                        rbodyLine.color=Color.GREEN
                        bKanan=true
                    }else{
                        Log.d("cekposebkk","salah")
                        rbodyLine.color=Color.RED
                        bKanan=false
                    }
                }else if(namaPose=="Pose Cobra"){
                    if(sudutBK in 126.0..133.0){
                        Log.d("cekposebkk","benar")
                        lbodyLine.color=Color.GREEN
                        bKiri=true
                    }else{
                        Log.d("cekposebkk","salah")
                        lbodyLine.color=Color.RED
                        bKiri=false
                    }
                }
            }

            // badan kiri
            if(leftShoulder != null && leftHip != null && leftKnee != null){
                val sudutBKR = getAngle(leftShoulder, leftHip, leftKnee)
                Log.d("sudutbkr", sudutBKR.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(sudutBKR in 171.0..175.0){
                        Log.d("cekposebkr","benar")
                        lbodyLine.color=Color.GREEN
                        bKiri=true
                    }else{
                        Log.d("cekposebkr","salah")
                        lbodyLine.color=Color.RED
                        bKiri=false
                    }
                }else if(namaPose=="Pose Tree"){
                    if(sudutBKR in 115.0..127.0){
                        Log.d("cekposebkr","benar")
                        lbodyLine.color=Color.GREEN
                        bKiri=true
                    }else{
                        Log.d("cekposebkr","salah")
                        lbodyLine.color=Color.RED
                        bKiri=false
                    }
                }else if(namaPose=="Pose Cobra"){
                    if(sudutBKR in 140.0..149.0){
                        Log.d("cekposebkr","benar")
                        lbodyLine.color=Color.GREEN
                        bKiri=true
                    }else{
                        Log.d("cekposebkr","salah")
                        lbodyLine.color=Color.RED
                        bKiri=false
                    }
                }
            }

            // pojok tangan kanan
            if(rightShoulder != null && rightElbow != null && rightWrist != null){
                val sudutKanan = getAngle(rightShoulder, rightElbow, rightWrist)
                Log.d("suduttangankanan", sudutKanan.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(sudutKanan in 86.0..93.0){
                        Log.d("cekposetk","benar")
                        rhandLine.color=Color.GREEN
                        tKanan=true
                    }else{
                        Log.d("cekposetk","salah")
                        rhandLine.color=Color.RED
                        tKanan=false
                    }
                }else if(namaPose=="Pose Tree"){
                    if(sudutKanan in 48.0..56.0){
                        Log.d("cekposetk","benar")
                        rhandLine.color=Color.GREEN
                        tKanan=true
                    }else{
                        Log.d("cekposetk","salah")
                        rhandLine.color=Color.RED
                        tKanan=false
                    }
                }else if(namaPose=="Pose Cobra"){
                    if(sudutKanan in 166.0..176.0){
                        Log.d("cekposetk","benar")
                        rhandLine.color=Color.GREEN
                        tKanan=true
                    }else{
                        Log.d("cekposetk","salah")
                        rhandLine.color=Color.RED
                        tKanan=false
                    }
                }
            }

            // pojok tangan kiri
            if(leftShoulder != null && leftElbow != null && leftWrist != null){
                val sudutKiri = getAngle(leftShoulder, leftElbow, leftWrist)
                Log.d("suduttangankiri", sudutKiri.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(sudutKiri in 79.0..85.0){
                        Log.d("cekposetr","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposetr","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }else if(namaPose=="Pose Tree"){
                    if(sudutKiri in 45.0..56.0){
                        Log.d("cekposetr","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposetr","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }else if(namaPose=="Pose Cobra"){
                    if(sudutKiri in 150.0..168.0){
                        Log.d("cekposetr","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposetr","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }
            }

            // sudut kaki kanan
            if( rightAnkle != null && rightKnee != null  && rightFootIndex != null){
                val kakiKanan = getAngle( rightKnee, rightAnkle, rightFootIndex)
                Log.d("sudutkakikanan", kakiKanan.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(kakiKanan in 97.0..105.0){
                        Log.d("cekposekk","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposekk","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }else if(namaPose=="Pose Tree"){
                    if(kakiKanan in 168.0..174.0){
                        Log.d("cekposekk","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposekk","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }else if(namaPose=="Pose Cobra"){
                    if(kakiKanan in 163.0..170.0){
                        Log.d("cekposekk","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposekk","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }
            }

            // sudut kaki kiri
            if( leftAnkle != null && leftKnee != null  && leftFootIndex != null){
                val kakiKiri = getAngle( leftKnee, leftAnkle, leftFootIndex)
                Log.d("sudutkakikiri", kakiKiri.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(kakiKiri in 102.0..108.0){
                        Log.d("cekposekr","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposekr","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }else if(namaPose=="Pose Tree"){
                    if(kakiKiri in 104.0..110.0){
                        Log.d("cekposekr","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposekr","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }else if(namaPose=="Pose Cobra"){
                    if(kakiKiri in 159.0..166.0){
                        Log.d("cekposekr","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposekr","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }
            }

            if(!tKanan || !tKiri){
                val text = "incorrect arm position"
                val delayMs = 4000
                handler.postDelayed(Runnable { tts!!.speak(text, TextToSpeech.QUEUE_ADD, null,"") },
                    delayMs.toLong()
                )
            }else if(!bKanan||!bKiri){
                val text = "incorrect body position"
                val delayMs = 4000
                handler.postDelayed(Runnable { tts!!.speak(text, TextToSpeech.QUEUE_ADD, null,"") },
                    delayMs.toLong()
                )
            }else if(!kKanan||!kKiri){
                val text = "incorrect foot position"
                val delayMs = 4000
                handler.postDelayed(Runnable { tts!!.speak(text, TextToSpeech.QUEUE_ADD, null,"") },
                    delayMs.toLong()
                )
            }else{
                tts!!.stop()
            }

            if(leftShoulder != null && rightShoulder != null){
                rect_overlay.drawLine(leftShoulder, rightShoulder, headLine)
            }

            if(leftHip != null &&  rightHip != null){
                rect_overlay.drawLine(leftHip, rightHip, headLine)
            }

            if(leftShoulder != null &&  leftElbow != null){
                rect_overlay.drawLine(leftShoulder, leftElbow,lhandLine)
            }

            if(leftElbow != null &&  leftWrist != null){
                rect_overlay.drawLine(leftElbow, leftWrist,lhandLine)
            }

            if(leftShoulder != null &&  leftHip != null){
                rect_overlay.drawLine(leftShoulder, leftHip,lbodyLine)
            }

            if(leftHip != null &&  leftKnee != null){
                rect_overlay.drawLine(leftHip, leftKnee,lbodyLine)
            }

            if(leftKnee != null &&  leftAnkle != null){
                rect_overlay.drawLine(leftKnee, leftAnkle,lfootLine)
            }

            if(leftWrist != null &&  leftThumb != null){
                rect_overlay.drawLine(leftWrist, leftThumb,headLine)
            }

            if(leftWrist != null &&  leftPinky != null){
                rect_overlay.drawLine(leftWrist, leftPinky,headLine)
            }

            if(leftWrist != null &&  leftIndex != null){
                rect_overlay.drawLine(leftWrist, leftIndex,headLine)
            }

            if(leftIndex != null &&  leftPinky != null){
                rect_overlay.drawLine(leftIndex, leftPinky,headLine)
            }

            if(leftAnkle != null &&  leftHeel != null){
                rect_overlay.drawLine(leftAnkle, leftHeel,lfootLine)
            }

            if(leftHeel != null &&  leftFootIndex != null){
                rect_overlay.drawLine(leftHeel, leftFootIndex,headLine)
            }

            if(rightShoulder != null &&  rightElbow != null){
                rect_overlay.drawLine(rightShoulder, rightElbow,rhandLine)
            }

            if(rightElbow != null &&  rightWrist != null){
                rect_overlay.drawLine(rightElbow, rightWrist,rhandLine)
            }

            if(rightShoulder != null &&  rightHip != null){
                rect_overlay.drawLine(rightShoulder, rightHip,rbodyLine)
            }

            if(rightHip != null &&  rightKnee != null){
                rect_overlay.drawLine(rightHip, rightKnee,rbodyLine)
            }

            if(rightKnee != null &&  rightAnkle != null){
                rect_overlay.drawLine(rightKnee, rightAnkle,rfootLine)
            }

            if(rightWrist != null &&  rightThumb != null){
                rect_overlay.drawLine(rightWrist, rightThumb,headLine)
            }

            if(rightWrist != null &&  rightPinky != null){
                rect_overlay.drawLine(rightWrist, rightPinky,headLine)
            }

            if(rightWrist != null &&  rightIndex != null){
                rect_overlay.drawLine(rightWrist, rightIndex,headLine)
            }

            if(rightIndex != null &&  rightPinky != null){
                rect_overlay.drawLine(rightIndex, rightPinky,headLine)
            }

            if(rightAnkle != null &&  rightHeel != null){
                rect_overlay.drawLine(rightAnkle, rightHeel,headLine)
            }

            if(rightHeel != null &&  rightFootIndex != null){
                rect_overlay.drawLine(rightHeel, rightFootIndex,headLine)
            }


        } catch (e: java.lang.Exception) {
            Toast.makeText(this@deteksi, "Errore", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        Log.d("kamera",lensFacing.toString())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            if(lensFacing==1){
                rect_overlay.scaleX=1f
                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor,PoseAnalyzer(::onTextFound))
                    }

                imageCapture = ImageCapture.Builder()
                    .build()

                val cameraSelector: CameraSelector
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture, imageAnalyzer)

                } catch(exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }
            }else{
                rect_overlay.scaleX=-1f
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor,PoseAnalyzer(::onTextFound))
                    }

                imageCapture = ImageCapture.Builder()
                    .build()

                val cameraSelector: CameraSelector
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture, imageAnalyzer)

                } catch(exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        tts!!.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("ttsresult","The Language not supported!")
            }
        }else{
            Log.e("ttsresult","Initilization Failed!")
        }
    }
}
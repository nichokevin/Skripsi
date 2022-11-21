package com.example.skripsi

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.QUEUE_ADD
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
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
import kotlin.math.atan2

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
        _bahuKanan: PoseLandmark?,
        paint:Paint
    ) {

        val xmul = 3.3f
        val ymul = 3.3f


        val mataKiri = _mataKiri!!.position
        val mataKanan = _mataKanan!!.position
        val bahuKiri = _bahuKiri!!.position
        val bahuKanan = _bahuKanan!!.position


        val endNeckX =  mataKanan.x +  ((mataKiri.x - mataKanan.x) / 2)
        val endNeckY = mataKanan.y + ((mataKiri.y - mataKanan.y) / 2)

        val startNeckX = bahuKanan.x + ((bahuKiri.x - bahuKanan.x ) / 2)
        val startNeckY = bahuKanan.y + ((bahuKiri.y - bahuKanan.y) / 2)

        extraCanvas.drawLine(
            (endNeckX * xmul) - 250, endNeckY* ymul, (startNeckX* xmul) -250, startNeckY* ymul, paint
        )

        extraCanvas.drawLine(
            (mataKiri.x * xmul) - 250, mataKiri.y* ymul, (mataKanan.x* xmul) -250, mataKanan.y* ymul, paint
        )
        invalidate()
    }


}

@Suppress("DEPRECATION")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deteksi)
        val text= findViewById<TextView>(R.id.txtName)
        text.text = intent.extras?.get("KEY_NAME").toString()
        val cameraSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        tts = TextToSpeech(this, this)

        startCamera()

        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraSwitch.setOnClickListener {

            if (lensFacing == CameraSelector.LENS_FACING_FRONT) lensFacing = CameraSelector.LENS_FACING_BACK
            else if (lensFacing == CameraSelector.LENS_FACING_BACK) lensFacing = CameraSelector.LENS_FACING_FRONT
            startCamera()
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

    private fun onTextFound(pose: Pose)  {
        val namaPose = intent.extras?.get("KEY_NAME")
        val arahPose = intent.extras?.get("arah")

        val headLine = Paint()
        headLine.color=Color.GREEN
        headLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        headLine.isDither = true
        headLine.style = Paint.Style.STROKE // default: FILL
        headLine.strokeJoin = Paint.Join.ROUND // default: MITER
        headLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        headLine.strokeWidth = 30f

        val lhandLine = Paint()
        lhandLine.color=Color.GREEN
        lhandLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        lhandLine.isDither = true
        lhandLine.style = Paint.Style.STROKE // default: FILL
        lhandLine.strokeJoin = Paint.Join.ROUND // default: MITER
        lhandLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        lhandLine.strokeWidth = 30f

        val rhandLine = Paint()
        rhandLine.color=Color.GREEN
        rhandLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        rhandLine.isDither = true
        rhandLine.style = Paint.Style.STROKE // default: FILL
        rhandLine.strokeJoin = Paint.Join.ROUND // default: MITER
        rhandLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        rhandLine.strokeWidth = 30f

        val lbodyLine = Paint()
        lbodyLine.color=Color.GREEN
        lbodyLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        lbodyLine.isDither = true
        lbodyLine.style = Paint.Style.STROKE // default: FILL
        lbodyLine.strokeJoin = Paint.Join.ROUND // default: MITER
        lbodyLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        lbodyLine.strokeWidth = 30f

        val rbodyLine = Paint()
        rbodyLine.color=Color.GREEN
        rbodyLine.color=Color.GREEN
        // Smooths out edges of what is drawn without affecting shape
        rbodyLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        rbodyLine.isDither = true
        rbodyLine.style = Paint.Style.STROKE // default: FILL
        rbodyLine.strokeJoin = Paint.Join.ROUND // default: MITER
        rbodyLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        rbodyLine.strokeWidth = 30f

        val lfootLine = Paint()
        lfootLine.color=Color.GREEN
        lfootLine.color=Color.GREEN
        lfootLine.color=Color.GREEN
        lfootLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        lfootLine.isDither = true
        lfootLine.style = Paint.Style.STROKE // default: FILL
        lfootLine.strokeJoin = Paint.Join.ROUND // default: MITER
        lfootLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        lfootLine.strokeWidth = 30f

        val rfootLine = Paint()
        rfootLine.color=Color.GREEN
        rfootLine.color=Color.GREEN
        rfootLine.color=Color.GREEN
        rfootLine.isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        rfootLine.isDither = true
        rfootLine.style = Paint.Style.STROKE // default: FILL
        rfootLine.strokeJoin = Paint.Join.ROUND // default: MITER
        rfootLine.strokeCap = Paint.Cap.ROUND // default: BUTT
        rfootLine.strokeWidth = 30f

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

            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)

            rect_overlay.clear()

            // menggambar leher sebagai rata-rata antara mata dan telinga
            if( leftEye != null && rightEye != null && leftShoulder != null && rightShoulder != null  ){
                rect_overlay.drawNeck(leftEye, rightEye, leftShoulder, rightShoulder,headLine)
            }

            // menggambar leher yang terlihat menyamping dari kiri
            if(leftEar != null && leftShoulder != null){
                rect_overlay.drawLine(leftEar, leftShoulder,headLine)
            }

            // Menggambar leher yang terlihat menyamping dari kanan
            if(rightEar != null && rightShoulder != null){
                rect_overlay.drawLine(rightEar, rightShoulder, headLine)
            }

            // badan kanan
            if(rightShoulder != null && rightHip != null && rightKnee != null){
                val sudutBK = getAngle(rightShoulder, rightHip, rightKnee)
                Log.d("sudutbkk", sudutBK.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(arahPose=="kanan"){
                        if(sudutBK in 160.0..185.0){
                            Log.d("cekposebkk","benar")
                            rbodyLine.color=Color.GREEN
                            bKanan=true
                        }else{
                            Log.d("cekposebkk","salah")
                            rbodyLine.color=Color.RED
                            bKanan=false
                        }
                    }else{
                        rbodyLine.color=Color.GREEN
                        bKanan=true
                    }
                }else if(namaPose=="Pose Tree"){
                    if(arahPose=="kanan"){
                        if(sudutBK in 115.0..127.0){
                            Log.d("cekposebkk","benar")
                            rbodyLine.color=Color.GREEN
                            bKanan=true
                        }else{
                            Log.d("cekposebkk","salah")
                            rbodyLine.color=Color.RED
                            bKanan=false
                        }
                    }else {
                        if(sudutBK in 165.0..180.0){
                            Log.d("cekposebkk","benar")
                            rbodyLine.color=Color.GREEN
                            bKanan=true
                        }else{
                            Log.d("cekposebkk","salah")
                            rbodyLine.color=Color.RED
                            bKanan=false
                        }
                    }

                }else if(namaPose=="Pose Cobra"){
                    if(arahPose=="kanan"){
                        if(sudutBK in 124.0..150.0){
                            Log.d("cekposebkk","benar")
                            lbodyLine.color=Color.GREEN
                            bKiri=true
                        }else{
                            Log.d("cekposebkk","salah")
                            lbodyLine.color=Color.RED
                            bKiri=false
                        }
                    }else {
                        lbodyLine.color=Color.GREEN
                        bKiri=true
                    }
                }
            }

            // badan kiri
            if(leftShoulder != null && leftHip != null && leftKnee != null){
                val sudutBKR = getAngle(leftShoulder, leftHip, leftKnee)
                Log.d("sudutbkr", sudutBKR.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(arahPose=="kiri"){
                        if(sudutBKR in 160.0..185.0){
                            Log.d("cekposebkr","benar")
                            lbodyLine.color=Color.GREEN
                            bKiri=true
                        }else{
                            Log.d("cekposebkr","salah")
                            lbodyLine.color=Color.RED
                            bKiri=false
                        }
                    }else {
                        lbodyLine.color=Color.GREEN
                        bKiri=true
                    }
                }else if(namaPose=="Pose Tree"){
                    if(arahPose=="kanan"){
                        if(sudutBKR in 165.0..180.0){
                            Log.d("cekposebkr","benar")
                            lbodyLine.color=Color.GREEN
                            bKiri=true
                        }else{
                            Log.d("cekposebkr","salah")
                            lbodyLine.color=Color.RED
                            bKiri=false
                        }
                    }else{
                        if(sudutBKR in 115.0..127.0){
                            Log.d("cekposebkr","benar")
                            lbodyLine.color=Color.GREEN
                            bKiri=true
                        }else{
                            Log.d("cekposebkr","salah")
                            lbodyLine.color=Color.RED
                            bKiri=false
                        }
                    }

                }else if(namaPose=="Pose Cobra"){
                    if(arahPose=="kiri"){
                        if(sudutBKR in 124.0..150.0){
                            Log.d("cekposebkr","benar")
                            lbodyLine.color=Color.GREEN
                            bKiri=true
                        }else{
                            Log.d("cekposebkr","salah")
                            lbodyLine.color=Color.RED
                            bKiri=false
                        }
                    }else {
                        lbodyLine.color=Color.GREEN
                        bKiri=true
                    }
                }
            }

            // pojok tangan kanan
            if(rightShoulder != null && rightElbow != null && rightWrist != null){
                val sudutKanan = getAngle(rightShoulder, rightElbow, rightWrist)
                Log.d("suduttangankanan", sudutKanan.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(arahPose=="kanan"){
                        if(sudutKanan in 70.0..95.0){
                            Log.d("cekposetk","benar")
                            rhandLine.color=Color.GREEN
                            tKanan=true
                        }else{
                            Log.d("cekposetk","salah")
                            rhandLine.color=Color.RED
                            tKanan=false
                        }
                    }else {
                        rhandLine.color=Color.GREEN
                        tKanan=true
                    }

                }else if(namaPose=="Pose Tree"){
                    if(sudutKanan in 45.0..65.0){
                        Log.d("cekposetk","benar")
                        rhandLine.color=Color.GREEN
                        tKanan=true
                    }else{
                        Log.d("cekposetk","salah")
                        rhandLine.color=Color.RED
                        tKanan=false
                    }
                }else if(namaPose=="Pose Cobra"){
                    if(arahPose=="kanan"){
                        if(sudutKanan in 125.0..160.0){
                            Log.d("cekposetk","benar")
                            rhandLine.color=Color.GREEN
                            tKanan=true
                        }else{
                            Log.d("cekposetk","salah")
                            rhandLine.color=Color.RED
                            tKanan=false
                        }
                    }else {
                        rhandLine.color=Color.GREEN
                        tKanan=true
                    }
                }
            }

            // pojok tangan kiri
            if(leftShoulder != null && leftElbow != null && leftWrist != null){
                val sudutKiri = getAngle(leftShoulder, leftElbow, leftWrist)
                Log.d("suduttangankiri", sudutKiri.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(arahPose=="kiri"){
                        if(sudutKiri in 70.0..100.0){
                            Log.d("cekposetr","benar")
                            lhandLine.color=Color.GREEN
                            tKiri=true
                        }else{
                            Log.d("cekposetr","salah")
                            lhandLine.color=Color.RED
                            tKiri=false
                        }
                    }else {
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }
                }else if(namaPose=="Pose Tree"){
                    if(sudutKiri in 45.0..65.0){
                        Log.d("cekposetr","benar")
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }else{
                        Log.d("cekposetr","salah")
                        lhandLine.color=Color.RED
                        tKiri=false
                    }
                }else if(namaPose=="Pose Cobra"){
                    if(arahPose=="kiri"){
                        if(sudutKiri in 125.0..160.0){
                            Log.d("cekposetr","benar")
                            lhandLine.color=Color.GREEN
                            tKiri=true
                        }else{
                            Log.d("cekposetr","salah")
                            lhandLine.color=Color.RED
                            tKiri=false
                        }
                    }else{
                        lhandLine.color=Color.GREEN
                        tKiri=true
                    }
                }
            }

            // sudut kaki kanan
            if( rightAnkle != null && rightKnee != null  && rightFootIndex != null){
                val kakiKanan = getAngle( rightKnee, rightAnkle, rightFootIndex)
                Log.d("sudutkakikanan", kakiKanan.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(arahPose=="kanan"){
                        if(kakiKanan in 70.0..135.0){
                            Log.d("cekposekk","benar")
                            rfootLine.color=Color.GREEN
                            kKanan=true
                        }else{
                            Log.d("cekposekk","salah")
                            rfootLine.color=Color.RED
                            kKanan=false
                        }
                    }else{
                        rfootLine.color=Color.GREEN
                        kKanan=true
                    }
                }else if(namaPose=="Pose Tree"){
                    rfootLine.color=Color.GREEN
                    kKanan=true

                }else if(namaPose=="Pose Cobra"){
                    if(arahPose=="kanan"){
                        if(kakiKanan in 145.0..170.0){
                            Log.d("cekposekk","benar")
                            rfootLine.color=Color.GREEN
                            kKanan=true
                        }else{
                            Log.d("cekposekk","salah")
                            rfootLine.color=Color.RED
                            kKanan=false
                        }
                    }else {
                        rfootLine.color=Color.GREEN
                        kKanan=true
                    }
                }
            }

            // sudut kaki kiri
            if( leftAnkle != null && leftKnee != null  && leftFootIndex != null){
                val kakiKiri = getAngle( leftKnee, leftAnkle, leftFootIndex)
                Log.d("sudutkakikiri", kakiKiri.toInt().toString())
                if (namaPose=="Pose Plank"){
                    if(arahPose=="kiri"){
                        if(kakiKiri in 70.0..135.0){
                            Log.d("cekposekr","benar")
                            lfootLine.color=Color.GREEN
                            kKiri=true
                        }else{
                            Log.d("cekposekr","salah")
                            lfootLine.color=Color.RED
                            kKiri=false
                        }
                    }else {
                        lfootLine.color=Color.GREEN
                        kKiri=true
                    }
                }else if(namaPose=="Pose Tree"){
                    lfootLine.color=Color.GREEN
                    kKiri=true

                }else if(namaPose=="Pose Cobra"){
                    if(arahPose=="kiri"){
                        if(kakiKiri in 145.0..170.0){
                            Log.d("cekposekr","benar")
                            lfootLine.color=Color.GREEN
                            kKiri=true
                        }else{
                            Log.d("cekposekr","salah")
                            lfootLine.color=Color.RED
                            kKiri=false
                        }
                    }else {
                        lfootLine.color=Color.GREEN
                        kKiri=true
                    }
                }
            }

            if(!tKanan || !tKiri){
                val text = "incorrect arm position"
                tts!!.playSilentUtterance(1000,QUEUE_ADD,null)
                tts!!.speak(text, QUEUE_ADD, null,"")
                tts!!.playSilentUtterance(2000,QUEUE_ADD,null)

            }else if(!bKanan||!bKiri){
                val text = "incorrect body position"
                tts!!.playSilentUtterance(1000,QUEUE_ADD,null)
                tts!!.speak(text, QUEUE_ADD, null,"")
                tts!!.playSilentUtterance(2000,QUEUE_ADD,null)

            }else if(!kKanan||!kKiri){
                val text = "incorrect foot position"
                tts!!.playSilentUtterance(1000,QUEUE_ADD,null)
                tts!!.speak(text, QUEUE_ADD, null,"")
                tts!!.playSilentUtterance(2000,QUEUE_ADD,null)

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
                rect_overlay.drawLine(leftHeel, leftFootIndex,lfootLine)
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
                rect_overlay.drawLine(rightAnkle, rightHeel,rfootLine)
            }

            if(rightHeel != null &&  rightFootIndex != null){
                rect_overlay.drawLine(rightHeel, rightFootIndex,rfootLine)
            }


        } catch (e: java.lang.Exception) {
            Toast.makeText(this@deteksi, "Errore", Toast.LENGTH_SHORT).show()
        }
    }
    
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
                    Log.e("cameraxre", "Use case binding failed", exc)
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
                    Log.e("cameraxre", "Use case binding failed", exc)
                }
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        tts!!.shutdown()
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
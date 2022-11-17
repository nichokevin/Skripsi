package com.example.skripsi;

import java.lang.System;

@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0018J\u0012\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J\b\u0010\u001f\u001a\u00020\u001cH\u0014J\u0010\u0010 \u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\u0010H\u0016J\u0010\u0010\"\u001a\u00020\u001c2\u0006\u0010#\u001a\u00020$H\u0002J\b\u0010%\u001a\u00020\u001cH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/example/skripsi/deteksi;", "Landroidx/appcompat/app/AppCompatActivity;", "Landroid/speech/tts/TextToSpeech$OnInitListener;", "()V", "bKanan", "", "bKiri", "cameraExecutor", "Ljava/util/concurrent/ExecutorService;", "handler", "Landroid/os/Handler;", "imageCapture", "Landroidx/camera/core/ImageCapture;", "kKanan", "kKiri", "lensFacing", "", "tKanan", "tKiri", "tts", "Landroid/speech/tts/TextToSpeech;", "getAngle", "", "firstPoint", "Lcom/google/mlkit/vision/pose/PoseLandmark;", "midPoint", "lastPoint", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onInit", "status", "onTextFound", "pose", "Lcom/google/mlkit/vision/pose/Pose;", "startCamera", "app_debug"})
public final class deteksi extends androidx.appcompat.app.AppCompatActivity implements android.speech.tts.TextToSpeech.OnInitListener {
    private androidx.camera.core.ImageCapture imageCapture;
    private android.speech.tts.TextToSpeech tts;
    private java.util.concurrent.ExecutorService cameraExecutor;
    private int lensFacing = androidx.camera.core.CameraSelector.LENS_FACING_FRONT;
    private boolean bKanan = true;
    private boolean bKiri = true;
    private boolean tKanan = true;
    private boolean tKiri = true;
    private boolean kKanan = true;
    private boolean kKiri = true;
    private final android.os.Handler handler = null;
    private java.util.HashMap _$_findViewCache;
    
    public deteksi() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public final double getAngle(@org.jetbrains.annotations.NotNull()
    com.google.mlkit.vision.pose.PoseLandmark firstPoint, @org.jetbrains.annotations.NotNull()
    com.google.mlkit.vision.pose.PoseLandmark midPoint, @org.jetbrains.annotations.NotNull()
    com.google.mlkit.vision.pose.PoseLandmark lastPoint) {
        return 0.0;
    }
    
    private final void onTextFound(com.google.mlkit.vision.pose.Pose pose) {
    }
    
    private final void startCamera() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @java.lang.Override()
    public void onInit(int status) {
    }
}
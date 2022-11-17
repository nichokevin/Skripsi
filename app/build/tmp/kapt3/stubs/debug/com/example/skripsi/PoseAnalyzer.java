package com.example.skripsi;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\rH\u0017R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/example/skripsi/PoseAnalyzer;", "Landroidx/camera/core/ImageAnalysis$Analyzer;", "poseFoundListener", "Lkotlin/Function1;", "Lcom/google/mlkit/vision/pose/Pose;", "", "(Lkotlin/jvm/functions/Function1;)V", "options", "Lcom/google/mlkit/vision/pose/accurate/AccuratePoseDetectorOptions;", "poseDetector", "Lcom/google/mlkit/vision/pose/PoseDetector;", "analyze", "imageProxy", "Landroidx/camera/core/ImageProxy;", "app_debug"})
final class PoseAnalyzer implements androidx.camera.core.ImageAnalysis.Analyzer {
    private final kotlin.jvm.functions.Function1<com.google.mlkit.vision.pose.Pose, kotlin.Unit> poseFoundListener = null;
    private final com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions options = null;
    private final com.google.mlkit.vision.pose.PoseDetector poseDetector = null;
    
    public PoseAnalyzer(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.google.mlkit.vision.pose.Pose, kotlin.Unit> poseFoundListener) {
        super();
    }
    
    @android.annotation.SuppressLint(value = {"UnsafeExperimentalUsageError", "UnsafeOptInUsageError"})
    @java.lang.Override()
    public void analyze(@org.jetbrains.annotations.NotNull()
    androidx.camera.core.ImageProxy imageProxy) {
    }
}
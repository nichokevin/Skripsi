package com.example.skripsi;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000b\u001a\u00020\fJ)\u0010\r\u001a\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0000\u00a2\u0006\u0002\b\u0013J=\u0010\u0014\u001a\u00020\f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0016\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0017\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0000\u00a2\u0006\u0002\b\u0019J\u0010\u0010\u001a\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\nH\u0014J(\u0010\u001c\u001a\u00020\f2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u001eH\u0014R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/example/skripsi/RectOverlay;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attributeSet", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "extraBitmap", "Landroid/graphics/Bitmap;", "extraCanvas", "Landroid/graphics/Canvas;", "clear", "", "drawLine", "startLandmark", "Lcom/google/mlkit/vision/pose/PoseLandmark;", "endLandmark", "paint", "Landroid/graphics/Paint;", "drawLine$app_debug", "drawNeck", "_mataKiri", "_mataKanan", "_bahuKiri", "_bahuKanan", "drawNeck$app_debug", "onDraw", "canvas", "onSizeChanged", "width", "", "height", "oldWidth", "oldHeight", "app_debug"})
public final class RectOverlay extends android.view.View {
    private android.graphics.Canvas extraCanvas;
    private android.graphics.Bitmap extraBitmap;
    private java.util.HashMap _$_findViewCache;
    
    public RectOverlay(@org.jetbrains.annotations.Nullable()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attributeSet) {
        super(null);
    }
    
    @java.lang.Override()
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    }
    
    @java.lang.Override()
    protected void onDraw(@org.jetbrains.annotations.NotNull()
    android.graphics.Canvas canvas) {
    }
    
    public final void clear() {
    }
    
    public final void drawLine$app_debug(@org.jetbrains.annotations.Nullable()
    com.google.mlkit.vision.pose.PoseLandmark startLandmark, @org.jetbrains.annotations.Nullable()
    com.google.mlkit.vision.pose.PoseLandmark endLandmark, @org.jetbrains.annotations.NotNull()
    android.graphics.Paint paint) {
    }
    
    public final void drawNeck$app_debug(@org.jetbrains.annotations.Nullable()
    com.google.mlkit.vision.pose.PoseLandmark _mataKiri, @org.jetbrains.annotations.Nullable()
    com.google.mlkit.vision.pose.PoseLandmark _mataKanan, @org.jetbrains.annotations.Nullable()
    com.google.mlkit.vision.pose.PoseLandmark _bahuKiri, @org.jetbrains.annotations.Nullable()
    com.google.mlkit.vision.pose.PoseLandmark _bahuKanan, @org.jetbrains.annotations.NotNull()
    android.graphics.Paint paint) {
    }
}
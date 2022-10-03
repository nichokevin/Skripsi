package com.example.skripsi.databaseLokal;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nJ\b\u0010\f\u001a\u0004\u0018\u00010\rJ\u0010\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000f\u001a\u00020\nJ\u0010\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J \u0010\u0013\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0016J\u0016\u0010\u0017\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n\u00a8\u0006\u0019"}, d2 = {"Lcom/example/skripsi/databaseLokal/DBHelper;", "Landroid/database/sqlite/SQLiteOpenHelper;", "context", "Landroid/content/Context;", "factory", "Landroid/database/sqlite/SQLiteDatabase$CursorFactory;", "(Landroid/content/Context;Landroid/database/sqlite/SQLiteDatabase$CursorFactory;)V", "addName", "", "name", "", "des", "getAll", "Landroid/database/Cursor;", "getByName", "data", "onCreate", "db", "Landroid/database/sqlite/SQLiteDatabase;", "onUpgrade", "p1", "", "p2", "replace", "Companion", "app_debug"})
public final class DBHelper extends android.database.sqlite.SQLiteOpenHelper {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.skripsi.databaseLokal.DBHelper.Companion Companion = null;
    private static final java.lang.String DATABASE_NAME = "yogaDatabase";
    private static final int DATABASE_VERSION = 1;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TABLE_NAME = "poseyoga";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String NAME_COl = "name";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DES_COL = "des";
    
    public DBHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.database.sqlite.SQLiteDatabase.CursorFactory factory) {
        super(null, null, null, 0);
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.NotNull()
    android.database.sqlite.SQLiteDatabase db) {
    }
    
    @java.lang.Override()
    public void onUpgrade(@org.jetbrains.annotations.NotNull()
    android.database.sqlite.SQLiteDatabase db, int p1, int p2) {
    }
    
    public final void addName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String des) {
    }
    
    public final void replace(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String des) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.database.Cursor getAll() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.database.Cursor getByName(@org.jetbrains.annotations.NotNull()
    java.lang.String data) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0014\u0010\f\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\t\u00a8\u0006\u000e"}, d2 = {"Lcom/example/skripsi/databaseLokal/DBHelper$Companion;", "", "()V", "DATABASE_NAME", "", "DATABASE_VERSION", "", "DES_COL", "getDES_COL", "()Ljava/lang/String;", "NAME_COl", "getNAME_COl", "TABLE_NAME", "getTABLE_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTABLE_NAME() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getNAME_COl() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDES_COL() {
            return null;
        }
    }
}
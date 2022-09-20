package com.example.skripsi.databaseLokal;

import java.lang.System;

@androidx.room.Database(entities = {com.example.skripsi.databaseLokal.Data.class}, version = 1)
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lcom/example/skripsi/databaseLokal/yogaDatabase;", "Landroidx/room/RoomDatabase;", "()V", "dataDAO", "Lcom/example/skripsi/databaseLokal/dataDAO;", "Companion", "app_debug"})
public abstract class yogaDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.skripsi.databaseLokal.yogaDatabase.Companion Companion = null;
    private static com.example.skripsi.databaseLokal.yogaDatabase INSTANCE;
    private static final java.lang.Object LOCK = null;
    
    public yogaDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.skripsi.databaseLokal.dataDAO dataDAO();
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0011\u0010\t\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0086\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/example/skripsi/databaseLokal/yogaDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/skripsi/databaseLokal/yogaDatabase;", "LOCK", "buildDatabase", "context", "Landroid/content/Context;", "invoke", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.skripsi.databaseLokal.yogaDatabase invoke(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        private final com.example.skripsi.databaseLokal.yogaDatabase buildDatabase(android.content.Context context) {
            return null;
        }
    }
}
package com.example.skripsi.databaseLokal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Data::class), version = 1)
abstract class yogaDatabase : RoomDatabase() {
    abstract fun dataDAO(): dataDAO

    companion object {
        private var INSTANCE: yogaDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context): yogaDatabase = Room.databaseBuilder(
            context.applicationContext,
            yogaDatabase::class.java,
            "dbYoga.db"
        ).build()
    }
}

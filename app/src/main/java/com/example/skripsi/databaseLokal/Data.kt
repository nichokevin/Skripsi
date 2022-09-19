package com.example.skripsi.databaseLokal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yoga")
data class Data(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val Nama : String,
    val Detail : String
)

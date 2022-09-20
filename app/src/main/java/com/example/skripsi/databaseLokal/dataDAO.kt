package com.example.skripsi.databaseLokal

import androidx.room.*

@Dao
interface dataDAO{
    /*@Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun tambahData(data: Data)

    @Query("SELECT * FROM yoga ORDER BY Nama ASC")
    suspend fun getAllData() : List<Data>

    @Delete
    suspend fun deleteData(data: Data)

    @Query("UPDATE yoga SET Nama=:yogaName, Detail=:detailYoga WHERE id=:Noid")
    suspend fun updateData(yogaName: String, detailYoga: String, Noid: Int)*/
}

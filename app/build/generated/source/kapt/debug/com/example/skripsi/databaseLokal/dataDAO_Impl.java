package com.example.skripsi.databaseLokal;

import androidx.room.RoomDatabase;
import java.lang.SuppressWarnings;

@SuppressWarnings({"unchecked", "deprecation"})
public final class dataDAO_Impl implements dataDAO {
  private final RoomDatabase __db;

  public dataDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
  }
}

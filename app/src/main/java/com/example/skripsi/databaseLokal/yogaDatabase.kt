package com.example.skripsi.databaseLokal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                NAME_COl + " TEXT PRIMARY KEY," +
                DES_COL + " TEXT" + "" +
                "UNIQUE($NAME_COl))")
        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addName(name : String, des : String ){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(NAME_COl, name)
        values.put(DES_COL, des)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)
        Log.d("add","fun add")
        // at last we are
        // closing our database
        db.close()
    }

    fun replace(name : String, des : String ){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(NAME_COl, name)
        values.put(DES_COL, des)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.replace(TABLE_NAME,null,values)
        // at last we are
        // closing our database
        Log.d("replace","fun replace")
        db.close()
    }

    // below method is to get
    // all data from our database
    fun getAll(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        val temp= db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        if (temp.moveToFirst()) {
            do {
                val test =temp.getColumnIndex(DBHelper.NAME_COl)
                val t = temp.getColumnIndex(DBHelper.DES_COL)
                Log.d("getAll Nama", temp.getString(test))
                Log.d("getAll des", temp.getString(t))
                // get the data into array, or class variable
            } while (temp.moveToNext());
        }
        temp.close();
        return temp

    }

    fun getByName(data: String): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        return db.rawQuery("select * from "+ TABLE_NAME+" where " + NAME_COl + "=?", arrayOf(data))
        /*try {
           db.rawQuery("select * from "+ TABLE_NAME+" where " + NAME_COl + "=?", arrayOf(data))
        }catch (e: SQLException) {
            return db.execSQL(e.message)
        }*/
        // below code returns a cursor to
        // read data from the database
    }

    companion object{
        private val DATABASE_NAME = "yogaDatabase"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "poseyoga"
        // below is the variable for name column
        val NAME_COl = "name"
        // below is the variable for age column
        val DES_COL = "des"
    }
}

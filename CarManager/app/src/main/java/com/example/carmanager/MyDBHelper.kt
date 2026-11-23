package com.example.carmanager

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(
    context: Context,
    name: String = DB_NAME,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = VERSION
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val DB_NAME = "usedCarDatabase" // 資料庫名稱
        private const val VERSION = 1                 // 資料庫版本
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 建立 carTable 資料表，包含 brand(主鍵), model, year, price 四個欄位
        db.execSQL(
            "CREATE TABLE carTable(" +
                    "brand text PRIMARY KEY, " +
                    "model text NOT NULL, " +
                    "year integer NOT NULL, " +
                    "price integer NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 升級資料庫版本時，刪除舊資料表並重建
        db.execSQL("DROP TABLE IF EXISTS carTable")
        onCreate(db)
    }
}
package com.example.administrator.hundreddays.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.administrator.hundreddays.base.BaseApplication
import com.example.administrator.hundreddays.constant.*
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context = BaseApplication.context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {

    companion object {
        private val TAG = "MyDatabaseOpenHelper"
        val instance: MyDatabaseOpenHelper by lazy { MyDatabaseOpenHelper(BaseApplication.context) }

    }

    override fun onCreate(db: SQLiteDatabase) {

        db.createTable(TABLE_PLAN, true,
                DB_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                DB_TITLE to TEXT,
                DB_REMIND to TEXT,
                DB_IMAGE_PATH to TEXT,
                DB_CREATE_DATE to TEXT,
                DB_FREQUENT_DAY to INTEGER,
                DB_TARGET_DAY to INTEGER)

        db.createTable("CREATE TABLE IF NOT EXISTS `$TABLE_SIGN`($DB_ID INTEGER , $DB_DATE TEXT, $DB_MESSAGE TEXT, PRIMARY KEY($DB_ID,$DB_DATE))")

        db.createTable(TABLE_ING, true,
                DB_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                DB_KEEP_DAY to INTEGER,
                DB_LAST_DATE to TEXT)

        db.createTable(TABLE_HISTORY, true,
                DB_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                DB_KEEP_DAY to INTEGER)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual

    }

    private fun SQLiteDatabase.createTable(sql:String) {
        execSQL(sql)
    }
}

package com.example.administrator.hundreddays.sqlite

import android.content.ContentValues
import android.util.Log
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.constant.*
import org.jetbrains.anko.db.select

class HistoryDao(val helper: MyDatabaseOpenHelper = MyDatabaseOpenHelper()) {

    private val TAG = "PlanDao"

    fun insert(history: History): Long {
        var code = -1L

        val cv = ContentValues()
        cv.put(DB_ID,history.plan?.ID)
        cv.put(DB_KEEP_DAY,history.keppDay)

        helper.use{
            code = insert(TABLE_HISTORY,null,cv)
        }

        return code
    }

    fun delete(id: Long?): Int {
        var count = 0

        helper.use {
            count = delete(TABLE_HISTORY,"$DB_ID= $id",null)
        }

        return count
    }

    fun alter(): ArrayList<History> {
        val sql = "select * from $TABLE_PLAN"
        var list = arrayListOf<History>()

        helper.use {

            val cursor = rawQuery(sql, null)
            if (cursor.moveToFirst()) {

                while (true) {
                    val plan = History(cursor.getLong(0))
                    plan.keppDay = cursor.getInt(1)

                    Log.i(TAG, plan.toString())

                    list.add(plan)

                    if (cursor.isLast)
                        break
                    cursor.moveToNext()
                }

                cursor.close()
            }
        }
        return list
    }

}
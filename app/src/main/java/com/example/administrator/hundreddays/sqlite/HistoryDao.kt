package com.example.administrator.hundreddays.sqlite

import android.content.ContentValues
import android.util.Log
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.constant.*

class HistoryDao(val helper: MyDatabaseOpenHelper = MyDatabaseOpenHelper()) {

    private val TAG = "HistoryDao"

    fun insert(history: History): Long {
        var code = -1L

        val cv = ContentValues()
        cv.put(DB_ID,history.plan?.ID)
        cv.put(DB_KEEP_DAY,history.keepDay)
        cv.put(DB_IS_FINISH,history.isFinish)

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

    fun alter(): MutableList<History> {
        val sql = "select * from $TABLE_HISTORY"
        var list = mutableListOf<History>()

        helper.use {

            val cursor = rawQuery(sql, null)
            if (cursor.moveToFirst()) {
                val planDao = PlanDao()
                while (true) {
                    val history = History(cursor.getLong(0))
                    history.keepDay = cursor.getInt(1)
                    history.isFinish = cursor.getInt(2)
                    history.plan = planDao.alterByID(history.id)

                    Log.i(TAG, history.toString())

                    list.add(history)

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
package com.example.administrator.hundreddays.sqlite

import android.content.ContentValues
import android.util.Log
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.constant.*

class SignDao(val helper: MyDatabaseOpenHelper = MyDatabaseOpenHelper()) {

    private val TAG = "PlanDao"

    fun insert(sign: Sign): Long {
        var code = -1L

        val cv = ContentValues()
        cv.put(DB_MESSAGE,sign.message)
        cv.put(DB_DATE,sign.date)
        cv.put(DB_ID,sign.id)

        helper.use{
            code = insert(TABLE_SIGN,null,cv)

        }

        return code
    }

    fun alter(): ArrayList<Sign> {
        val sql = "select * from $TABLE_SIGN"
        var list = arrayListOf<Sign>()

        helper.use {
            val cursor = rawQuery(sql, null)
            if (cursor.moveToFirst()) {

                while (true) {
                    val plan = Sign(cursor.getLong(0))
                    plan.message = cursor.getString(1)
                    plan.date = cursor.getString(2)

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


    fun alter(id:Long): MutableList<Sign> {
        var sum = 0
        var list = mutableListOf<Sign>()
        helper.use {
            val cursor = rawQuery("select * from $TABLE_SIGN where $DB_ID= $id", null)

            if (cursor.moveToFirst()) {

                while (true) {
                    val planIng = Sign(cursor.getLong(0))

                    planIng.date = cursor.getString(1)
                    planIng.message = cursor.getString(2)

                    planIng.plan = PlanDao().alterByID(planIng.id)

                    Log.i(TAG, planIng.toString())

                    list.add(planIng)

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
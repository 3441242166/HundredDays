package com.example.administrator.hundreddays.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.constant.*
import org.jetbrains.anko.db.*
import javax.security.auth.login.LoginException

class PlanDao(val helper: MyDatabaseOpenHelper = MyDatabaseOpenHelper()) {

    private val TAG = "PlanDao"

    fun insert(plan:Plan): Long {
        var code = -1L

        val cv = ContentValues()
        cv.put(DB_TARGET_DAY,plan.targetDay)
        cv.put(DB_REMIND,plan.remindTime)
        cv.put(DB_FREQUENT_DAY,plan.frequentDay)
        cv.put(DB_CREATE_DATE,plan.createDateTime)
        cv.put(DB_IMAGE_PATH,plan.imgUrl)
        cv.put(DB_TITLE,plan.title)

        helper.use{
            code = insert(TABLE_PLAN,null,cv)

        }

        return code
    }

    fun delete(id: Long?): Int {
        var count = 0

        helper.use {
            count = delete(TABLE_PLAN,"$DB_ID= $id",null)
        }

        return count
    }

    fun alterByID(ID:Long?): Plan {
        val sql = "select * from $TABLE_PLAN where $DB_ID = $ID"
        Log.i(TAG,sql)
        val plan = Plan()

        helper.use {
            val cursor = rawQuery(sql, null)
            cursor.moveToFirst()
            plan.ID = cursor.getLong(0)
            plan.title = cursor.getString(1)
            plan.remindTime = cursor.getString(2)
            plan.imgUrl = cursor.getString(3)
            plan.createDateTime = cursor.getString(4)
            plan.frequentDay = cursor.getInt(5)
            plan.targetDay = cursor.getInt(6)

            Log.i(TAG, plan.toString())

            cursor.close()
        }

        return plan
    }

    fun alter(): ArrayList<Plan> {
        val sql = "select * from $TABLE_PLAN"
        var list = arrayListOf<Plan>()

        helper.use {
            val cursor = rawQuery(sql, null)
            if (cursor.moveToFirst()) {

                while (true) {
                    val plan = Plan()
                    plan.ID = cursor.getLong(0)
                    plan.title = cursor.getString(1)
                    plan.remindTime = cursor.getString(2)
                    plan.imgUrl = cursor.getString(3)
                    plan.createDateTime = cursor.getString(4)
                    plan.frequentDay = cursor.getInt(5)
                    plan.targetDay = cursor.getInt(6)

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
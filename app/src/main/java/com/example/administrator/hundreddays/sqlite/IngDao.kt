package com.example.administrator.hundreddays.sqlite

import android.content.ContentValues
import android.util.Log
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.constant.*
import com.example.administrator.hundreddays.util.getNowDateString

class IngDao(private val helper: MyDatabaseOpenHelper = MyDatabaseOpenHelper()) {

    private val TAG = "IngDao"

    fun insert(planIng: PlanIng): Long {
        var code = -1L

        val cv = ContentValues()
        cv.put(DB_ID,planIng.id)
        cv.put(DB_KEEP_DAY,planIng.insistentDay)
        cv.put(DB_LAST_DATE,planIng.lastSignDay)

        helper.use{
            code = insert(TABLE_ING,null,cv)

        }

        return code
    }

    fun delete(id: Long?): Int {
        var count = 0

        helper.use {
            count = delete(TABLE_ING,"$DB_ID= $id",null)
        }

        return count
    }


    fun update(planIng: PlanIng){

        val cv = ContentValues()
        cv.put(DB_KEEP_DAY,planIng.insistentDay)
        cv.put(DB_LAST_DATE,planIng.lastSignDay)

        helper.use {
            update(TABLE_ING,cv,"$DB_ID=${planIng.plan?.ID}",null)
        }

    }


    fun alter(type:Int): MutableList<PlanIng> {
        val sql = "select * from $TABLE_ING"
        var list = mutableListOf<PlanIng>()

        helper.use {
            val cursor = rawQuery(sql, null)
            if (cursor.moveToFirst()) {

                while (true) {
                    val planIng = PlanIng(cursor.getLong(0))

                    planIng.insistentDay = cursor.getInt(1)
                    planIng.lastSignDay = cursor.getString(2)

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

        alterList(list,type)

        return list
    }

    private fun alterList(list:MutableList<PlanIng>, type: Int){
        when(type){
            //今日已签到
            1-> {
                var index = list.size-1
                while (index!=0){
                    if(list[index].lastSignDay == getNowDateString()){
                        list.removeAt(index)
                    }
                    index--
                }
            }
            //今日未签到
            2->{
                var index = list.size-1
                while (index>=0){
                    if(list[index].lastSignDay == getNowDateString()){
                        list.removeAt(index)
                    }
                    index--
                }
            }
            else->return
        }
    }


}
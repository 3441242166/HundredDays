package com.example.administrator.hundreddays.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BarBaseActivity
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.getDateString
import com.example.administrator.hundreddays.util.getNowString
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.find
import java.util.*

class SettingActivity : BarBaseActivity() {
    val TAG = "SettingActivity"


    override val contentView: Int
        get() = R.layout.activity_setting

    override fun init(savedInstanceState: Bundle?) {
        val map = mutableMapOf(getNowString(DATETYPE.DATE_DATE) to 1)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,1)

        val sum = calendar.get(Calendar.DAY_OF_YEAR)
        Log.i(TAG," sum = $sum")

        for( num in 0 until sum){
            val temp = Math.abs(Random().nextInt()%10)
            Log.i(TAG," date = ${getDateString(calendar.time,DATETYPE.DATE_DATE)}   第${calendar.get(Calendar.WEEK_OF_MONTH)}周")
            map[getDateString(calendar.time,DATETYPE.DATE_DATE)] = temp
            calendar.add(Calendar.DAY_OF_MONTH,1)
        }

        ac_setting_statistic.setData(map)


    }

}


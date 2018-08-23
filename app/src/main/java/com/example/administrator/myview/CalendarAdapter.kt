package com.example.administrator.myview

import android.graphics.Color
import android.graphics.DashPathEffect
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.textColor
import java.util.*

class CalendarAdapter(data: ArrayList<CalendarBean>?,val nowCalendar: Calendar) : BaseQuickAdapter<CalendarBean, BaseViewHolder>(R.layout.item_calendar, data) {
    private val TAG = "CalendarAdapter"

    private val now = Calendar.getInstance()

    override fun convert(helper: BaseViewHolder, item: CalendarBean) {
        val startTime = System.currentTimeMillis()
        val calendar = item.calendar
        helper.setText(R.id.item_calendar_day, calendar.get(Calendar.DAY_OF_MONTH).toString())

        helper.adapterPosition

        if(item.isSign){
            helper.getView<ConstraintLayout>(R.id.item_calendar_bck).setBackgroundColor(Color.rgb(103, 182, 94))
        }else{
            helper.getView<ConstraintLayout>(R.id.item_calendar_bck).setBackgroundColor(Color.rgb(233, 233, 233))
        }

        if(nowCalendar.get(Calendar.YEAR)==calendar.get(Calendar.YEAR) && nowCalendar.get(Calendar.MONTH)==calendar.get(Calendar.MONTH)){
            helper.getView<TextView>(R.id.item_calendar_day).setTextColor(android.graphics.Color.rgb(0, 0, 0))
        }

        if(now.get(Calendar.YEAR)==calendar.get(Calendar.YEAR) &&now.get(Calendar.DAY_OF_YEAR)==calendar.get(Calendar.DAY_OF_YEAR)) {
            helper.getView<CircleImageView>(R.id.item_calendar_now).visibility = View.VISIBLE
        }

        val endTime = System.currentTimeMillis()
        Log.i(TAG,"time = ${endTime - startTime}")
    }

}

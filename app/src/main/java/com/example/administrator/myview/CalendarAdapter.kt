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

class CalendarAdapter(data: ArrayList<CalendarBean>?) : BaseQuickAdapter<CalendarBean, BaseViewHolder>(R.layout.item_calendar, data) {
    private val TAG = "CalendarAdapter"

    override fun convert(helper: BaseViewHolder, item: CalendarBean) {
        helper.setText(R.id.item_calendar_day, item.date.date.toString())
        val date = item.date

        if(item.isSign){
            helper.getView<ConstraintLayout>(R.id.item_calendar_bck).setBackgroundColor(Color.rgb(103, 182, 94))
        }else{
            helper.getView<ConstraintLayout>(R.id.item_calendar_bck).setBackgroundColor(Color.rgb(233, 233, 233))
        }

        val now =  Date()
        if( now.year == date.year){
            if(now.month == date.month){
                if(now.date == date.date){
                    helper.getView<CircleImageView>(R.id.item_calendar_now).visibility = View.VISIBLE
                }
            }else{
                helper.getView<TextView>(R.id.item_calendar_day).setTextColor(Color.rgb(199, 199, 199))
            }
        }

    }
}

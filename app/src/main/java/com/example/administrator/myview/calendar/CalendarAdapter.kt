package com.example.administrator.myview.calendar


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.bean.Sign
import java.util.*

class CalendarAdapter(data: ArrayList<Calendar>?, val signMap: Map<String, Sign>) : BaseQuickAdapter<Calendar, BaseViewHolder>(R.layout.item_calendar,data) {
    private val TAG = "CalendarAdapter"

    override fun convert(helper: BaseViewHolder, item: Calendar) {
        val view: CalendarView = helper.getView(R.id.item_calendar_calendar)
        view.setData(signMap)
        view.setCalendar(item)

        helper.addOnClickListener(R.id.item_calendar_calendar)
    }


}
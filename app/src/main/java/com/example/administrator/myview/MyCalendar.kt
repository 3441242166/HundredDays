package com.example.administrator.myview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.getDateString
import com.example.administrator.hundreddays.util.getNowString
import org.jetbrains.anko.find
import java.util.*



class MyCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val TAG = "MyCalendar"

    lateinit var imgBack:ImageView
    lateinit var imgNext:ImageView
    lateinit var title:TextView
    lateinit var recycler:RecyclerView
    lateinit var message:TextView

    lateinit var signMap: MutableMap<String, Sign>

    private val curDate:Calendar = Calendar.getInstance()

    init {
        bindView()
        bindEvent()
        //renderCalendar()
    }

    private fun bindEvent() {

        imgBack.setOnClickListener {
            curDate.add(Calendar.MONTH,-1)
            renderCalendar()
            title.text = getDateString(curDate.time,DATETYPE.DATE_MONTH)

        }

        imgNext.setOnClickListener {
            curDate.add(Calendar.MONTH,1)
            renderCalendar()
            title.text = getDateString(curDate.time,DATETYPE.DATE_MONTH)
        }

    }

    private fun renderCalendar() {
        val startTime = System.currentTimeMillis()
        //------------------------------------------------------------------------------------------
        val max = 6*7

        val cells = arrayListOf<CalendarBean>()
        val calendar = curDate.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH,1)
        val prevDays = calendar.get(Calendar.DAY_OF_WEEK)-1
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays)

        while (cells.size <max){
            if(signMap[getDateString(calendar.time,DATETYPE.DATE_DATE)] != null){
                cells.add(CalendarBean(calendar.clone() as Calendar,true))
            }else{
                cells.add(CalendarBean(calendar.clone() as Calendar,false))
            }
            calendar.add(Calendar.DAY_OF_MONTH,1)
        }
        //------------------------------------------------------------------------------------------
        val adapter = CalendarAdapter(cells,curDate)
        recycler.layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)
        recycler.isNestedScrollingEnabled = false

        adapter.setOnItemClickListener { adapter, view, position ->
            Log.i(TAG,"onclick $position view ${view::javaClass}")
            val date = adapter.data[position] as CalendarBean
            if(signMap[getDateString(date.calendar.time,DATETYPE.DATE_DATE)] == null){
                message.text = getDateString(date.calendar.time,DATETYPE.DATE_DATE)+"\n"+"今天没有签到哦"
            }else{
                message.text = getDateString(date.calendar.time,DATETYPE.DATE_DATE)+"\n"+signMap[getDateString(date.calendar.time,DATETYPE.DATE_DATE)]?.message!!
            }
            //view.setBackgroundColor(Color.rgb(255, 64, 129))
        }
        //------------------------------------------------------------------------------------------
        val endTime = System.currentTimeMillis()
        Log.i(TAG,"time = ${endTime - startTime}")
    }

    private fun bindView() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_calendar,this)

        imgBack = find(R.id.view_calendar_back)
        imgNext = find(R.id.view_calendar_next)
        title = find(R.id.view_calendar_date)
        recycler = find(R.id.view_calendar_recycler)
        message = find(R.id.view_calendar_message)

        title.text = getDateString(curDate.time,DATETYPE.DATE_MONTH)


    }

    fun setData(signMap: MutableMap<String, Sign>){
        this.signMap = signMap
        renderCalendar()
        if(signMap[getDateString(curDate.time,DATETYPE.DATE_DATE)] == null){
            message.text = getDateString(curDate.time,DATETYPE.DATE_DATE)+"\n"+"今天没有签到哦"
        }else{
            message.text = getDateString(curDate.time,DATETYPE.DATE_DATE)+"\n"+signMap[getDateString(curDate.time,DATETYPE.DATE_DATE)]?.message!!
        }
    }
}
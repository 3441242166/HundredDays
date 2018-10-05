package com.example.administrator.myview.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.util.*
import org.jetbrains.anko.find
import java.util.*

class PlanCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val TAG = "PlanCalendar"

    lateinit var imgBack: ImageView
    lateinit var imgNext: ImageView
    lateinit var title: TextView
    lateinit var recycler: RecyclerView
    lateinit var message: TextView

    lateinit var signMap: MutableMap<String, Sign>
    val data: ArrayList<Calendar> = arrayListOf()

    private var scrollHelper: PagingScrollHelper = PagingScrollHelper()
    private var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    private var position = 0

    private lateinit var selectListener: OnDateSelectListener


    init {
        bindView()
        bindEvent()
    }

    private fun bindView() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_calendar,this)

        imgBack = find(R.id.view_calendar_back)
        imgNext = find(R.id.view_calendar_next)
        title = find(R.id.view_calendar_date)
        recycler = find(R.id.view_calendar_recycler)
        message = find(R.id.view_calendar_message)

        title.text = getDateString(getNowString(DATETYPE.DATE_MONTH))
    }

    @SuppressLint("SetTextI18n")
    private fun renderCalendar() {

        recycler.layoutManager = layoutManager
        LinearSnapHelper().attachToRecyclerView(recycler)
        scrollHelper.setUpRecycleView(recycler)

        val adapter = CalendarAdapter(data, signMap)
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)
        recycler.isNestedScrollingEnabled = false

        adapter.setOnItemChildClickListener { adapter, view, position ->
            Log.i(TAG,"tag = ${view.tag}")
            val date = view.tag as String
            val sign = signMap[date]
            selectListener.onDateSelect(date)
            if (sign != null) {
                if(sign.message == "") {
                    message.text = "$date\n 今天没有记东西哦"
                }else{
                    message.text = "$date\n ${sign.message}"
                }
            }else{
                message.text =  "$date\n 今天没有签到哦"
            }
        }

        val cur = Calendar.getInstance().time
        val date = getDateString(cur,DATETYPE.DATE_DATE)
        title.text = getDateString(cur,DATETYPE.DATE_MONTH)
        val sign = signMap[getDateString(cur,DATETYPE.DATE_DATE)]
        if (sign != null) {
            if(sign.message == "") {
                message.text = "$date\n 今天没有记东西哦"
            }else{
                message.text = "$date\n ${sign.message}"
            }
        }else{
            message.text =  "$date\n 今天没有签到哦"
        }
    }

    private fun bindEvent() {
        scrollHelper.setOnPageChangeListener(object : PagingScrollHelper.onPageChangeListener {
            override fun onPageChange(index: Int) {
                position = index
                title.text = getDateString(data[index].time,DATETYPE.DATE_MONTH)
            }
        })

        imgBack.setOnClickListener {

        }

        imgNext.setOnClickListener {

        }

    }

    fun setData(signMap: MutableMap<String, Sign>){
        this.signMap = signMap
        initCalendarList()
        if(data.size==0){
            data.add(Calendar.getInstance())
        }
        renderCalendar()
    }

    /**
     *  获取计划月份间隔
     */
    private fun initCalendarList() {
        val startStr = StringBuilder()
        val endStr = StringBuilder()

        if(signMap.isEmpty()){
            return
        }

        for((key,value) in signMap){
            Log.i(TAG,"key = $key")
            if(startStr.isEmpty() || differentDay(key,startStr.toString()) < 0){
                startStr.replace(0,startStr.length,key)
            }
            if(endStr.isEmpty() || differentDay(endStr.toString(),key) < 0){
                endStr.replace(0,endStr.length,key)
            }
        }

        val startList = startStr.split("-")
        val startYear = startList[0].toInt()
        val startMonth = startList[1].toInt()

        val endList = endStr.split("-")
        val endYear = endList[0].toInt()
        val endMonth = endList[1].toInt()

        Log.i(TAG,"startStr = $startStr  endStr = $endStr")

        val dif = (endYear - startYear) * 12 + endMonth - startMonth

        Log.i(TAG,"startYear $startYear startMonth $startMonth endYear $endYear endMonth $endMonth")
        val calendar = Calendar.getInstance()
        calendar.time = getDateByDateString(startStr.toString())

        for( i in 0..dif){
            Log.i(TAG,"$i")
            data.add(calendar.clone() as Calendar)
            calendar.add(Calendar.MONTH,1)
        }

    }

    interface OnDateSelectListener{
        fun onDateSelect(date:String)
    }

    fun setOnDateSelectListener(selectListener: OnDateSelectListener){
        this.selectListener = selectListener
    }

}
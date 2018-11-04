package com.example.administrator.myview.statisticsview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.util.*
import org.jetbrains.anko.find
import java.util.*

class StatisticLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val TAG = "StatisticLayout"

    lateinit var imgBack: ImageView
    lateinit var imgNext: ImageView
    lateinit var txTitle: TextView
    lateinit var txState: TextView
    lateinit var txSecond: TextView
    lateinit var imgState: ImageView
    lateinit var statistic: StatisticView



    val dataMap = HashMap<String, List<Int>>()
    val nowCalendar = Calendar.getInstance()

    init {
        bindView()
        initEvent()
    }

    private fun bindView() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_statistic,this)

        imgBack = find(R.id.view_statistic_back)
        imgNext = find(R.id.view_statistic_next)
        txTitle = find(R.id.view_statistic_date)
        txState = find(R.id.view_statistic_statetx)
        imgState = find(R.id.view_statistic_state)
        statistic = find(R.id.view_statistic_statistic)
        txSecond = find(R.id.view_statistic_second)

    }

    private fun initEvent(){
        imgBack.setOnClickListener {
            val tempCalendar = nowCalendar.clone() as Calendar

            when(statistic.getState()){
                StatisticView.STATISTIC_STATE.WEEK_DAY -> tempCalendar.add(Calendar.WEEK_OF_MONTH,-1)
                else -> {
                    tempCalendar.add(Calendar.MONTH,-1)
                }
            }
            if(dataMap[getDateString(tempCalendar.time,DATETYPE.DATE_DATE)]!=null) {
                statistic.befor()
            }else{
                Toast.makeText(context,"没有数据啦",Toast.LENGTH_SHORT).show()
            }
        }

        imgNext.setOnClickListener {
            val tempCalendar = nowCalendar.clone() as Calendar

            when(statistic.getState()){
                StatisticView.STATISTIC_STATE.WEEK_DAY -> tempCalendar.add(Calendar.WEEK_OF_MONTH,1)
                else -> {
                    tempCalendar.add(Calendar.MONTH,1)
                }
            }

            if(dataMap[getDateString(tempCalendar.time,DATETYPE.DATE_DATE)]!=null) {
                statistic.next()
            }else{
                Toast.makeText(context,"没有数据啦",Toast.LENGTH_SHORT).show()
            }
        }

        imgState.setOnClickListener {
            statistic.changeState()
        }

        statistic.setOnLongClickListener {
            statistic.changeStyle()
            true }

        statistic.setOnTitleChangeListener(object : StatisticView.TitleChangeListener{
            override fun onTitleChangeListener(title: String) {
                txSecond.visibility = View.GONE
                txTitle.text = title
            }
        })

        statistic.setOnStateChangeListener(object : StatisticView.StateChangeListener{
            override fun onStateChangeListener(title: String) {
                txSecond.visibility = View.GONE
                txState.text = title
            }

        })

        statistic.setOnSeccendTitleChangeListener(object : StatisticView.SecondTitleChangeListener{
            override fun onSecondChangeListener(title: String) {
                txSecond.visibility = View.VISIBLE
                txSecond.text = title
            }


        })
    }

    fun setData(signMap: Map<String, Int>){
        initCalendarList(signMap)
        if(dataMap.size==0){

            dataMap
        }
        statistic.setData(dataMap, nowCalendar)
    }

    private fun initCalendarList(signMap: Map<String, Int>){
        val startStr = StringBuilder()
        val endStr = StringBuilder()

        if(signMap.isEmpty()){
            return
        }

        for((key,value) in signMap){
            Log.i(TAG,"key = $key  value = $value")

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

        //有多少月份
        val dif = (endYear - startYear) * 12 + (endMonth - startMonth)
        //设置开始月份
        val calendar = Calendar.getInstance()
        calendar.time = getDateByDateString(startStr.toString())
        Log.i(TAG,"dif = $dif")
        for( i in 0 .. dif){

            val list:MutableList<Int> = mutableListOf()
            calendar.set(Calendar.DAY_OF_MONTH,1)
            calendar.roll(Calendar.DAY_OF_MONTH,-1)
            //获取该月份的天数
            val monthSum = calendar.get(Calendar.DAY_OF_MONTH)

            calendar.set(Calendar.DAY_OF_MONTH,1)
            var date:String
            Log.i(TAG,"当月一共有 $monthSum  天")
            for( j in 0 until monthSum){
                // 如果map当前日期不为空
                date = getDateString(calendar.time, DATETYPE.DATE_DATE)

                if(signMap[date] != null){
                    list.add(signMap[date]!!)
                }else{
                    list.add(0)
                }

                dataMap[date] = list
                calendar.add(Calendar.DAY_OF_MONTH,1)
            }
        }

    }
}
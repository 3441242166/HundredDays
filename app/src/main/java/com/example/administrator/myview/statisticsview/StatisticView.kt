package com.example.administrator.myview.statisticsview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.administrator.hundreddays.util.onMeasureName
import java.util.*
import android.graphics.Shader
import android.graphics.LinearGradient
import android.graphics.CornerPathEffect
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.getDateString


class StatisticView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val TAG = "StatisticView"
    //画笔
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val decorationPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val stripPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    //宽高属性
    private var width:Float = 0f
    private var height:Float = 0f
    private var widthMode:Int = 0
    private var heightMode:Int = 0
    private var maxHeight:Float = 0f
    //视图状态
    private var state = STATISTIC_STATE.MONTH_DAY
    private var style = STATISTIC_STYLE.STRIP

    enum class STATISTIC_STATE{
        MONTH_DAY,
        MONTH_WEEK,
        WEEK_DAY
    }

    enum class STATISTIC_STYLE{
        LINE,
        STRIP
    }

    private var isNormal = true

    init {
        initPaint()
    }

    private fun initPaint(){
        textPaint.color = Color.BLACK        //设置画笔颜色
        textPaint.style = Paint.Style.FILL   // 设置样式
        textPaint.textAlign = Paint.Align.CENTER

        decorationPaint.color = Color.parseColor("#9f9f9f")        //设置画笔颜色
        decorationPaint.style = Paint.Style.FILL  //设置画笔模式为填充
        decorationPaint.isAntiAlias = true

        stripPaint.color = Color.parseColor("#FFEF5F5F")     //设置画笔颜色
        stripPaint.style = Paint.Style.FILL  //设置画笔模式为填充
        stripPaint.isAntiAlias = true

        val shader = LinearGradient(100f, 100f, 500f, 500f, Color.parseColor("#E91E63"),
                Color.parseColor("#2196F3"), Shader.TileMode.CLAMP)
        linePaint.shader = shader
        val pathEffect = CornerPathEffect(20f)
        linePaint.pathEffect = pathEffect
        linePaint.style = Paint.Style.STROKE
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = 4F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize =  MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize =  MeasureSpec.getSize(heightMeasureSpec)

        Log.i(TAG,"widthMode = ${onMeasureName(widthMode)}  widthSize = $widthSize  heightMode = ${onMeasureName(heightMode)}  heightSize = $heightSize")

        heightSize = (widthSize/9*5)
        widthSize = widthSize / 7 * itemSum

        width = widthSize.toFloat()
        height = heightSize.toFloat()
        this.widthMode = widthMode
        this.heightMode = heightMode

        //分五格大小
        itemHeight = height / 5
        maxHeight = itemHeight * 3
        //字体大小
        textPaint.textSize = itemHeight / 5

        setMeasuredDimension(
                resolveSize(widthSize, widthMeasureSpec),
                resolveSize(heightSize, heightMeasureSpec)
        )

    }

    private var itemSum = 20        //item总数
    private var itemWidth = 0f      //item的宽度
    private var itemHeight = 0f
    private var interval = 0f       //item的间隔
    private var startX = 0f         //绘制坐标

    override fun onDraw(canvas: Canvas?) {
        if(canvas  == null){
            return
        }

        Log.i(TAG,"width = ${width}  height = ${height}  maxHeight = $maxHeight")
        Log.i(TAG,"itemWidth = ${itemWidth}  itemHeight = ${itemHeight}  maxHeight = $maxHeight")
        Log.i(TAG,"max = ${max}")

        canvas.save()
        canvas.translate(0f, height/5*4)

        when (state){
            StatisticView.STATISTIC_STATE.MONTH_DAY -> stateMonthDay(canvas)
            StatisticView.STATISTIC_STATE.MONTH_WEEK -> stateMonthWeek(canvas)
            StatisticView.STATISTIC_STATE.WEEK_DAY -> stateWeekDay(canvas)
        }
        //中线
        canvas.drawLine(3f,-itemHeight*3/2 ,width-3f,-itemHeight*3/2 ,decorationPaint)
        //底线
        canvas.drawLine(0f,0f,width,0f,decorationPaint)

        canvas.restore()
    }

    private fun stateMonthDay(canvas: Canvas){

        var data = dataMap!![getDateString(curCalendar!!.time,DATETYPE.DATE_DATE)]

        if(data == null){
            data = listOf()
        }

        when(style){
            StatisticView.STATISTIC_STYLE.STRIP -> typeStrip(data,canvas)
            StatisticView.STATISTIC_STYLE.LINE -> typeLine(data,canvas)
        }
    }

    private fun stateMonthWeek(canvas: Canvas) {
        val nowData = dataMap!![getDateString(curCalendar!!.time,DATETYPE.DATE_DATE)]
        curCalendar!!.add(Calendar.MONTH,1)
        val nextData = dataMap!![getDateString(curCalendar!!.time,DATETYPE.DATE_DATE)]
        curCalendar!!.add(Calendar.MONTH,-1)

        val monthData = mutableListOf<Int>()
        val tempCalendar = curCalendar!!.clone() as Calendar

        //获取该月天数
        tempCalendar.set(Calendar.DAY_OF_MONTH,1)
        tempCalendar.roll(Calendar.DAY_OF_MONTH,-1)
        var sumDays =  tempCalendar.get(Calendar.DAY_OF_MONTH)
        tempCalendar.set(Calendar.DAY_OF_MONTH,1)
        Log.i(TAG,"sumDays = $sumDays")

        //获取该月第一天为一星期的第几天
        val prevDays = tempCalendar.get(Calendar.DAY_OF_WEEK)-1
        Log.i(TAG,"prevDays = $prevDays")

        //从第一个完整的星期开始
        tempCalendar.add(Calendar.DAY_OF_MONTH, 7-prevDays)
        Log.i(TAG,"第一个完整的星期 = ${getDateString(tempCalendar.time,DATETYPE.DATE_DATE)}")

        //计算需要遍历多少天
        val startIndex = (7-prevDays)%7
        sumDays = if(startIndex + 28 > sumDays) 28 else 35

        Log.i(TAG,"startIndex = $startIndex    sumDays = $sumDays")
        //统计每周累计数目
        var sum = 0
        for( index in startIndex until sumDays){
            if(index<nowData!!.size){
                sum += nowData[index]
            }else{
                if(nextData != null){
                    sum += nextData[index % nowData.size]
                }
            }

            if(index%7==0){
                monthData.add(sum)
                sum = 0
            }
            tempCalendar.add(Calendar.DAY_OF_MONTH,1)
        }

        when(style){
            StatisticView.STATISTIC_STYLE.STRIP -> typeStrip(monthData,canvas)
            StatisticView.STATISTIC_STYLE.LINE -> typeLine(monthData,canvas)
        }

    }

    private fun stateWeekDay(canvas: Canvas) {
        val nowData = dataMap!![getDateString(curCalendar!!.time,DATETYPE.DATE_DATE)]
        curCalendar!!.add(Calendar.MONTH,1)
        val nextData = dataMap!![getDateString(curCalendar!!.time,DATETYPE.DATE_DATE)]
        curCalendar!!.add(Calendar.MONTH,-1)
        val weekData = mutableListOf<Int>()
        val tempCalendar = curCalendar!!.clone() as Calendar

        //获取该月天数
        tempCalendar.set(Calendar.DAY_OF_MONTH,1)
        tempCalendar.roll(Calendar.DAY_OF_MONTH,-1)
        val sumDays =  tempCalendar.get(Calendar.DAY_OF_MONTH)
        tempCalendar.set(Calendar.DAY_OF_MONTH,1)
        Log.i(TAG,"sumDays = $sumDays")

        //获取该月第一天为一星期的第几天
        val prevDays = tempCalendar.get(Calendar.DAY_OF_WEEK)-1
        Log.i(TAG,"prevDays = $prevDays")

        //获取目前为该月的第几周
        val index = curCalendar!!.get(Calendar.WEEK_OF_MONTH)-1
        Log.i(TAG,"该月的第 $index 周 ")

        //从第一个完整的星期开始
        tempCalendar.add(Calendar.DAY_OF_MONTH, 7-prevDays)
        Log.i(TAG,"第一个完整的星期 = ${getDateString(tempCalendar.time,DATETYPE.DATE_DATE)}")

        //计算需要遍历多少天
        val startIndex =  (7-prevDays)%7

        for(num in (index*7+startIndex) until (index*7+startIndex+7)){
            if(num<nowData!!.size){
                weekData.add(nowData[num])
            }else{
                if(nextData != null){
                    weekData.add(nextData[index % nowData.size])
                }
            }
        }

        when(style){
            StatisticView.STATISTIC_STYLE.STRIP -> typeStrip(weekData,canvas)
            StatisticView.STATISTIC_STYLE.LINE -> typeLine(weekData,canvas)
        }

    }

    private fun typeStrip(data:List<Int>, canvas: Canvas){
        max = data.max()!!
        itemSum = data.size
        interval = width/itemSum
        itemWidth = interval
        startX =itemWidth/2

        Log.i(TAG,"itemSum = ${itemSum}  interval = ${interval}  data = ${data.size}")

        for(num in data.indices){
            canvas.drawRect(startX - itemWidth/4,0f,
                    startX + itemWidth/4, -maxHeight / max * data[num] ,stripPaint)

            canvas.drawText("${data[num]}", startX,- maxHeight/max * data[num] - textPaint.textSize *3/2,textPaint)

            canvas.drawText("${num+1}", startX,textPaint.textSize * 3/2,textPaint)

            startX += interval

        }

    }

    private fun typeLine(data:List<Int>,canvas: Canvas){
        max = data.max()!!
        itemSum = data.size
        interval = width/itemSum
        itemWidth = interval
        startX = interval/2
        Log.i(TAG,"itemSum = ${itemSum}  interval = ${interval}  data = ${data.size}")
        val path = Path()
        path.moveTo(startX,-maxHeight/max * data[0])

        for( num in data.indices){
            canvas.drawText("${data[num]}",
                    startX, -maxHeight/max * data[num] - textPaint.textSize *3/2,textPaint)

            path.lineTo(startX, -maxHeight/max * data[num])

            canvas.drawText("${num+1}",
                    startX, textPaint.textSize * 3/2,textPaint)

            startX += interval
        }

        canvas.drawPath(path,linePaint)
    }

    private var dataMap:HashMap<String, List<Int>>? = null
    private var curCalendar: Calendar? = null
    private var max = 0

    fun setData(map:HashMap<String, List<Int>>?, calendar: Calendar){
        dataMap = map
        curCalendar = calendar
        invalidate()
    }

    fun changeStyle() {
        style = if(style == STATISTIC_STYLE.LINE){
            STATISTIC_STYLE.STRIP
        }else{
            STATISTIC_STYLE.LINE
        }
        invalidate()
    }

    fun changeState(){
        state = when(state){
            StatisticView.STATISTIC_STATE.MONTH_DAY -> {
                stateChangeListener?.onStateChangeListener("Month/Week")
                STATISTIC_STATE.MONTH_WEEK
            }
            StatisticView.STATISTIC_STATE.MONTH_WEEK -> {
                stateChangeListener?.onStateChangeListener("Week/Day")
                STATISTIC_STATE.WEEK_DAY

            }
            StatisticView.STATISTIC_STATE.WEEK_DAY -> {
                stateChangeListener?.onStateChangeListener("Month/Day")
                //设置为一个月的第一个完整星期的第一天
                curCalendar!!.set(Calendar.DAY_OF_MONTH,1)
                STATISTIC_STATE.MONTH_DAY
            }
        }
        invalidate()
    }

    fun next(){
        when(state){
            StatisticView.STATISTIC_STATE.MONTH_DAY -> {
                curCalendar!!.add(Calendar.MONTH,1)
                titleChangeListener?.onTitleChangeListener(getDateString(curCalendar!!.time,DATETYPE.DATE_MONTH))
            }
            StatisticView.STATISTIC_STATE.MONTH_WEEK -> {
                curCalendar!!.add(Calendar.MONTH,1)
                titleChangeListener?.onTitleChangeListener(getDateString(curCalendar!!.time,DATETYPE.DATE_MONTH))
            }
            StatisticView.STATISTIC_STATE.WEEK_DAY -> {
                val index = curCalendar!!.get(Calendar.WEEK_OF_MONTH)
                titleChangeListener?.onTitleChangeListener(getDateString(curCalendar!!.time,DATETYPE.DATE_MONTH)+"-第${index}周")
                secondTitleChangeListener?.onSecondChangeListener("5.21-5.28")
                curCalendar!!.add(Calendar.WEEK_OF_MONTH,1)
            }
        }
        invalidate()
    }

    fun befor(){
        when(state){
            StatisticView.STATISTIC_STATE.MONTH_DAY -> {
                curCalendar!!.add(Calendar.MONTH,-1)
                titleChangeListener?.onTitleChangeListener(getDateString(curCalendar!!.time,DATETYPE.DATE_MONTH))
            }
            StatisticView.STATISTIC_STATE.MONTH_WEEK -> {
                curCalendar!!.add(Calendar.MONTH,-1)
                titleChangeListener?.onTitleChangeListener(getDateString(curCalendar!!.time,DATETYPE.DATE_MONTH))
            }
            StatisticView.STATISTIC_STATE.WEEK_DAY -> {
                val index = curCalendar!!.get(Calendar.WEEK_OF_MONTH)
                titleChangeListener?.onTitleChangeListener(getDateString(curCalendar!!.time,DATETYPE.DATE_MONTH)+"-第${index}周")
                secondTitleChangeListener?.onSecondChangeListener("5.21-5.28")
                curCalendar!!.add(Calendar.WEEK_OF_MONTH,-1)
            }
        }
        invalidate()
    }

    fun getState(): STATISTIC_STATE {
        return state
    }

    private var titleChangeListener:TitleChangeListener? = null

    private var stateChangeListener:StateChangeListener? = null

    private var secondTitleChangeListener:SecondTitleChangeListener? = null

    fun setOnTitleChangeListener(listener:TitleChangeListener){
        this.titleChangeListener = listener
    }

    fun setOnStateChangeListener(listener:StateChangeListener){
        this.stateChangeListener = listener
    }

    fun setOnSeccendTitleChangeListener(listener:SecondTitleChangeListener){
        this.secondTitleChangeListener = listener
    }

    interface SecondTitleChangeListener{
        fun onSecondChangeListener(title:String)
    }

    interface TitleChangeListener{
        fun onTitleChangeListener(title:String)
    }

    interface StateChangeListener{
        fun onStateChangeListener(title:String)
    }
}
package com.example.administrator.myview.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.getDateString
import java.util.*

class CalendarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr)  {
    private val TAG = "CalendarView"

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val specialTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val bckPaint = Paint()
    private val specialBckPaint = Paint()
    private val pointPaint = Paint()

    private var itemWidth = 0f
    private var itemHeight = 0f

    //此View所表现的日期的Calendar
    private var curCalendar:Calendar = Calendar.getInstance()
    //当前的日期的Calendar
    private var nowCalendar:Calendar = Calendar.getInstance()

    lateinit var signMap: Map<String, Sign>

    init {
        initPain()
    }

    private fun initPain() {
        textPaint.color = Color.BLACK        //设置画笔颜色
        textPaint.style = Paint.Style.FILL   // 设置样式
        //textPaint.strokeWidth = 1f
        textPaint.textSize = 24F             // 设置字体大小
        textPaint.textAlign = Paint.Align.CENTER

        specialTextPaint.color = Color.rgb(199, 199, 199)       //设置画笔颜色
        specialTextPaint.style = Paint.Style.FILL   // 设置样式
        specialTextPaint.textSize = 24F             // 设置字体大小
        specialTextPaint.textAlign = Paint.Align.CENTER

        bckPaint.color = Color.rgb(233, 233, 233)       //设置画笔颜色
        bckPaint.style = Paint.Style.FILL  //设置画笔模式为填充

        specialBckPaint.color = Color.rgb(103, 182, 94)       //设置画笔颜色
        specialBckPaint.style = Paint.Style.FILL  //设置画笔模式为填充

        pointPaint.color = Color.argb(50,164, 164, 164)
        pointPaint.style = Paint.Style.FILL
        pointPaint.isAntiAlias = true
        this.isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize =  MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize =  MeasureSpec.getSize(heightMeasureSpec)
        Log.i(TAG,"widthMode = $widthMode widthSize = $widthSize  heightMode = $heightMode  heightSize = $heightSize")

        itemWidth = widthSize /7f
        itemHeight = itemWidth

        setMeasuredDimension(
                resolveSize(widthSize, widthMeasureSpec),
                resolveSize((itemHeight * 6).toInt(), heightMeasureSpec)
        )



    }

    override fun onDraw(canvas: Canvas?) {

        if (canvas == null) {
            return
        }

        canvas.drawColor(Color.WHITE)

        val calendar = curCalendar.clone() as Calendar

        calendar.set(Calendar.DAY_OF_MONTH,1)
        val prevDays = calendar.get(Calendar.DAY_OF_WEEK)-1
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays)

        var posX = itemWidth / 2
        var posY = itemWidth / 2

        for( pos in 1..42){
            // 绘制背景
            if(signMap[getDateString(calendar.time,DATETYPE.DATE_DATE)] != null){
                canvas.drawRect(posX - itemWidth / 2 + 1,posY- itemWidth / 2 + 1,posX + itemWidth / 2 - 1,posY + itemWidth / 2 - 1,specialBckPaint)
            }else{
                canvas.drawRect(posX - itemWidth / 2 + 1,posY- itemWidth / 2 + 1,posX + itemWidth / 2 - 1,posY + itemWidth / 2 - 1,bckPaint)
            }
            // 绘制文字
            if(curCalendar.get(Calendar.YEAR)==calendar.get(Calendar.YEAR) && curCalendar.get(Calendar.MONTH)==calendar.get(Calendar.MONTH)){
                canvas.drawText(calendar.get(Calendar.DAY_OF_MONTH).toString(), posX, posY,textPaint)
            }else{
                canvas.drawText(calendar.get(Calendar.DAY_OF_MONTH).toString(), posX, posY,specialTextPaint)
            }
            if(nowCalendar.get(Calendar.YEAR)==calendar.get(Calendar.YEAR) && nowCalendar.get(Calendar.MONTH)==calendar.get(Calendar.MONTH) && nowCalendar.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH)){
                canvas.drawCircle(posX,posY,36f,pointPaint)
            }
            //canvas.drawRoundRect(posX-itemHeight/4,posY+itemWidth / 3-2,posX+itemHeight/4,posY+itemWidth / 3+2,2f,2f,pointPaint)

            if(pos % 7 == 0){
                posX = itemHeight/2
                posY += itemHeight
            }else{
                posX += itemHeight
            }

            calendar.add(Calendar.DAY_OF_MONTH,1)
        }

    }

    private var downPos = 0
    private var upPos = 0
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG,"ACTION_DOWN")
                downPos = (event.x / itemWidth + 1).toInt() + ( event.y /itemHeight).toInt() * 7
                Log.i(TAG,"downPos = $downPos")
            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG,"ACTION_UP")
                upPos = (event.x / itemWidth + 1).toInt() + ( event.y /itemHeight).toInt() * 7
                Log.i(TAG,"upPos = $upPos")

                this.tag = getDateByPosition(upPos)
            }
        }

        return super.onTouchEvent(event)
    }

    fun setData(signMap: Map<String, Sign>){
        this.signMap = signMap
    }

    fun setCalendar(curCalendar:Calendar){
        this.curCalendar = curCalendar
    }

    /**
     *  通过位置获取日期
     */
    private fun getDateByPosition(pos:Int):String{
        val temp = (curCalendar.clone() as Calendar)
        temp.set(Calendar.DAY_OF_MONTH,1)
        val prevDays = temp.get(Calendar.DAY_OF_WEEK)-1
        temp.add(Calendar.DAY_OF_MONTH, -prevDays)
        temp.add(Calendar.DAY_OF_MONTH,pos -1)

        return getDateString(temp.time,DATETYPE.DATE_DATE)
    }

}


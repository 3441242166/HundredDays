package com.example.administrator.myview.statisticsview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.administrator.hundreddays.util.onMeasureName
import kotlin.math.max

class StatisticView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val TAG = "StatisticView"

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val decorationPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val stripPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var width:Float = 0f
    private var height:Float = 0f
    private var phoneWidth:Int = 0

    private var itemLength:Float = 0f

    private var state:Int = 0

    private var monthAr = arrayOf<Int>()
    private var weekAr = arrayOf<Int>()

    init {
        val dm = resources.displayMetrics
        phoneWidth = dm.widthPixels

        textPaint.color = Color.BLACK        //设置画笔颜色
        textPaint.style = Paint.Style.FILL   // 设置样式
        textPaint.textAlign = Paint.Align.CENTER

        decorationPaint.color = Color.parseColor("#9f9f9f")        //设置画笔颜色
        decorationPaint.style = Paint.Style.FILL  //设置画笔模式为填充
        decorationPaint.isAntiAlias = true

        stripPaint.color = Color.parseColor("#FFEF5F5F")     //设置画笔颜色
        stripPaint.style = Paint.Style.FILL  //设置画笔模式为填充
        stripPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize =  MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize =  MeasureSpec.getSize(heightMeasureSpec)

        Log.i(TAG,"widthMode = ${onMeasureName(widthMode)}  widthSize = $widthSize  heightMode = ${onMeasureName(heightMode)}  heightSize = $heightSize")

        heightSize = (widthSize/9*4)
        widthSize = widthSize / 7 * itemSum

        width = widthSize.toFloat()
        height = heightSize.toFloat()

        itemLength = height/4
        textPaint.textSize = itemLength / 5

        setMeasuredDimension(
                resolveSize(widthSize, widthMeasureSpec),
                resolveSize(heightSize, heightMeasureSpec)
        )

    }


    override fun onDraw(canvas: Canvas?) {
        if(canvas  == null){
            return
        }
        typeMonthDay(canvas)
        when (state){

        }

        canvas.drawLine(3f,itemLength*3/2,width-3f,itemLength*3/2,decorationPaint)
        canvas.drawLine(3f,itemLength*3,width-3f,itemLength*3,decorationPaint)
    }
    /*--------------------------------------------------------------------------------------------*/
    var itemSum = 30    //item总数
    var itemWidth = 0f      //item的宽度
    var interval = 0f       //item的间隔
    var itemHeight = 0f     //item的最大长度
    var startX = 0f
    var startY = 0f
    var maxTime = 0

    private fun typeWeek(canvas: Canvas){
        itemSum = 7
    }

    private fun typeMonthDay(canvas: Canvas){
        itemSum = 30
        itemWidth = width/itemSum/2
        interval = width/itemSum - itemWidth
        startX = width/itemSum/4
        startY = height/4*3
        itemHeight = height/3

        var num = 0
        while(num<itemSum){
            canvas.drawRect(startX,itemHeight-startY
                    ,startX+itemWidth,startY,stripPaint)
            canvas.drawText("${num+1}", startX+itemWidth/2, startY + textPaint.textSize * 3/2,textPaint)

            startX += interval + itemWidth
            num++
        }

    }

    private fun typeMonthWeek(canvas: Canvas){
        itemSum = 7
    }

    fun setData(ar:Array<Int>){
        var index = 0
        while(index < ar.size){
            monthAr[index] = ar[index]
            if(maxTime<ar[index]){
                maxTime = ar[index]
            }
            index++
        }
    }

    fun nextState(){
        state = ++state % 3
        invalidate()
    }
}
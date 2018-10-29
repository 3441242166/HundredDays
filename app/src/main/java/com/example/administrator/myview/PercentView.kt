package com.example.administrator.myview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.administrator.hundreddays.util.onMeasureName
import java.util.*

class PercentView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val TAG = "PercentView"

    private val bckPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val externalPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val presentTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val smallPresentTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val normalTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var circleLength:Float = 0f
    private var circleSmallLength:Float = 0f

    private var centerPresentX:Float = 0f
    private var centerPresentY:Float = 0f

    private var centerInsistX:Float = 0f
    private var centerInsistY:Float = 0f

    private var centerOtherX:Float = 0f
    private var centerOtherY:Float = 0f

    private var width:Float = 0f
    private var height:Float = 0f

    private var present:String = ""
    private var title:String = ""
    private var left:String = ""
    private var right:String = ""
    private var endAngle:Float  = -90f

    init {
        bckPaint.color = Color.rgb(233, 233, 233)       //设置画笔颜色
        bckPaint.style = Paint.Style.FILL  //设置画笔模式为填充
        bckPaint.isAntiAlias = true

        externalPaint.color = Color.rgb(50, 123, 226)       //设置画笔颜色
        externalPaint.style = Paint.Style.FILL  //设置画笔模式为填充
        externalPaint.isAntiAlias = true

        imagePaint.color = Color.rgb(233, 100, 100)       //设置画笔颜色
        imagePaint.style = Paint.Style.FILL  //设置画笔模式为填充
        imagePaint.isAntiAlias = true

        presentTextPaint.color = Color.WHITE        //设置画笔颜色
        presentTextPaint.style = Paint.Style.FILL   // 设置样式
        presentTextPaint.textAlign = Paint.Align.CENTER

        normalTextPaint.color = Color.rgb(115, 115, 115)        //设置画笔颜色
        normalTextPaint.style = Paint.Style.FILL   // 设置样式
        normalTextPaint.textAlign = Paint.Align.CENTER

        smallPresentTextPaint.color = Color.WHITE        //设置画笔颜色
        smallPresentTextPaint.style = Paint.Style.FILL   // 设置样式
        smallPresentTextPaint.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize =  MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize =  MeasureSpec.getSize(heightMeasureSpec)
        Log.i(TAG,"widthMode = ${onMeasureName(widthMode)}  widthSize = $widthSize  heightMode = ${onMeasureName(heightMode)}  heightSize = $heightSize")


        if (widthMode != MeasureSpec.UNSPECIFIED) {
            heightSize = (widthSize*1.4).toInt()
        }

        if (heightMode != MeasureSpec.UNSPECIFIED) {
            widthSize = (heightSize/1.4).toInt()
        }

        circleLength = widthSize* 0.3.toFloat()
        circleSmallLength = circleLength /2

        centerPresentX = (widthSize/2).toFloat()
        centerPresentY = (heightSize/3).toFloat()

        centerInsistX = (widthSize/4*1).toFloat()
        centerInsistY = (heightSize/6*5).toFloat()

        centerOtherX = (widthSize/4*3).toFloat()
        centerOtherY = (heightSize/6*5).toFloat()

        width = widthSize.toFloat()
        height = heightSize.toFloat()


        presentTextPaint.textSize = circleLength/2
        normalTextPaint.textSize = circleLength/3
        smallPresentTextPaint.textSize = circleSmallLength/2

        setMeasuredDimension(
                resolveSize(widthSize, widthMeasureSpec),
                resolveSize(heightSize, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        if(canvas  == null){
            return
        }
        canvas.drawRoundRect(0f, 0f, width, height, 15f, 15f, bckPaint)
        canvas.drawArc(centerPresentX-circleLength-10,centerPresentY-circleLength-10
                ,centerPresentX+circleLength+10,centerPresentY+circleLength+10,
                -90f,endAngle,true,externalPaint)
        canvas.drawCircle(centerPresentX,centerPresentY,circleLength,imagePaint)
        canvas.drawCircle(centerInsistX,centerInsistY,circleSmallLength,imagePaint)
        canvas.drawCircle(centerOtherX,centerOtherY,circleSmallLength,imagePaint)

        canvas.drawText(present, centerPresentX, centerPresentY+presentTextPaint.textSize/5*2,presentTextPaint)
        canvas.drawText(left, centerInsistX, centerInsistY+smallPresentTextPaint.textSize/5*2,smallPresentTextPaint)
        canvas.drawText(right, centerOtherX, centerOtherY+smallPresentTextPaint.textSize/5*2,smallPresentTextPaint)
        canvas.drawText(title, centerPresentX, (centerPresentY+circleLength*1.2+normalTextPaint.textSize).toFloat(),normalTextPaint)
    }


    fun setMainProcess(num:Float){
        endAngle = num/100*360
        present = "${num.toInt()}%"
    }
    fun setMainText(str:String){
        title = str
    }

    fun setLeftText(str:String){
        left = str
    }

    fun setRightText(str:String){
        right = str
    }

}
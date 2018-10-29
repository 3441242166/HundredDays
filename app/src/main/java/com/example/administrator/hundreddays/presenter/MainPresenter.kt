package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.constant.*
import com.example.administrator.hundreddays.util.*
import com.example.administrator.hundreddays.view.*
import io.realm.Realm
import java.util.*


class MainPresenter(val view: MainView, private val context: Context,val realm: Realm? = Realm.getDefaultInstance()) {
    private val TAG = "MainPresenter"

    lateinit var planList:MutableList<Plan>

    private var position = 0
    // 指向图片所在位置
    private var pre = 0
    private var now = 1
    private var next = 2

    fun initData(){
        var sum: Int

        realm?.beginTransaction()
        //------------------------------------------------------------------------------------------
        val userList = realm?.where(Plan::class.java)
                ?.equalTo("planState", PLAN_ING)
                ?.findAll()
        planList = userList!!.toMutableList()

        sum = checkInvalid()

        if(planList.size == 0){
            view.setVisibility(false)
        }else {
            view.setVisibility(true)
            view.setData(planList)
            view.setBackground(now,getBlurPath(planList[position].imgPath))
            view.setMessage(now, planList[position].title)
            if(planList.size>position+1){
                view.setBackground(next,getBlurPath(planList[position+1].imgPath))
                view.setMessage(next, planList[position+1].title)
            }

            changeIndex(position,true)
        }
        //------------------------------------------------------------------------------------------
        realm?.commitTransaction()
        if(sum > 0){
            view.setMessageDialog("你有$sum 个任务过期了,快去回收站看看吧")
        }
    }

    fun sign(message:String) {
        val temp = planList[position]
        Log.i(TAG,"${temp.lastSignDate} .... ${getNowString(DATETYPE.DATE_DATE)}")
        if(temp.lastSignDate == getNowString(DATETYPE.DATE_DATE)){
            view.setMessageDialog("已经签到过了哦")
            return
        }

        realm?.beginTransaction()
        //------------------------------------------------------------------------------------------
        if(temp.signDays+1 == temp.targetTimes){
            temp.planState = PLAN_COMPLETE
        }
        if(temp.lastSignDate != getNowString(DATETYPE.DATE_DATE)) {
            temp.signDays ++
        }
        temp.lastSignDate = getNowString(DATETYPE.DATE_DATE)
        var sign = realm?.where(Sign::class.java)
                ?.endsWith("planId",temp.id)
                ?.findFirst()
        if (sign == null){
            sign = realm?.createObject(Sign::class.java,UUID.randomUUID().toString())
            if (sign == null){
                Log.i(TAG,"sing error")
                return
            }
            sign.message = message
            sign.date = getNowString(DATETYPE.DATE_DATE)
            sign.planId = temp.id!!
            sign.plan = temp
        }else{
            sign.message = message
        }
        //------------------------------------------------------------------------------------------
        realm?.commitTransaction()

        view.signSuccess(position)
    }

    /**
     * 当滑动切换 item 时
     */
    fun changeIndex(index:Int,isUpdate:Boolean = false){
        Log.i(TAG,"before pre = $pre  now = $now  next = $next")
        if(position == index && !isUpdate){
            return
        }
        if(index > position){
            val temp = now
            now = next
            next = pre
            pre = temp

            if(index<planList.size-1) {
                view.setMessage(next, planList[index+1].title)
                view.setBackground(next, getBlurPath(planList[index+1].imgPath))
            }
        }else if(index < position){
            val temp = now
            now = pre
            pre = next
            next = temp

            if(index>0) {
                view.setMessage(pre, planList[index-1].title)
                view.setBackground(pre, getBlurPath(planList[index-1].imgPath))
            }
        }
        position = index
        view.setIndex("${position+1}/${planList.size}")

        //----------------------------------weakHashMap---------------------------------------------
        if(bitmapMap[planList[position].id] == null) {
            bitmapMap.clear()
            Glide.with(context)
                    .asBitmap()
                    .load(planList[position].imgPath)
                    .into(bitmapTarget)
        }

        if(blurBitmapMap[planList[position].id] == null) {
            blurBitmapMap.clear()
            Glide.with(context)
                    .asBitmap()
                    .load(getMessageBlurPath(planList[position].imgPath))
                    .into(blurBitmapTarget)
        }
       // ------------------------------------------------------------------------------------------

        //Log.i(TAG,"after pre = $pre  now = $now  next = $next")
    }

    private val bitmapTarget =object :  SimpleTarget<Bitmap>(getValueFromSharedPreferences(SCREEN_WIDTH)!!.toInt()/2,getValueFromSharedPreferences(SCREEN_HEIGHT)!!.toInt()/2){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            Log.i(TAG,"getBitmap")
            bitmapMap[planList[position].id!!] = resource
        }
    }
    private val blurBitmapTarget =object :  SimpleTarget<Bitmap>(getValueFromSharedPreferences(SCREEN_WIDTH)!!.toInt()/2,getValueFromSharedPreferences(SCREEN_HEIGHT)!!.toInt()/2){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            Log.i(TAG,"getBlurBitmap")
            blurBitmapMap[planList[position].id!!] = resource
        }
    }

    fun changeAlpha(value:Float){

        if(value>0){
            view.setViewAlpha(now,255-value)
            view.setViewAlpha(next,value)
            val alpha = 255 - 2*value
            if(alpha>0) {
                view.setTextViewAlpha(now, alpha)
                view.setTextViewAlpha(next, 0f)
            }else{
                view.setTextViewAlpha(now, 0f)
                view.setTextViewAlpha(next, -alpha)
            }
        }else{
            view.setViewAlpha(now,value+255)
            view.setViewAlpha(pre,-value)

            val alpha = 255 + 2 * value
            if(alpha>0) {
                view.setTextViewAlpha(now, alpha)
                view.setTextViewAlpha(pre, 0f)
            }else{
                view.setTextViewAlpha(now, 0f)
                view.setTextViewAlpha(pre, -alpha)
            }
        }
    }

    private fun checkInvalid():Int{
        var sum = 0
        var index = planList.size - 1
        while (index >= 0){
            val temp = planList[index]

            if(differentDay(getNowString(DATETYPE.DATE_DATE),temp.lastSignDate) > temp.frequentDay){
                //如果过期
                val temp = realm?.where(Plan::class.java)
                        ?.equalTo("id",planList[index].id)
                        ?.findAll()
                temp!![0].planState = PLAN_FAIL

                planList.removeAt(index)
                sum++
            }

            index--
        }

        return sum
    }

}


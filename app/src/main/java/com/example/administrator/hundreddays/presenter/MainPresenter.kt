package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.constant.PLAN_COMPLETE
import com.example.administrator.hundreddays.constant.PLAN_FAIL
import com.example.administrator.hundreddays.constant.PLAN_ING
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.blurImageView
import com.example.administrator.hundreddays.util.differentDay
import com.example.administrator.hundreddays.util.getNowString
import com.example.administrator.hundreddays.view.*
import io.realm.Realm
import java.util.*


class MainPresenter(val view: MainView, private val context: Context,val realm: Realm? = Realm.getDefaultInstance()) {
    private val TAG = "MainPresenter"

    lateinit var planList:MutableList<History>

    var position = 0

    fun initData(){
        var sum = 0

        realm?.beginTransaction()
        val userList = realm?.where(History::class.java)
                ?.equalTo("state", PLAN_ING)
                ?.findAll()
        planList = userList!!.toMutableList()

        sum = checkInvalid()

        if(planList.size == 0){
            view.setVisibility(false)
        }else {
            view.setVisibility(true)
            view.setData(planList)
            changeIndex(position,true)
        }
        realm?.commitTransaction()
        if(sum > 0){
            view.setMessageDialog("你有$sum 个任务过期了,快去回收站看看吧")
        }
    }

    fun sign(pos:Int,message:String) {
        Log.i(TAG,"sign")
        val temp = planList[pos]
        if(temp.lastSignDate == getNowString(DATETYPE.DATE_DATE)){
            view.setMessageDialog("已经签到过了哦")
            return
        }
        //------------------------------------------------------------------------------------------
        realm?.beginTransaction()

        val history = realm?.where(History::class.java)
                ?.endsWith("id",temp.id)
                ?.findFirst()

        if(differentDay(getNowString(DATETYPE.DATE_DATE), history!!.plan!!.createDateTime) >= history.plan!!.targetDay){
            history.state = PLAN_COMPLETE
        }
        history.keepDay = history.keepDay++
        history.lastSignDate = getNowString(DATETYPE.DATE_DATE)
        temp.keepDay++
        temp.lastSignDate = getNowString(DATETYPE.DATE_DATE)

        val sign = realm?.createObject(Sign::class.java,UUID.randomUUID().toString())
        sign?.message = message
        sign?.date = getNowString(DATETYPE.DATE_DATE)
        sign?.plan = history.plan

        realm?.commitTransaction()
        //------------------------------------------------------------------------------------------

        view.signSuccess(pos)
    }

    private val target =object :  SimpleTarget<Bitmap>(500,700){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            view.setBackgroud(blurImageView(context,resource,3f)!!)
        }
    }
    private fun getBlurBitmap(path: String?){
        Glide
                .with(context) // could be an issue!
                .asBitmap()
                .load(path)
                .into(target)
    }

    fun changeIndex(index:Int,isUpdate:Boolean = false){
        if(position == index && !isUpdate){
            return
        }
        position = index
        view.setMessage(planList[index].plan!!.title, "${index+1}/${planList.size}")
        getBlurBitmap(planList[index].plan!!.imgPath)
    }

    private fun checkInvalid():Int{
        var sum = 0
        var index = planList.size-1
        while (index>-1){
            val temp = planList[index]
            // Log.i(TAG," 1 ${getNowDateString()}  2 ${temp.lastSignDay} 3 ${temp.plan?.frequentDay}")
            if(differentDay(getNowString(DATETYPE.DATE_DATE),temp.lastSignDate) > temp.plan?.frequentDay!!){
                //如果过期
                val temp = realm
                        ?.where(History::class.java)
                        ?.equalTo("id",planList[index].id)
                        ?.findAll()
                temp!![0].state = PLAN_FAIL

                planList.removeAt(index)
                sum++
            }

            index--
        }

        return sum
    }

}


package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.constant.SCREEN_HEIGHT
import com.example.administrator.hundreddays.constant.SCREEN_WIDTH
import com.example.administrator.hundreddays.constant.bitmapMap
import com.example.administrator.hundreddays.constant.blurBitmapMap
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.getMessageBlurPath
import com.example.administrator.hundreddays.util.getNowString
import com.example.administrator.hundreddays.util.getValueFromSharedPreferences
import com.example.administrator.hundreddays.view.PlanMessageView
import io.realm.Realm

class PlanMessagePresenter(val context: Context,val view: PlanMessageView,val realm: Realm? = Realm.getDefaultInstance()) {
    private val TAG = "PlanMessagePresenter"

    lateinit var plan: Plan
    val signMap: MutableMap<String,Sign> = mutableMapOf()
    var planId = ""

    fun initData(id:String){
        realm?.beginTransaction()
        //------------------------------------------------------------------------------------------
        val temp = realm?.where(Plan::class.java)
                ?.equalTo("id",id)
                ?.findFirst()
        if (temp == null){
            Log.i(TAG,"plan error")
            return
        }
        planId = temp.id!!
        plan = temp
        view.returnMessage(plan,plan.signDays,plan.planState,plan.lastSignDate == getNowString(DATETYPE.DATE_DATE))
        //------------------------------------------------------------------------------------------
        realm?.commitTransaction()

        val bck = bitmapMap[plan.id]
        if (bck!=null){
            view.setBck(bck)
        }else{
            Glide.with(context)
                    .asBitmap()
                    .load(plan.imgPath)
                    .into(bitmapTarget)
        }
        val blurBck = blurBitmapMap[plan.id]
        if (blurBck!=null){
            view.setBlurBck(blurBck)
        }else{
            Glide.with(context)
                    .asBitmap()
                    .load(getMessageBlurPath(plan.imgPath))
                    .into(blurBitmapTarget)
        }
    }

    private val bitmapTarget =object :  SimpleTarget<Bitmap>(getValueFromSharedPreferences(SCREEN_WIDTH)!!.toInt()/2, getValueFromSharedPreferences(SCREEN_HEIGHT)!!.toInt()/2){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            view.setBck(resource)
        }
    }
    private val blurBitmapTarget =object :  SimpleTarget<Bitmap>(getValueFromSharedPreferences(SCREEN_WIDTH)!!.toInt()/2, getValueFromSharedPreferences(SCREEN_HEIGHT)!!.toInt()/2){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            view.setBlurBck(resource)
        }
    }

    fun lateInit(){
        realm?.beginTransaction()

        val signList = realm
                ?.where(Sign::class.java)
                ?.findAll()

        val list = signList?.toMutableList()!!
        Log.i(TAG,"list.size = ${list.size}")

        var index = list.size-1
        while(index>-1){
            if(list[index].plan?.id == planId){
                signMap[list[index].date] = list[index]
                Log.i(TAG,list[index].date+"   "+list[index].plan?.title)
            }else{
                list.removeAt(index)
            }
            index--
        }

        realm?.commitTransaction()

        view.setSignData(signMap)
    }

}
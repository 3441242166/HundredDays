package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.blurImageView
import com.example.administrator.hundreddays.util.getNowString
import com.example.administrator.hundreddays.view.PlanMessageView
import io.realm.Realm

class PlanMessagePresenter(val context: Context,val view: PlanMessageView,val realm: Realm? = Realm.getDefaultInstance()) {
    private val TAG = "PlanMessagePresenter"

    lateinit var history: History
    val signMap: MutableMap<String,Sign> = mutableMapOf()
    var planId = ""

    fun initData(id:String){
        //------------------------------------------------------------------------------------------
        realm?.beginTransaction()
        val temp = realm?.where(History::class.java)
                ?.equalTo("id",id)
                ?.findFirst()
        planId = temp?.plan!!.ID!!
        history = temp!!
        view.returnMessage(history.plan!!,history.keepDay,history.state,history.lastSignDate == getNowString(DATETYPE.DATE_DATE))
        realm?.commitTransaction()
        //------------------------------------------------------------------------------------------
        Glide
                .with(context) // could be an issue!
                .asBitmap()
                .load(history.plan!!.imgPath)
                .into(target)
    }
    private val target =object :  SimpleTarget<Bitmap>(1080,1920){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            view.setBlurBck(blurImageView(context,resource,25f)!!)
        }
    }

    fun lateInit(){
        realm?.beginTransaction()

        val signList = realm?.where(Sign::class.java)
                ?.findAll()

        val list = signList?.toMutableList()!!
        Log.i(TAG,"list.size = ${list.size}")

        var index = list.size-1
        while(index>-1){
            if(list[index].plan?.ID == planId){
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
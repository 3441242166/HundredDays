package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.administrator.hundreddays.base.BarBaseActivity
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.sqlite.HistoryDao
import com.example.administrator.hundreddays.sqlite.IngDao
import com.example.administrator.hundreddays.sqlite.PlanDao
import com.example.administrator.hundreddays.sqlite.SignDao
import com.example.administrator.hundreddays.util.blurImageView
import com.example.administrator.hundreddays.util.differentDay
import com.example.administrator.hundreddays.util.getNowDateString
import com.example.administrator.hundreddays.view.*

class MainPresenter(val view: MainView, private val context: Context) {


    fun initData(){
        val list = IngDao().alter(2)

        var index = list.size-1
        var sum = 0

        while (index>=0){
            val ing = list[index]
            if(differentDay(getNowDateString(),ing.lastSignDay)> ing.plan?.frequentDay ?:0 ){
                //如果过期 从ING表删除  然后加入HISTORY表
                IngDao().delete(ing.id)
                val history = History(ing.id)
                history.keppDay = SignDao().alter(ing.id?:-1).size
                addHistory(history)
                list.removeAt(index)
                sum++
            }
            index--
        }

        view.success(list)
        if(sum>0){
            view.setMessageDialog("你有$sum 个任务过期了,快去回收站看看吧")
        }
    }

    fun sign(planIng: PlanIng) {
        IngDao().update(planIng)
        SignDao().insert(Sign(planIng.id,"", planIng.lastSignDay,null))
    }

    fun addHistory(history: History){
        HistoryDao().insert(history)
    }

    fun getBlurBitmap(path: String?){
        Glide
                .with(context) // could be an issue!
                .asBitmap()
                .load(path)
                .into(target)
    }

    private val target =object :  SimpleTarget<Bitmap>(500,700){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            view.setBackgroud(blurImageView(context,resource,3f)!!)
        }
    }

}


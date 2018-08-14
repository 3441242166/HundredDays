package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.sqlite.HistoryDao
import com.example.administrator.hundreddays.sqlite.IngDao
import com.example.administrator.hundreddays.sqlite.SignDao
import com.example.administrator.hundreddays.util.blurImageView
import com.example.administrator.hundreddays.util.differentDay
import com.example.administrator.hundreddays.util.getNowDateString
import com.example.administrator.hundreddays.view.*

class MainPresenter(val view: MainView, private val context: Context) {
    private val TAG = "MainPresenter"

    private val signDao = SignDao()
    private val ingDao = IngDao()
    private val historyDao = HistoryDao()

    lateinit var planList:MutableList<PlanIng>

    var position = 0

    fun initData(){
        val list = ingDao.alter()

        var sum = 0     //统计过期任务数

        var index = list.size-1
        while (index>-1){
            val temp = list[index]
            // Log.i(TAG," 1 ${getNowDateString()}  2 ${temp.lastSignDay} 3 ${temp.plan?.frequentDay}")

            if(differentDay(getNowDateString(),temp.lastSignDay) > temp.plan?.frequentDay!!){
                //如果过期 从ING表删除  然后加入HISTORY表
                ingDao.delete(temp.id)

                addHistory(History(temp.id,signDao.alter(temp.id!!).size,0,null))
                Log.i(TAG,"任务过期")
                list.removeAt(index)
                sum++
            }

            index--
        }

        if(list.size == 0){
            view.setVisibility(false)
        }else {
            view.setVisibility(true)
            planList = list
            view.setData(list)
            changeIndex(position,true)
        }

        if(sum > 0){
            view.setMessageDialog("你有$sum 个任务过期了,快去回收站看看吧")
        }
    }

    fun sign(pos:Int,message:String) {
        val planIng = planList[pos]
        //------------------------------------------
        if(planIng.isFinish){
            view.setMessageDialog("已经签到过了哦")
            return
        }

        planIng.insistentDay+=1
        planIng.lastSignDay = getNowDateString()
        planIng.isFinish = true

        // 如果 达到目标天数
        if(differentDay(getNowDateString(), planIng.plan!!.createDateTime) >= planIng.plan!!.targetDay){
            Log.i(TAG,"达到目标天数")
            addHistory(History(planIng.id,signDao.planSum(planIng.id!!),1,null))
            ingDao.delete(planIng.id)
        }else{
            ingDao.update(planIng)
        }
        signDao.insert(Sign(planIng.id,message, getNowDateString(),null))
        view.signSuccess(pos)
    }

    private fun addHistory(history: History){
        historyDao.insert(history)
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

}


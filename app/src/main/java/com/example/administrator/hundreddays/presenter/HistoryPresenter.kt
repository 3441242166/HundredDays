package com.example.administrator.hundreddays.presenter

import android.content.Context
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.sqlite.HistoryDao
import com.example.administrator.hundreddays.util.getNowDateTimeString
import com.example.administrator.hundreddays.util.getNowTimeString
import com.example.administrator.hundreddays.view.HistoryView

class HistoryPresenter(val view: HistoryView, val context: Context) {

    val historyDao  = HistoryDao()

    fun initData(){
        val list = historyDao.alter()
//        val data: MutableList<History> = mutableListOf()
//        for(pos in 1..10){
//            val plan = Plan(null,"666$pos", getNowTimeString(),"", getNowDateTimeString(),10,100)
//            val history = History(null,pos,plan)
//            data.add(history)
//        }

        view.setData(list)
    }

}
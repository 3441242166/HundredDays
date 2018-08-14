package com.example.administrator.hundreddays.presenter

import android.content.Context
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.constant.DB_ID
import com.example.administrator.hundreddays.sqlite.HistoryDao
import com.example.administrator.hundreddays.sqlite.IngDao
import com.example.administrator.hundreddays.sqlite.PlanDao
import com.example.administrator.hundreddays.sqlite.SignDao
import com.example.administrator.hundreddays.view.PlanMessageView

class PlanMessagePresenter(val context: Context,val view: PlanMessageView) {

    val signDao = SignDao()
    val ingDao = IngDao()
    val planDao = PlanDao()
    val historyDao = HistoryDao()

    var signList = listOf<Sign>()

    fun initData(id:Long){

        val plan = planDao.alterByID(id)
        signList = signDao.alter(DB_ID to id.toString())

    }


}
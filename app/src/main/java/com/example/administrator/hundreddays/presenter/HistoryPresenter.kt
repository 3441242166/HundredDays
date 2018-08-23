package com.example.administrator.hundreddays.presenter

import android.content.Context
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.view.HistoryView
import io.realm.Realm

class HistoryPresenter(val view: HistoryView, val context: Context,val realm: Realm? = Realm.getDefaultInstance()) {

    lateinit var planList:MutableList<History>

    fun initData(){
        realm?.beginTransaction()

        val userList = realm?.where(History::class.java)
                ?.findAll()
        planList = userList!!.toMutableList()

        realm?.commitTransaction()

        view.setData(planList)
    }

}
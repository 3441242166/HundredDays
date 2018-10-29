package com.example.administrator.hundreddays.presenter

import android.content.Context
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.view.HistoryView
import io.realm.Realm

class HistoryPresenter(val view: HistoryView, val context: Context,val realm: Realm? = Realm.getDefaultInstance()) {

    lateinit var planList:MutableList<Plan>

    fun initData(){
        realm?.beginTransaction()

        val userList = realm?.where(Plan::class.java)
                ?.findAll()
        planList = userList!!.toMutableList()
        //realm.copyToRealmOrUpdate(userList)

        realm?.commitTransaction()

        view.setData(planList)
    }

}
package com.example.administrator.hundreddays.view

import com.example.administrator.hundreddays.base.BaseView
import com.example.administrator.hundreddays.bean.Plan

interface HistoryView :BaseView<Plan> {

    fun setData(data:MutableList<Plan>)

}
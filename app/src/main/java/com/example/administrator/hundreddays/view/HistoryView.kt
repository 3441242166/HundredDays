package com.example.administrator.hundreddays.view

import com.example.administrator.hundreddays.base.BaseView
import com.example.administrator.hundreddays.bean.History

interface HistoryView :BaseView<History> {

    fun setData(data:MutableList<History>)

}
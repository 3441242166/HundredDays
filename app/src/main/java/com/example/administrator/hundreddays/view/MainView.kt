package com.example.administrator.hundreddays.view

import android.graphics.Bitmap
import com.example.administrator.hundreddays.base.BaseView
import com.example.administrator.hundreddays.bean.PlanIng

interface MainView : BaseView<MutableList<PlanIng>>{
    fun setBackgroud(bitmap: Bitmap)
    fun setMessageDialog(str:String)
}